package ui;

import com.example.EmployeeRecordsManagementSystem.dtos.RoleDto;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.swing.*;
import java.awt.*;

public class ButtonEditorRole extends DefaultCellEditor {
    private JButton button;
    private String label;
    private boolean clicked;
    private JTable table;
    private String token;
    private ManageRoles parent;

    public ButtonEditorRole(JCheckBox checkBox, ManageRoles parent, String token) {
        super(checkBox);
        this.parent = parent;
        this.token = token;

        button = new JButton();
        button.addActionListener(e -> fireEditingStopped());
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        this.table = table;
        label = value == null ? "" : value.toString();
        button.setText(label);
        clicked = true;
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        if (clicked) {
            int row = table.getSelectedRow();
            String idRole = table.getValueAt(row, 0).toString();

            if (label.equals("Delete")) {
                deleteRole(idRole);
            } else if (label.equals("Modify")) {
                openUpdateForm(idRole);
            }
        }
        clicked = false;
        return label;
    }

    private void deleteRole(String idRole) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);

            HttpEntity<Void> request = new HttpEntity<>(headers);
            restTemplate.exchange("http://localhost:8081/role/delete/" + idRole, HttpMethod.DELETE, request, String.class);

            JOptionPane.showMessageDialog(button, "Role deleted successfully!");
            parent.loadRoles();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(button, "Error deleting role: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openUpdateForm(String idRole) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<RoleDto> response = restTemplate.exchange(
                "http://localhost:8081/role/" + idRole,
                HttpMethod.GET,
                request,
                RoleDto.class
        );

        RoleDto role = response.getBody();

        parent.openAddOrUpdateRoleForm(role);
    }

}
