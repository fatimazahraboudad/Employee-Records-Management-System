package ui;

import com.example.EmployeeRecordsManagementSystem.dtos.RoleDto;
import com.example.EmployeeRecordsManagementSystem.dtos.UserDto;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import javax.swing.*;
import java.awt.*;
import java.util.stream.Collectors;

public class AddOrUpdateRoleForm extends JFrame {
    private JTextField nameField;
    private JButton saveButton, cancelButton;
    private String token;
    private ManageRoles parent;
    private RoleDto role;

    public AddOrUpdateRoleForm(String token, ManageRoles parent, RoleDto role) {
        this.token = token;
        this.parent = parent;
        this.role = role;

        setTitle(role == null ? "Add Role" : "Update Role");
        setSize(600, 300);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Role name:"), gbc);

        gbc.gridx = 1;
        nameField = new JTextField(20);
        add(nameField, gbc);



        if (role != null) {
            nameField.setText(role.getName());
        }

        saveButton = new JButton(role == null ? "Add" : "Update");
        saveButton.addActionListener(e -> saveRole());
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        add(saveButton, gbc);
    }

    private void saveRole() {
        try {
            RoleDto newRole = role == null ? new RoleDto() : role;
            newRole.setName(nameField.getText());

            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<RoleDto> addRequest = new HttpEntity<>(newRole,headers);
            HttpEntity<RoleDto> editRequest = new HttpEntity<>(role, headers);

            if (role == null) {
                restTemplate.postForEntity("http://localhost:8081/role/add", addRequest, RoleDto.class);
                JOptionPane.showMessageDialog(this, "Role added successfully!");
            } else {
                restTemplate.put("http://localhost:8081/role/update", editRequest, RoleDto.class);
                JOptionPane.showMessageDialog(this, "Role updated successfully!");
            }

            parent.loadRoles();
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error saving role: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}


