package ui;

import com.example.EmployeeRecordsManagementSystem.dtos.JwtAuthenticationResponse;
import com.example.EmployeeRecordsManagementSystem.dtos.SignInRequest;
import org.springframework.web.client.RestTemplate;
import ui.service.EmployeeService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginForm extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton createAccountButton;
    private String token;

    public LoginForm() {
        setTitle("Login Form");
        setSize(400, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
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
        loginButton = new JButton("Login");
        add(loginButton, gbc);

        gbc.gridx = 1;
        createAccountButton = new JButton("Create Account");
        add(createAccountButton, gbc);

        loginButton.addActionListener(new LoginAction());
        createAccountButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Create Account Form (to be implemented)");
        });
    }

    private class LoginAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = emailField.getText();
            String password = new String(passwordField.getPassword());

            RestTemplate restTemplate = new RestTemplate();
            String apiUrl = "http://localhost:8081/users/login";

            try {
                SignInRequest loginRequest = new SignInRequest(username, password);
                JwtAuthenticationResponse response = restTemplate.postForObject(apiUrl, loginRequest, JwtAuthenticationResponse.class);

                if (response != null && response.getAccessToken() != null) {
                    token = response.getAccessToken();
                    JOptionPane.showMessageDialog(LoginForm.this, "Login Successful!");

                    EmployeeManagementUI employeeUI = new EmployeeManagementUI(token,new EmployeeService(token));
                    employeeUI.setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(LoginForm.this, "Invalid credentials!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(LoginForm.this, "Login failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }




    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginForm loginForm = new LoginForm();
            loginForm.setVisible(true);
        });
    }
}

