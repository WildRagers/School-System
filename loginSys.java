import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;

public class loginSys {
    public static final String USERS_FILE_PATH = "users.txt";
    public static final boolean USE_UI = true;  // if false, the homeGUI will not open, not recommended to touch
    
    
    public static String loggedInUsername = null;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new lgnframe(USERS_FILE_PATH));
    }
}

class lgnframe extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel statusLabel;
    private final String usersFilePath;

    public lgnframe(String usersFilePath) {
        this.usersFilePath = usersFilePath;

        setTitle("Login System");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(5, 1));

        add(new JLabel("Username:"));
        usernameField = new JTextField();
        add(usernameField);

        add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        add(passwordField);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new LoginActionListener());
        add(loginButton);

        statusLabel = new JLabel(" ", SwingConstants.CENTER);
        add(statusLabel);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private class LoginActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (validateCredentials(username, password)) {
                statusLabel.setText("Login successful!");
                statusLabel.setForeground(Color.GREEN);
                logAttempt("Successful Login", username);

                
                loginSys.loggedInUsername = username;

                String userRole = getUserRole(username);

                if (loginSys.USE_UI) {
                    new homeGUI.HomeFrame(userRole);  // will open the homegui assuming boolean is true
                }
                dispose();
            } else {
                statusLabel.setText("Incorrect username or password.");
                statusLabel.setForeground(Color.RED);
                logAttempt("Unsuccessful Login", username);
            }
        }

        private boolean validateCredentials(String username, String password) {
            File usersFile = new File(usersFilePath);
            if (!usersFile.exists()) {
                JOptionPane.showMessageDialog(lgnframe.this, "users.txt file not found. Recommended to reinstall", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            try (BufferedReader reader = new BufferedReader(new FileReader(usersFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] fields = line.split(",\\s*");
                    if (fields.length >= 3) {
                        String fileUsername = fields[1];
                        String filePassword = fields[2];

                        if (fileUsername.equals(username) && filePassword.equals(password)) {
                            return true;
                        }
                    }
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(lgnframe.this, "Couldn't validate users file, try again later", "Error", JOptionPane.ERROR_MESSAGE);
            }

            return false;
        }

        private void logAttempt(String attemptStatus, String username) {
            File logDir = new File("logs");
            if (!logDir.exists()) {
                logDir.mkdir();
            }

            File logFile = new File(logDir, "loginlogs.txt");

            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm 'GMT', EEEE, d MMMM yyyy");
            String currentTime = sdf.format(new Date());
            String logEntry = attemptStatus + " at " + currentTime + ", " + username;

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, true))) {
                writer.write(logEntry);
                writer.newLine();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(lgnframe.this, "Couldn't log the login attempt", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        private String getUserRole(String username) {
            File usersFile = new File(usersFilePath);
            try (BufferedReader reader = new BufferedReader(new FileReader(usersFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] fields = line.split(",\\s*");
                    if (fields.length >= 4) {
                        String fileUsername = fields[1];
                        if (fileUsername.equals(username)) {
                            return fields[3];  
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
