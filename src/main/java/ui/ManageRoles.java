package ui;

import com.example.EmployeeRecordsManagementSystem.dtos.JwtAuthenticationResponse;
import com.example.EmployeeRecordsManagementSystem.dtos.RoleDto;
import com.example.EmployeeRecordsManagementSystem.dtos.UserDto;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;

public class ManageRoles  extends JFrame {
    private JTable roleTable;
    private JButton addRoleButton;
    private String token;
    private UserDto authenticatedUser;
    private JwtAuthenticationResponse response;

    public ManageRoles(JwtAuthenticationResponse response) {
        this.token = response.getAccessToken();
        this.authenticatedUser = response.getUsers();
        this.response = response;

        setTitle("Role Management");
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




        addRoleButton = new JButton("Add Role");
        addRoleButton.setFont(new Font("Arial", Font.BOLD, 14));
        addRoleButton.setBackground(new Color(3, 66, 31));
        addRoleButton.setForeground(Color.WHITE);
        addRoleButton.setFocusPainted(false);
        addRoleButton.addActionListener(e -> openAddOrUpdateRoleForm(null));
        topPanel.add(addRoleButton);



        add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        roleTable = new JTable();
        loadRoles();
        JScrollPane scrollPane = new JScrollPane(roleTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Role Records"));
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
        employeeMenu.add(createStyledMenuItem("View Employees", e ->{
            EmployeeManagementForm employeeManagementForm = new EmployeeManagementForm(response);
            employeeManagementForm.setVisible(true);
            dispose();
        }));

        JMenu userMenu = createStyledMenu("User", "Manage users");
        userMenu.add(createStyledMenuItem("Manage Users", e -> {
            ManageUsers manageUsersPage = new ManageUsers(response);
            manageUsersPage.setVisible(true);
            dispose();
        }));

        JMenu roleMenu = createStyledMenu("Role", "Manage roles");
        roleMenu.add(createStyledMenuItem("Manage Roles", e -> loadRoles()));

        JMenu logoutMenu = createStyledMenu("Logout", "Exit the application");
        logoutMenu.add(createStyledMenuItem("Logout", e -> logout()));

        menuBar.add(employeeMenu);
        menuBar.add(Box.createHorizontalStrut(20));
        menuBar.add(userMenu);
        menuBar.add(Box.createHorizontalStrut(20));
        menuBar.add(roleMenu);
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(logoutMenu);

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


    public void loadRoles() {
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);


            HttpEntity<String> request = new HttpEntity<>(headers);

            ResponseEntity<RoleDto[]> response = restTemplate.exchange(
                    "http://localhost:8081/role/all",
                    HttpMethod.GET,
                    request,
                    RoleDto[].class
            );

            RoleDto[] roles = response.getBody();

            if (roles != null) {
                String[] columnNames = new String[]{ "Id", "Role name", "Modify", "Delete"};

                Object[][] data = new Object[roles.length][columnNames.length];
                for (int i = 0; i < roles.length; i++) {
                    RoleDto emp = roles[i];
                    data[i][0] = emp.getIdRole();
                    data[i][1] = emp.getName();
                    data[i][2] = "Modify";
                    data[i][3] = "Delete";

                }

                DefaultTableModel model = new DefaultTableModel(data, columnNames) {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return column == 2 || column == 3;

                    }
                };

                roleTable.setModel(model);

                roleTable.getColumn("Modify").setCellRenderer(new ButtonRenderer());
                roleTable.getColumn("Modify").setCellEditor(new ButtonEditorRole(new JCheckBox(), this, token));

                roleTable.getColumn("Delete").setCellRenderer(new ButtonRenderer());
                roleTable.getColumn("Delete").setCellEditor(new ButtonEditorRole(new JCheckBox(), this, token));


                roleTable.setModel(model);
                roleTable.setFont(new Font("Arial", Font.PLAIN, 14));
                roleTable.setRowHeight(30);
                roleTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
                roleTable.getTableHeader().setBackground(new Color(70, 130, 180));
                roleTable.getTableHeader().setForeground(Color.BLACK);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading roles: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    void openAddOrUpdateRoleForm(RoleDto role) {
        new AddOrUpdateRoleForm(token, this, role).setVisible(true);
    }
}
