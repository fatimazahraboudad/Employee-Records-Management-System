package ui;

import com.example.EmployeeRecordsManagementSystem.dtos.EmployeeDto;
import com.example.EmployeeRecordsManagementSystem.dtos.JwtAuthenticationResponse;
import com.example.EmployeeRecordsManagementSystem.dtos.UserDto;
import com.example.EmployeeRecordsManagementSystem.enumeration.EmploymentStatus;
import com.example.EmployeeRecordsManagementSystem.filters.EmployeeFilter;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import java.awt.event.ActionListener;
import java.time.LocalDate;
import javax.swing.border.EmptyBorder;

public class EmployeeManagementForm extends JFrame {
    private JTable employeeTable;
    private JButton addEmployeeButton;
    private String token;
    private UserDto authenticatedUser;
    private JTextField departmentField ;
    private JComboBox<EmploymentStatus> employmentStatusComboBox;
    private JTextField hireDateField;
    private JwtAuthenticationResponse response;

    public EmployeeManagementForm(JwtAuthenticationResponse response) {
        this.token = response.getAccessToken();
        this.authenticatedUser = response.getUsers();
        this.response = response;

        setTitle("Employee Management");
        setSize(1000, 750);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        createNavBar();

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        topPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        departmentField = new JTextField(15);
        departmentField.setToolTipText("Filter by department");
        topPanel.add(new JLabel("Department:"));
        topPanel.add(departmentField);



        employmentStatusComboBox = new JComboBox<>();
        employmentStatusComboBox.addItem(null);
        for (EmploymentStatus status : EmploymentStatus.values()) {
            employmentStatusComboBox.addItem(status);
        }
        employmentStatusComboBox.setToolTipText("Filter by employment status");
        topPanel.add(employmentStatusComboBox);



        hireDateField = new JTextField(10);
        hireDateField.setToolTipText("Filter by hire date (YYYY-MM-DD)");
        topPanel.add(new JLabel("Hire Date:"));
        topPanel.add(hireDateField);

        JButton filterButton = new JButton("Filter");
        filterButton.setFont(new Font("Arial", Font.BOLD, 14));
        filterButton.setBackground(new Color(3, 66, 31));
        filterButton.setForeground(Color.WHITE);
        filterButton.setFocusPainted(false);
        filterButton.addActionListener(e -> loadEmployees());
        topPanel.add(filterButton);


        if (authenticatedUser.getRole().size() == 1
                && authenticatedUser.getRole().stream().noneMatch(role -> role.getName().equals("Managers"))) {

            addEmployeeButton = new JButton("Add Employee");
            addEmployeeButton.setFont(new Font("Arial", Font.BOLD, 14));
            addEmployeeButton.setBackground(new Color(3, 66, 31));
            addEmployeeButton.setForeground(Color.WHITE);
            addEmployeeButton.setFocusPainted(false);
            addEmployeeButton.addActionListener(e -> openAddOrUpdateEmployeeForm(null));
            topPanel.add(addEmployeeButton);
        }


        add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        employeeTable = new JTable();
        loadEmployees();
        JScrollPane scrollPane = new JScrollPane(employeeTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Employee Records"));
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);


    }
    private void createNavBar() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(new Color(45, 45, 45));
        menuBar.setBorder(new EmptyBorder(5, 10, 5, 10));

        menuBar.setForeground(Color.WHITE);

        JMenu employeeMenu = createStyledMenu("Employee", "Manage employees");
        employeeMenu.add(createStyledMenuItem("View Employees", e -> loadEmployees()));



        JMenu userMenu = createStyledMenu("User", "Manage users");
        userMenu.add(createStyledMenuItem("Manage Users", e -> {
            ManageUsers manageUsersPage = new ManageUsers(response);
            manageUsersPage.setVisible(true);
            dispose();
        }));

        JMenu roleMenu = createStyledMenu("Role", "Manage roles");
        roleMenu.add(createStyledMenuItem("Manage Roles", e -> {

            ManageRoles manageRolesPage = new ManageRoles(response);
            manageRolesPage.setVisible(true);
            dispose();
        }));

        JMenu logoutMenu = createStyledMenu("Logout", "Exit the application");
        logoutMenu.add(createStyledMenuItem("Logout", e -> logout()));

        boolean isManager = authenticatedUser.getRole().stream()
                .anyMatch(role -> role.getName().equals("Administrators"));

        if (isManager) {
            menuBar.add(employeeMenu);
            menuBar.add(Box.createHorizontalStrut(20));
            menuBar.add(userMenu);
            menuBar.add(Box.createHorizontalStrut(20));
            menuBar.add(roleMenu);
            menuBar.add(Box.createHorizontalGlue());
            menuBar.add(logoutMenu);
        } else {
            menuBar.add(employeeMenu);
            menuBar.add(Box.createHorizontalGlue());
            menuBar.add(logoutMenu);

        }


