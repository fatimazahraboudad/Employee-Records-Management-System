package ui;


import com.example.EmployeeRecordsManagementSystem.dtos.UserDto;
import org.springframework.web.client.RestTemplate;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CreateAccountForm extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton registerButton;
    private JButton cancelButton;

    public CreateAccountForm() {
        setTitle("Create Account");
        setSize(400, 250);
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
        gbc.gridy = 1;
        add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        passwordField = new JPasswordField(20);
        add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        registerButton = new JButton("Register");
        add(registerButton, gbc);

        gbc.gridx = 1;
        cancelButton = new JButton("Cancel");
        add(cancelButton, gbc);

        registerButton.addActionListener(new RegisterAction());
        cancelButton.addActionListener(e -> dispose());
    }

    private class RegisterAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());

            RestTemplate restTemplate = new RestTemplate();
            String apiUrl = "http://localhost:8081/users/register";

            try {
                UserDto userDto = new UserDto();
                userDto.setEmail(email);
                userDto.setPassword(password);

                UserDto response = restTemplate.postForObject(apiUrl, userDto, UserDto.class);

                if (response != null) {
                    JOptionPane.showMessageDialog(CreateAccountForm.this, "Account created successfully!");
                    LoginForm loginForm = new LoginForm();
                    loginForm.setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(CreateAccountForm.this, "Registration failed!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(CreateAccountForm.this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
