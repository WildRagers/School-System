import java.awt.*;
import java.io.*;
import javax.swing.*;

class ClassDetailsFrame extends JFrame {
    private String className;
    private String teacher;
    private JLabel classNameLabel;
    private JLabel classDescriptionLabel;
    private JLabel classAnnouncementLabel;
    private JButton assignStudentButton;

    public ClassDetailsFrame(String className, String teacher) {
        this.className = className;
        this.teacher = teacher;

        setTitle("Class Details");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(5, 1));

        classNameLabel = new JLabel("Class Name: " + className);
        classDescriptionLabel = new JLabel("Class Description: N/A");
        classAnnouncementLabel = new JLabel("Class Announcement: N/A");
        assignStudentButton = new JButton("Assign Student");

        assignStudentButton.addActionListener(e -> assignStudent());

        add(classNameLabel);
        add(classDescriptionLabel);
        add(classAnnouncementLabel);
        add(assignStudentButton);

        fetchClassDetails();

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void fetchClassDetails() {
        File classFile = new File("classes", className.replace(" ", "_") + ".txt");

        if (classFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(classFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("Class Description: ")) {
                        classDescriptionLabel.setText(line);
                    } else if (line.startsWith("Class Announcement: ")) {
                        classAnnouncementLabel.setText(line);
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void assignStudent() {
        String username = JOptionPane.showInputDialog(this, "Enter student username to assign:");
        if (username != null && !username.trim().isEmpty()) {
            assignStudentToClass(username.trim());
        }
    }

    private void assignStudentToClass(String username) {
        File classFile = new File("classes", className.replace(" ", "_") + ".txt");

        try {
            StringBuilder fileContent = new StringBuilder();
            boolean foundStudentLine = false;

            try (BufferedReader reader = new BufferedReader(new FileReader(classFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("Class students: ")) {
                        String students = line.substring("Class students: ".length()).trim();
                        if (students.equals("0")) {
                            line = "Class students: " + username;
                        } else {
                            line += ", " + username;
                        }
                        foundStudentLine = true;
                    }
                    fileContent.append(line).append("\n");
                }
            }

            if (!foundStudentLine) {
                fileContent.append("Class students: ").append(username).append("\n");
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(classFile))) {
                writer.write(fileContent.toString());
            }

            JOptionPane.showMessageDialog(this, "Student assigned successfully!");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
