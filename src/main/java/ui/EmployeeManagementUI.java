package ui;

import com.example.EmployeeRecordsManagementSystem.dtos.EmployeeDto;
import com.example.EmployeeRecordsManagementSystem.enumeration.EmploymentStatus;
import net.miginfocom.swing.MigLayout;
import ui.service.EmployeeService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;
import java.util.List;
public class EmployeeManagementUI extends JFrame {

    private JFrame frame;
    private JTextField employeeIdField;
    private JTextField fullNameField;
    private JTextField emailField;
    private JTextField jobTitleField;
    private JTextField departmentField;
    private JTextField hireDateField;
    private JTextField contactInformationField;
    private JTextField addressField;
    private JTextField employmentStatusField;
    private JTable employeeTable;
    private EmployeeService employeeService;

    private String token;

    public EmployeeManagementUI(String token, EmployeeService employeeService) {
        this.token = token;
        this.employeeService = employeeService;
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Employee Records Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new MigLayout("", "[grow][grow]", "[][][][][][][grow]"));

        createEmployeeForm();
        createEmployeeTable();

        updateEmployeeTable();

        frame.setVisible(true);
    }

    private void createEmployeeForm() {
        JLabel labelId = new JLabel("Employee ID: ");
        employeeIdField = new JTextField(20);
        JLabel labelFullName = new JLabel("Employee Name: ");
        fullNameField = new JTextField(20);
        JLabel labelJobTitle = new JLabel("Job Title: ");
        jobTitleField = new JTextField(20);
        JLabel labelEmail = new JLabel("Email: ");
        emailField = new JTextField(20);
        JLabel labelDepartment = new JLabel("Department: ");
        departmentField = new JTextField(20);
        JLabel labelContactInformation = new JLabel("Contact Information: ");
        contactInformationField = new JTextField(20);
        JLabel labelAddress = new JLabel("Address: ");
        addressField = new JTextField(20);
        JLabel labelHireDate = new JLabel("Hire Date: ");
        hireDateField = new JTextField(20);
        JLabel labelEmploymentStatus = new JLabel("Employment Status: ");
        employmentStatusField = new JTextField(20);

        JButton addButton = new JButton("Add Employee");
        addButton.addActionListener(e -> addEmployee());

        JButton updateButton = new JButton("Update Employee");
        updateButton.addActionListener(e -> updateEmployee());

        JButton deleteButton = new JButton("Delete Employee");
        deleteButton.addActionListener(e -> deleteEmployee());

        frame.add(labelId, "cell 0 0, alignx right");
        frame.add(employeeIdField, "cell 1 0, growx");
        frame.add(labelFullName, "cell 0 1, alignx right");
        frame.add(fullNameField, "cell 1 1, growx");
        frame.add(labelJobTitle, "cell 0 2, alignx right");
        frame.add(jobTitleField, "cell 1 2, growx");
        frame.add(labelEmail, "cell 0 3, alignx right");
        frame.add(emailField, "cell 1 3, growx");
        frame.add(labelDepartment, "cell 0 4, alignx right");
        frame.add(departmentField, "cell 1 4, growx");
        frame.add(labelContactInformation, "cell 0 5, alignx right");
        frame.add(contactInformationField, "cell 1 5, growx");
        frame.add(labelAddress, "cell 0 6, alignx right");
        frame.add(addressField, "cell 1 6, growx");
        frame.add(labelHireDate, "cell 0 7, alignx right");
        frame.add(hireDateField, "cell 1 7, growx");
        frame.add(labelEmploymentStatus, "cell 0 8, alignx right");
        frame.add(employmentStatusField, "cell 1 8, growx");

        frame.add(addButton, "cell 0 9, alignx right");
        frame.add(updateButton, "cell 1 9, alignx right");
        frame.add(deleteButton, "cell 0 10, alignx right");
    }

    private void createEmployeeTable() {
        String[] columnNames = {"Employee ID", "Employee Name", "Email", "Job Title", "Department", "Contact Info", "Address", "Hire Date", "Status"};
        employeeTable = new JTable(new DefaultTableModel(new Object[][]{}, columnNames));
        JScrollPane scrollPane = new JScrollPane(employeeTable);
        frame.add(scrollPane, "cell 0 11 2 1, grow");
    }

    private void addEmployee() {
        EmployeeDto employee = new EmployeeDto();
        employee.setFullName(fullNameField.getText());
        employee.setEmail(emailField.getText());
        employee.setJobTitle(jobTitleField.getText());
        employee.setDepartment(departmentField.getText());
        employee.setHireDate(LocalDate.parse(hireDateField.getText()));
        employee.setEmploymentStatus(EmploymentStatus.valueOf(employmentStatusField.getText().toUpperCase()));
        employee.setContactInformation(contactInformationField.getText());
        employee.setAddress(addressField.getText());

        employeeService.addEmployee(employee);
        updateEmployeeTable();
        clearFormFields();
    }

    private void updateEmployee() {
        EmployeeDto employee = new EmployeeDto();
        employee.setIdEmployee(employeeIdField.getText());
        employee.setFullName(fullNameField.getText());
        employee.setEmail(emailField.getText());
        employee.setJobTitle(jobTitleField.getText());
        employee.setDepartment(departmentField.getText());
        employee.setHireDate(LocalDate.parse(hireDateField.getText()));
        employee.setEmploymentStatus(EmploymentStatus.valueOf(employmentStatusField.getText().toUpperCase()));
        employee.setContactInformation(contactInformationField.getText());
        employee.setAddress(addressField.getText());

        employeeService.updateEmployee(employee);
        updateEmployeeTable();
        clearFormFields();
    }

    private void deleteEmployee() {
        String id = employeeIdField.getText();
        employeeService.deleteEmployee(id);
        updateEmployeeTable();
        clearFormFields();
    }

    private void updateEmployeeTable() {
        List<EmployeeDto> employees = employeeService.getAllEmployees();
        DefaultTableModel model = (DefaultTableModel) employeeTable.getModel();
        model.setRowCount(0);

        for (EmployeeDto employee : employees) {
            model.addRow(new Object[]{
                    employee.getIdEmployee(),
                    employee.getFullName(),
                    employee.getEmail(),
                    employee.getJobTitle(),
                    employee.getDepartment(),
                    employee.getContactInformation(),
                    employee.getAddress(),
                    employee.getHireDate(),
                    employee.getEmploymentStatus()
            });
        }
    }

    private void clearFormFields() {
        employeeIdField.setText("");
        fullNameField.setText("");
        emailField.setText("");
        jobTitleField.setText("");
        departmentField.setText("");
        hireDateField.setText("");
        employmentStatusField.setText("");
        contactInformationField.setText("");
        addressField.setText("");
    }

    public static void main(String[] args) {
        String token = null;
        EmployeeService employeeService = new EmployeeService(token);
        SwingUtilities.invokeLater(() -> new EmployeeManagementUI(token, employeeService));
    }
}
