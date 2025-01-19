package ui;

import com.example.EmployeeRecordsManagementSystem.dtos.RoleDto;
import com.example.EmployeeRecordsManagementSystem.dtos.UserDto;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class AddOrRemoveUserRoleForm extends JFrame {
    private JTextField idField;
    private JComboBox<String> roleNameComboBox;
    private JButton saveButton;
    private String token;
    private ManageUsers parent;
    private UserDto user;
    private String label;
    private RoleDto[] roles;

    public AddOrRemoveUserRoleForm(String token, ManageUsers parent, UserDto user, String label, RoleDto[] roles) {
        this.token = token;
        this.parent = parent;
        this.user = user;
        this.label = label;
        this.roles = roles;

        setTitle(label);
        setSize(600, 300);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("User Email ID:"), gbc);

        gbc.gridx = 1;
        idField = new JTextField(20);
        idField.setEditable(false);
        add(idField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Role Name:"), gbc);

        gbc.gridx = 1;
        String[] roleNames = Arrays.stream(roles)
                .map(RoleDto::getName)
                .toArray(String[]::new);

        roleNameComboBox = new JComboBox<>(roleNames);
        roleNameComboBox.setToolTipText("Select a role");
        add(roleNameComboBox, gbc);

        if (user != null) {
            idField.setText(user.getIdUser());
        }

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        saveButton = new JButton(label);
        saveButton.addActionListener(e -> saveUserRole());
        add(saveButton, gbc);
    }

    private void saveUserRole() {
        try {
            String id = idField.getText();
            String roleName = roleNameComboBox.getSelectedItem().toString();

            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Void> request = new HttpEntity<>(headers);

            String url;
            if ("Add Role".equalsIgnoreCase(label)) {
                url = "http://localhost:8081/admin/" + id + "/" + roleName;
                restTemplate.exchange(url, HttpMethod.GET, request, UserDto.class);
            } else if ("Remove Role".equalsIgnoreCase(label)) {
                url = "http://localhost:8081/admin/remove/" + id + "/" + roleName;
                restTemplate.exchange(url, HttpMethod.GET, request, UserDto.class);
            } else {
                throw new IllegalArgumentException("Invalid label: " + label);
            }

            JOptionPane.showMessageDialog(this, label + " applied successfully!");

            parent.loadUsers();
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error while " + label + ": " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}