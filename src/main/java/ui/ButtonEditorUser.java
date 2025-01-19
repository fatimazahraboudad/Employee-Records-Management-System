package ui;

import com.example.EmployeeRecordsManagementSystem.dtos.EmployeeDto;
import com.example.EmployeeRecordsManagementSystem.dtos.RoleDto;
import com.example.EmployeeRecordsManagementSystem.dtos.UserDto;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class ButtonEditorUser extends DefaultCellEditor {
    private JButton button;
    private String label;
    private boolean clicked;
    private JTable table;
    private String token;
    private ManageUsers parent;

    public ButtonEditorUser(JCheckBox checkBox, ManageUsers parent, String token) {
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
            String idUser = table.getValueAt(row, 0).toString();

            if (label.equals("Delete")) {
                deleteUser(idUser);
            } else if (label.equals("Modify")) {
                openUpdateForm(idUser);
            } else if (label.equals("Add Role") || label.equals("Remove Role")) {
                openAuthorityForm(idUser,label);
            }
        }
        clicked = false;
        return label;
    }

    private void deleteUser(String idUser) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);

            HttpEntity<Void> request = new HttpEntity<>(headers);
            restTemplate.exchange("http://localhost:8081/users/delete/" + idUser, HttpMethod.DELETE, request, String.class);

            JOptionPane.showMessageDialog(button, "User deleted successfully!");
            parent.loadUsers();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(button, "Error deleting user: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openUpdateForm(String idUser) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<UserDto> response = restTemplate.exchange(
                "http://localhost:8081/users/get/" + idUser,
                HttpMethod.GET,
                request,
                UserDto.class
        );

        UserDto user = response.getBody();

        parent.openAddOrUpdateUserForm(user);
    }

    private void openAuthorityForm(String idUser,String label) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<UserDto> response = restTemplate.exchange(
                "http://localhost:8081/users/get/" + idUser,
                HttpMethod.GET,
                request,
                UserDto.class
        );

        UserDto user = response.getBody();

        ResponseEntity<RoleDto[]> response2 = restTemplate.exchange(
                "http://localhost:8081/role/all",
                HttpMethod.GET,
                request,
                RoleDto[].class
        );

        RoleDto[] roles = response2.getBody();

        parent.openAuthorityUserForm(user,label, roles);
    }

}
