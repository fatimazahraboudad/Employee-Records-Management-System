package com.example.EmployeeRecordsManagementSystem.services;

import com.example.EmployeeRecordsManagementSystem.dtos.JwtAuthenticationResponse;
import com.example.EmployeeRecordsManagementSystem.dtos.SignInRequest;
import com.example.EmployeeRecordsManagementSystem.dtos.UserDto;
import com.example.EmployeeRecordsManagementSystem.entities.Role;
import com.example.EmployeeRecordsManagementSystem.entities.User;
import com.example.EmployeeRecordsManagementSystem.exceptions.AlreadyExistException;
import com.example.EmployeeRecordsManagementSystem.exceptions.InvalidEmailOrPasswordException;
import com.example.EmployeeRecordsManagementSystem.exceptions.UserNotFoundException;
import com.example.EmployeeRecordsManagementSystem.mappers.RoleMapper;
import com.example.EmployeeRecordsManagementSystem.mappers.UserMapper;
import com.example.EmployeeRecordsManagementSystem.repositories.UserRepository;
import com.example.EmployeeRecordsManagementSystem.security.JwtTokenProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService{


    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    private final RoleService roleService;
    private final UserMapper userMapper;
    private final RoleMapper roleMapper;


    @Override
    public UserDto addUser(UserDto userDto) {
        if(userRepository.findByEmailIgnoreCase(userDto.getEmail()).isPresent()){
            throw new AlreadyExistException(userDto.getEmail());
        }
        User user = userMapper.toEntity(userDto);
        user.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
        if (user.getRole() == null) {
            user.setRole(new HashSet<>());
        }

        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userMapper.toDtos(userRepository.findAll());
    }

    @Override
    public UserDto getUserById(String idUser) {
        return userMapper.toDto(helper(idUser));
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        User user = helper(getCurrentUser().getIdUser());
        user.setEmail(userDto.getEmail());
        user.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));

        return userMapper.toDto(userRepository.save(user));
    }


    public User helper(String idUser) {
        return userRepository.findById(idUser).orElseThrow(() -> new UserNotFoundException(idUser));
    }

    @Override
    public JwtAuthenticationResponse signIn(SignInRequest request) throws Exception {

        User user = userRepository.findByEmailIgnoreCase(request.getEmail())
                .orElseThrow(InvalidEmailOrPasswordException::new);


        // Authenticate using email and password
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()));

        log.info("User authenticated");

        log.info("User found: {}", user.getEmail());

        String jwt = jwtTokenProvider.generateAccessToken(user);
        String refreshToken = jwtTokenProvider.generateRefreshToken(user);

        log.info("Access token generated: {}", jwt);
        log.info("Refresh token generated: {}",refreshToken);

        log.info("User return object: {}",JwtAuthenticationResponse.builder()
                .accessToken(jwt)
                .refreshToken(refreshToken)
                .users(userMapper.toDto(user))
                .build());
        return JwtAuthenticationResponse.builder()
                .accessToken(jwt)
                .refreshToken(refreshToken)
                .users(userMapper.toDto(user))
                .build();
    }

    @Override
    public void signOut(HttpServletRequest request, HttpServletResponse response) {
        request.getSession().invalidate();
        SecurityContextHolder.clearContext();

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            log.info("logout service ");

            for (Cookie cookie : cookies) {
                cookie.setPath("/");
                cookie.setMaxAge(0);
                cookie.setValue(null);
                response.addCookie(cookie);
            }
        }
    }


    @Override
    public UserDto getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof User) {
                return userMapper.toDto((User) principal);
            }
        }
        return null;

    }



    @Override
    public UserDto addAuthority(String idUser, String role) {
        User user=helper(idUser);
        Role addRole=roleMapper.toEntity(roleService.getRoleByName(role));
        if(role != null) {
//            if(user.getRole()
//                    .stream()
//                    .anyMatch(
//                        role1 -> role1.getIdRole().equals(addRole.getIdRole()))) {
//                throw new RolesException(idUser, role);
//            }
            user.getRole().add(addRole);
        }
        return userMapper.toDto(userRepository.save(user));
    }



    @Override
    public UserDto removeAuthority(String idUser, String role) {
        User user=helper(idUser);
        Role roleToRemove= roleMapper.toEntity(roleService.getRoleByName(role));
        if(roleToRemove != null) {
            user.getRole().removeIf(role1 -> role1.getIdRole().equals(roleToRemove.getIdRole()));
        }
        return userMapper.toDto(userRepository.save(user));
    }

}