        setJMenuBar(menuBar);
    }

    private JMenu createStyledMenu(String title, String toolTip) {
        JMenu menu = new JMenu(title);
        menu.setToolTipText(toolTip);
        menu.setFont(new Font("Arial", Font.BOLD, 14));
        menu.setForeground(Color.black);
        menu.setOpaque(true);
        menu.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        return menu;
    }

    private JMenuItem createStyledMenuItem(String title, ActionListener action) {
        JMenuItem menuItem = new JMenuItem(title);
        menuItem.addActionListener(action);
        menuItem.setFont(new Font("Arial", Font.PLAIN, 14));
        menuItem.setForeground(Color.WHITE);
        menuItem.setOpaque(true);
        menuItem.setBackground(new Color(55, 55, 55));
        menuItem.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        return menuItem;
    }




    private void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    private void logout() {
        int confirmed = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Logout Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirmed == JOptionPane.YES_OPTION) {
            try {
                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<String> response = restTemplate.exchange(
                        "http://localhost:8081/users/logout",
                        HttpMethod.GET,
                        null,
                        String.class
                );

                if (response.getStatusCode() == HttpStatus.OK) {
                    JOptionPane.showMessageDialog(this, response.getBody(), "Logout", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                    new LoginForm().setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this, "Logout failed: " + response.getBody(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "An error occurred while logging out: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    public void loadEmployees() {
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);

            EmployeeFilter filter = new EmployeeFilter();
            if (!departmentField.getText().isEmpty()) {
                filter.setDepartment(departmentField.getText());
            }
            if (employmentStatusComboBox.getSelectedItem() != null) {
                filter.setEmploymentStatus((EmploymentStatus) employmentStatusComboBox.getSelectedItem());
            }
            if (!hireDateField.getText().isEmpty()) {
                filter.setHireDate(LocalDate.parse(hireDateField.getText()));
            }

            HttpEntity<EmployeeFilter> request = new HttpEntity<>(filter, headers);

            ResponseEntity<EmployeeDto[]> response = restTemplate.exchange(
                    "http://localhost:8081/employee",
                    HttpMethod.POST,
                    request,
                    EmployeeDto[].class
            );

            EmployeeDto[] employees = response.getBody();

            if (employees != null) {
                String[] columnNames = authenticatedUser.getRole().size() == 1
                        && authenticatedUser.getRole().stream().anyMatch(role -> role.getName().equals("Managers"))
                        ? new String[]{"ID", "Full Name", "Email", "Job Title", "Department","Contact Information","Hire date","Employee status", "Modify"}
                        : new String[]{"ID", "Full Name", "Email", "Job Title", "Department" ,"Contact Information","Hire date","Employee status", "Modify", "Delete"};

                Object[][] data = new Object[employees.length][columnNames.length];
                for (int i = 0; i < employees.length; i++) {
                    EmployeeDto emp = employees[i];
                    data[i][0] = emp.getIdEmployee();
                    data[i][1] = emp.getFullName();
                    data[i][2] = emp.getEmail();
                    data[i][3] = emp.getJobTitle();
                    data[i][4] = emp.getDepartment();
                    data[i][5] = emp.getContactInformation();
                    data[i][6] = emp.getHireDate();
                    data[i][7] = emp.getEmploymentStatus().name();

                    data[i][8] = "Modify";
                    if (columnNames.length > 9) {
                        data[i][9] = "Delete";
                    }
                }

                DefaultTableModel model = new DefaultTableModel(data, columnNames) {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        if (columnNames.length > 9) {
                            return column == 8 || column == 9;
                        } else {
                            return column == 8;
                        }
                    }
                };

                employeeTable.setModel(model);

                employeeTable.getColumn("Modify").setCellRenderer(new ButtonRenderer());
                employeeTable.getColumn("Modify").setCellEditor(new ButtonEditor(new JCheckBox(), this, token));

                if (columnNames.length > 9) {
                    employeeTable.getColumn("Delete").setCellRenderer(new ButtonRenderer());
                    employeeTable.getColumn("Delete").setCellEditor(new ButtonEditor(new JCheckBox(), this, token));
                }

                employeeTable.setModel(model);
                employeeTable.setFont(new Font("Arial", Font.PLAIN, 14));
                employeeTable.setRowHeight(30);
                employeeTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
                employeeTable.getTableHeader().setBackground(new Color(70, 130, 180));
                employeeTable.getTableHeader().setForeground(Color.BLACK);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading employees: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    void openAddOrUpdateEmployeeForm(EmployeeDto employee) {
        new AddOrUpdateEmployeeForm(token, this, employee).setVisible(true);
    }
}
