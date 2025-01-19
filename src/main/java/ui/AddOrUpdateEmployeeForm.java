package ui;

import com.example.EmployeeRecordsManagementSystem.dtos.EmployeeDto;
import com.example.EmployeeRecordsManagementSystem.enumeration.EmploymentStatus;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class AddOrUpdateEmployeeForm extends JFrame {
    private JTextField fullNameField, emailField, jobTitleField, departmentField, addressField, hireDateField,contactInformationField;
    private JComboBox<EmploymentStatus> employmentStatusComboBox;
    private JButton saveButton, cancelButton;
    private String token;
    private EmployeeManagementForm parent;
    private EmployeeDto employee;

    public AddOrUpdateEmployeeForm(String token, EmployeeManagementForm parent, EmployeeDto employee) {
        this.token = token;
        this.parent = parent;
        this.employee = employee;

        setTitle(employee == null ? "Add Employee" : "Update Employee");
        setSize(400, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Full Name:"), gbc);

        gbc.gridx = 1;
        fullNameField = new JTextField(20);
        add(fullNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Email:"), gbc);

        gbc.gridx = 1;
        emailField = new JTextField(20);
        add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Job Title:"), gbc);

        gbc.gridx = 1;
        jobTitleField = new JTextField(20);
        add(jobTitleField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel("Department:"), gbc);

        gbc.gridx = 1;
        departmentField = new JTextField(20);
        add(departmentField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        add(new JLabel("Address:"), gbc);

        gbc.gridx = 1;
        addressField = new JTextField(20);
        add(addressField, gbc);


        gbc.gridx = 0;
        gbc.gridy = 5;
        add(new JLabel("contact Information:"), gbc);

        gbc.gridx = 1;
        contactInformationField = new JTextField(20);
        add(contactInformationField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        add(new JLabel("hire Date:"), gbc);

        gbc.gridx = 1;
        hireDateField = new JTextField(20);
        add(hireDateField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        add(new JLabel("employment Status:"), gbc);

        gbc.gridx = 1;
        employmentStatusComboBox = new JComboBox<>(EmploymentStatus.values());
        add(employmentStatusComboBox, gbc);

        if (employee != null) {
            fullNameField.setText(employee.getFullName());
            emailField.setText(employee.getEmail());
            jobTitleField.setText(employee.getJobTitle());
            departmentField.setText(employee.getDepartment());
            addressField.setText(employee.getAddress());
            contactInformationField.setText(employee.getContactInformation());
            hireDateField.setText(LocalDate.now().toString());
            employmentStatusComboBox.setSelectedItem(employee.getEmploymentStatus().toString());
        }

        saveButton = new JButton(employee == null ? "Add" : "Update");
        saveButton.addActionListener(e -> saveEmployee());
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        add(saveButton, gbc);
    }

    private void saveEmployee() {
        try {
            EmployeeDto newEmployee = employee == null ? new EmployeeDto() : employee;
            newEmployee.setFullName(fullNameField.getText());
            newEmployee.setEmail(emailField.getText());
            newEmployee.setJobTitle(jobTitleField.getText());
            newEmployee.setDepartment(departmentField.getText());
            newEmployee.setAddress(addressField.getText());
            newEmployee.setContactInformation(contactInformationField.getText());
            newEmployee.setHireDate(LocalDate.parse(hireDateField.getText()));
            newEmployee.setEmploymentStatus((EmploymentStatus)employmentStatusComboBox.getSelectedItem());

            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<EmployeeDto> addRequest = new HttpEntity<>(newEmployee, headers);
            HttpEntity<EmployeeDto> editRequest = new HttpEntity<>(employee, headers);

            if (employee == null) {
                restTemplate.postForEntity("http://localhost:8081/employee/add", addRequest, EmployeeDto.class);
                JOptionPane.showMessageDialog(this, "Employee added successfully!");
            } else {
                restTemplate.put("http://localhost:8081/employee/update", editRequest, EmployeeDto.class);
                JOptionPane.showMessageDialog(this, "Employee updated successfully!");
            }

            parent.loadEmployees();
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error saving employee: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

