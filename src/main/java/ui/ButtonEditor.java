package ui;

import com.example.EmployeeRecordsManagementSystem.dtos.EmployeeDto;
import com.example.EmployeeRecordsManagementSystem.dtos.JwtAuthenticationResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.swing.*;
import java.awt.*;

public class ButtonEditor extends DefaultCellEditor {
    private JButton button;
    private String label;
    private boolean clicked;
    private JTable table;
    private String token;
    private EmployeeManagementForm parent;

    public ButtonEditor(JCheckBox checkBox, EmployeeManagementForm parent, String token) {
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
            String idEmployee = table.getValueAt(row, 0).toString();

            if (label.equals("Delete")) {
                deleteEmployee(idEmployee);
            } else if (label.equals("Modify")) {
                openUpdateForm(idEmployee);
            }
        }
        clicked = false;
        return label;
    }

    private void deleteEmployee(String idEmployee) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);

            HttpEntity<Void> request = new HttpEntity<>(headers);
            restTemplate.exchange("http://localhost:8081/employee/delete/" + idEmployee, HttpMethod.DELETE, request, String.class);

            JOptionPane.showMessageDialog(button, "Employee deleted successfully!");
            parent.loadEmployees();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(button, "Error deleting employee: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openUpdateForm(String idEmployee) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<EmployeeDto> response = restTemplate.exchange(
                "http://localhost:8081/employee/" + idEmployee,
                HttpMethod.GET,
                request,
                EmployeeDto.class
        );

        EmployeeDto employee = response.getBody();

        parent.openAddOrUpdateEmployeeForm(employee);
    }

}
