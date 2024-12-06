import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import javax.swing.*;

public class AuthenticationFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public AuthenticationFrame() {
        setTitle("Authentication");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(3, 2));

        add(new JLabel("Username:"));
        usernameField = new JTextField();
        add(usernameField);

        add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        add(passwordField);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new LoginActionListener());
        add(loginButton);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private class LoginActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (validateCredentials(username, password)) {
                new ManageClassesFrame(username);
                dispose(); 
            } else {
                JOptionPane.showMessageDialog(AuthenticationFrame.this, "Invalid credentials", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        private boolean validateCredentials(String username, String password) {
            File usersFile = new File("users.txt");
            if (!usersFile.exists()) {
                JOptionPane.showMessageDialog(AuthenticationFrame.this, "users.txt file not found. Recommended to reinstall", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            try (BufferedReader reader = new BufferedReader(new FileReader(usersFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] fields = line.split(",\\s*");
                    if (fields.length >= 3 && fields[1].equals(username) && fields[2].equals(password)) {
                        return true;
                    }
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(AuthenticationFrame.this, "Couldn't validate users file, try again later", "Error", JOptionPane.ERROR_MESSAGE);
            }

            return false;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AuthenticationFrame());
    }
}
