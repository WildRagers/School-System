import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class assignStudents {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AssignStudentsFrame());
    }
}

class AssignStudentsFrame extends JFrame {
    private JComboBox<String> classComboBox;
    private JTextField studentUsernameField;
    private JButton assignButton;
    private JLabel statusLabel;

    public AssignStudentsFrame() {
        setTitle("Assign Students");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(5, 1));

        if (!isAdminOrStaff()) {
            JOptionPane.showMessageDialog(this, "You must be an admin or staff to assign students.");
            System.exit(0);
        }

        classComboBox = new JComboBox<>(getAvailableClasses());
        add(new JLabel("Select Class to Assign Student:"));
        add(classComboBox);

        studentUsernameField = new JTextField();
        add(new JLabel("Enter Student Username:"));
        add(studentUsernameField);

        assignButton = new JButton("Assign");
        assignButton.addActionListener(new AssignActionListener());
        add(assignButton);

        statusLabel = new JLabel(" ", SwingConstants.CENTER);
        add(statusLabel);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private boolean isAdminOrStaff() {
        /*
        WARNING: IF YOU ARE IMPLEMENTING THIS IN A REAL SCENARIO, ADD LOGIC HERE TO INSURE THE USER IS AN ADMIN OR STAFF,
        WITHOUT, YOU LEAVE YOUR SYSTEM VUNERABLE
         */ 
        return true;  // THIS RETURN TRUE STATEMENT WILL AUTOMATICALLY SKIP ANY AUTHENTICATION!
    }

    private String[] getAvailableClasses() {
        File classesDir = new File("classes");
        List<String> classList = new ArrayList<>();

        if (classesDir.exists() && classesDir.isDirectory()) {
            for (File file : classesDir.listFiles()) {
                if (file.getName().endsWith(".txt")) {
                    String className = file.getName().replace(".txt", "").replace("_", " ");
                    classList.add(className);
                }
            }
        }

        return classList.toArray(new String[0]);
    }

    private class AssignActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String selectedClass = (String) classComboBox.getSelectedItem();
            String studentUsername = studentUsernameField.getText().trim();

            if (selectedClass != null && !studentUsername.isEmpty()) {
                if (assignStudentToClass(selectedClass, studentUsername)) {
                    statusLabel.setText("Student assigned successfully.");
                    statusLabel.setForeground(Color.GREEN);
                } else {
                    statusLabel.setText("Failed to assign student.");
                    statusLabel.setForeground(Color.RED);
                }
            } else {
                statusLabel.setText("Please select a class and enter a student username.");
                statusLabel.setForeground(Color.RED);
            }
        }

        private boolean assignStudentToClass(String className, String studentUsername) {
            File classFile = new File("classes/" + className.replace(" ", "_") + ".txt");
            if (classFile.exists()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(classFile));
                     BufferedWriter writer = new BufferedWriter(new FileWriter(classFile, true))) {
                    String line;
                    List<String> classData = new ArrayList<>();
                    while ((line = reader.readLine()) != null) {
                        classData.add(line);
                    }

                    String studentsLine = classData.stream()
                            .filter(lineContent -> lineContent.startsWith("Class students:"))
                            .findFirst()
                            .orElse(null);

                    if (studentsLine != null) {
                        String updatedStudents = studentsLine.replace("Class students: ", "") + ", " + studentUsername;
                        classData.remove(studentsLine);
                        classData.add("Class students: " + updatedStudents);
                    }

                    try (BufferedWriter finalWriter = new BufferedWriter(new FileWriter(classFile))) {
                        for (String dataLine : classData) {
                            finalWriter.write(dataLine);
                            finalWriter.newLine();
                        }
                    }
                    return true;
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            return false;
        }
    }
}
