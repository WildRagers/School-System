import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import javax.swing.*;

public class deleteUser {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DeleteUserFrame());
    }
}

class DeleteUserFrame extends JFrame {
    private JComboBox<String> userComboBox;
    private JButton deleteButton;
    private JLabel statusLabel;

    public DeleteUserFrame() {
        setTitle("Delete User");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(4, 1));

        userComboBox = new JComboBox<>(getAllUsers());
        add(new JLabel("Select User to Delete:"));
        add(userComboBox);

        deleteButton = new JButton("Delete User");
        deleteButton.addActionListener(new DeleteUserActionListener());
        add(deleteButton);

        statusLabel = new JLabel(" ", SwingConstants.CENTER);
        add(statusLabel);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private class DeleteUserActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String selectedUser = (String) userComboBox.getSelectedItem();

            if (selectedUser != null) {
                int confirm = JOptionPane.showConfirmDialog(DeleteUserFrame.this,
                        "Are you sure you want to delete user '" + selectedUser + "'?",
                        "Confirm Deletion", JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    if (deleteUserFromFile(selectedUser)) {
                        logUserDeletion(selectedUser);
                        statusLabel.setText("User '" + selectedUser + "' deleted.");
                        statusLabel.setForeground(Color.RED);
                    } else {
                        statusLabel.setText("Failed to delete user.");
                        statusLabel.setForeground(Color.RED);
                    }
                }
            }
        }

        private boolean deleteUserFromFile(String username) {
            File usersFile = new File("users.txt");
            File tempFile = new File("users_temp.txt");

            try (BufferedReader reader = new BufferedReader(new FileReader(usersFile));
                 BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
                String line;
                boolean userFound = false;

                while ((line = reader.readLine()) != null) {
                    String[] fields = line.split(",\\s*");
                    if (fields.length >= 2) {
                        String fileUsername = fields[1];
                        if (!fileUsername.equals(username)) {
                            writer.write(line);
                            writer.newLine();
                        } else {
                            userFound = true;
                        }
                    }
                }

                if (userFound) {
                    usersFile.delete();
                    tempFile.renameTo(usersFile);
                    return true;
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            return false;
        }

        private void logUserDeletion(String username) {
            File logDir = new File("logs");
            if (!logDir.exists()) {
                logDir.mkdir();
            }

            File logFile = new File(logDir, "usercreationlogs.txt");

            String logEntry = "User '" + username + "' deleted";

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, true))) {
                writer.write(logEntry);
                writer.newLine();
            } catch (IOException e) {
                System.out.println("An error occurred while writing the log.");
                e.printStackTrace();
            }
        }
    }

    private String[] getAllUsers() {
        ArrayList<String> usersList = new ArrayList<>();
        File usersFile = new File("users.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(usersFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",\\s*");
                if (fields.length >= 2) {
                    usersList.add(fields[1]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return usersList.toArray(new String[0]);
    }
}
