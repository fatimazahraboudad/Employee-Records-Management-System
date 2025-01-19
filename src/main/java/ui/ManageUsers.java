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

public class ManageUsers extends JFrame {
    private JTable userTable;
    private JButton addUserButton;
    private String token;
    private UserDto authenticatedUser;
    private JwtAuthenticationResponse response;

    public ManageUsers(JwtAuthenticationResponse response) {
        this.token = response.getAccessToken();
        this.authenticatedUser = response.getUsers();
        this.response = response;

        setTitle("User Management");
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


        if (authenticatedUser.getRole().size() == 1
                && authenticatedUser.getRole().stream().noneMatch(role -> role.getName().equals("ROLE_MANAGER"))) {

            addUserButton = new JButton("Add User");
            addUserButton.setFont(new Font("Arial", Font.BOLD, 14));
            addUserButton.setBackground(new Color(3, 66, 31));
            addUserButton.setForeground(Color.WHITE);
            addUserButton.setFocusPainted(false);
            addUserButton.addActionListener(e -> openAddOrUpdateUserForm(null));
            topPanel.add(addUserButton);
        }


        add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        userTable = new JTable();
        loadUsers();
        JScrollPane scrollPane = new JScrollPane(userTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("User Records"));
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
        userMenu.add(createStyledMenuItem("Manage Users", e -> loadUsers()));


        JMenu roleMenu = createStyledMenu("Role", "Manage roles");
        roleMenu.add(createStyledMenuItem("Manage Roles", e -> {
            ManageRoles manageRolesPage = new ManageRoles(response);
            manageRolesPage.setVisible(true);
            dispose();
        }));

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


    public void loadUsers() {
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);


            HttpEntity<String> request = new HttpEntity<>(headers);

            ResponseEntity<UserDto[]> response = restTemplate.exchange(
                    "http://localhost:8081/admin/users",
                    HttpMethod.GET,
                    request,
                    UserDto[].class
            );

            UserDto[] users = response.getBody();

            if (users != null) {
                String[] columnNames = new String[]{ "Id","Email", "password", "Role", "Modify", "Delete","Add Role","Remove Role"};

                Object[][] data = new Object[users.length][columnNames.length];
                for (int i = 0; i < users.length; i++) {
                    UserDto emp = users[i];
                    data[i][0] = emp.getIdUser();
                    data[i][1] = emp.getEmail();
                    data[i][2] = emp.getPassword();
                    String roles = emp.getRole().stream()
                            .map(RoleDto::getName)
                            .reduce((role1, role2) -> role1 + ", " + role2)
                            .orElse("No Roles");

                    data[i][3] = roles;
                    data[i][4] = "Modify";
                    data[i][5] = "Delete";
                    data[i][6] = "Add Role";
                    data[i][7] = "Remove Role";


                }

                DefaultTableModel model = new DefaultTableModel(data, columnNames) {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return column == 4 || column == 5 || column == 6 || column == 7;

                    }
                };

                userTable.setModel(model);

                userTable.getColumn("Modify").setCellRenderer(new ButtonRenderer());
                userTable.getColumn("Modify").setCellEditor(new ButtonEditorUser(new JCheckBox(), this, token));

                userTable.getColumn("Delete").setCellRenderer(new ButtonRenderer());
                userTable.getColumn("Delete").setCellEditor(new ButtonEditorUser(new JCheckBox(), this, token));

                userTable.getColumn("Add Role").setCellRenderer(new ButtonRenderer());
                userTable.getColumn("Add Role").setCellEditor(new ButtonEditorUser(new JCheckBox(), this, token));

                userTable.getColumn("Remove Role").setCellRenderer(new ButtonRenderer());
                userTable.getColumn("Remove Role").setCellEditor(new ButtonEditorUser(new JCheckBox(), this, token));


                userTable.setModel(model);
                userTable.setFont(new Font("Arial", Font.PLAIN, 14));
                userTable.setRowHeight(30);
                userTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
                userTable.getTableHeader().setBackground(new Color(70, 130, 180));
                userTable.getTableHeader().setForeground(Color.BLACK);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading roles: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    void openAddOrUpdateUserForm(UserDto user) {
        new AddOrUpdateUserForm(token, this, user).setVisible(true);
    }

    void openAuthorityUserForm(UserDto user,String label,RoleDto[] roles) {
        new AddOrRemoveUserRoleForm(token, this, user,label,roles).setVisible(true);
    }
}
