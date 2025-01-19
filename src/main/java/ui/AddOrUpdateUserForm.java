package ui;

import com.example.EmployeeRecordsManagementSystem.dtos.UserDto;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import javax.swing.*;
import java.awt.*;

public class AddOrUpdateUserForm extends JFrame {
    private JTextField emailField, passwordField;
    private JButton saveButton, cancelButton;
    private String token;
    private ManageUsers parent;
    private UserDto user;

    public AddOrUpdateUserForm(String token, ManageUsers parent, UserDto user) {
        this.token = token;
        this.parent = parent;
        this.user = user;

        setTitle(user == null ? "Add User" : "Update User");
        setSize(600, 300);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Email:"), gbc);

        gbc.gridx = 1;
        emailField = new JTextField(20);
        add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        passwordField = new JTextField(20);
        add(passwordField, gbc);


        if (user != null) {
            emailField.setText(user.getEmail());
            passwordField.setText(user.getPassword());

        }

        saveButton = new JButton(user == null ? "Add" : "Update");
        saveButton.addActionListener(e -> saveUser());
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        add(saveButton, gbc);
    }

    private void saveUser() {
        try {
            UserDto newUser = user == null ? new UserDto() : user;
            newUser.setEmail(emailField.getText());
            newUser.setPassword(passwordField.getText());

            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<UserDto> addRequest = new HttpEntity<>(newUser);
            HttpEntity<UserDto> editRequest = new HttpEntity<>(user, headers);

            if (user == null) {
                restTemplate.postForEntity("http://localhost:8081/users/register", addRequest, UserDto.class);
                JOptionPane.showMessageDialog(this, "User added successfully!");
            } else {
                restTemplate.put("http://localhost:8081/users/update", editRequest, UserDto.class);
                JOptionPane.showMessageDialog(this, "User updated successfully!");
            }

            parent.loadUsers();
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error saving user: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}


