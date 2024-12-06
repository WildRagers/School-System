import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class createUserGUI {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(createUserGUI::createAndShowGUI);
    }

    public static void createAndShowGUI() {
        JFrame frame = new JFrame("Create User");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new GridLayout(5, 2));

        JLabel fullNameLabel = new JLabel("Full Name:");
        JTextField fullNameField = new JTextField();
        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();
        JLabel roleLabel = new JLabel("Role:");

        String[] roles = {"student", "staff", "admin"};
        JComboBox<String> roleComboBox = new JComboBox<>(roles);

        JButton createUserButton = new JButton("Create User");

        JLabel statusLabel = new JLabel("", SwingConstants.CENTER);

        frame.add(fullNameLabel);
        frame.add(fullNameField);
        frame.add(usernameLabel);
        frame.add(usernameField);
        frame.add(passwordLabel);
        frame.add(passwordField);
        frame.add(roleLabel);
        frame.add(roleComboBox);
        frame.add(createUserButton);
        frame.add(statusLabel);

        createUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String fullName = fullNameField.getText().trim();
                String username = usernameField.getText().trim();
                String password = new String(passwordField.getPassword()).trim();
                String role = (String) roleComboBox.getSelectedItem();

                if (fullName.isEmpty() || username.isEmpty() || password.isEmpty() || role.isEmpty()) {
                    statusLabel.setText("All fields are required!");
                    statusLabel.setForeground(Color.RED);
                } else {
                    createUser.createUser(fullName, username, password, role);
                    statusLabel.setText("User created successfully!");
                    statusLabel.setForeground(Color.GREEN);
                }
            }
        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
