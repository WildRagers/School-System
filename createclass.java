import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.*;

public class createclass {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CreateClassFrame());
    }
}

class CreateClassFrame extends JFrame {
    private JTextField classNameField;
    private JTextField teacherNameField;
    private JButton createButton;
    private JLabel statusLabel;
    private String admin = "admin";

    public CreateClassFrame() {
        setTitle("Create Class");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(4, 1));

        add(new JLabel("Enter Class Name:"));
        classNameField = new JTextField();
        add(classNameField);

        add(new JLabel("Enter Teacher Name:"));
        teacherNameField = new JTextField();
        add(teacherNameField);

        createButton = new JButton("Create Class");
        createButton.addActionListener(new CreateClassActionListener());
        add(createButton);

        statusLabel = new JLabel(" ", SwingConstants.CENTER);
        add(statusLabel);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private class CreateClassActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String className = classNameField.getText().trim();
            String teacherName = teacherNameField.getText().trim();

            if (!className.isEmpty() && !teacherName.isEmpty()) {
                createClassFile(className, teacherName);
                statusLabel.setText("Class created successfully.");
                statusLabel.setForeground(Color.GREEN);
            } else {
                statusLabel.setText("Please fill out all fields.");
                statusLabel.setForeground(Color.RED);
            }
        }

        private void createClassFile(String className, String classTeacher) {
            String sanitizedFileName = className.replace(" ", "_") + ".txt";
            File folder = new File("classes");
            File file = new File(folder, sanitizedFileName);

            try {
                if (!folder.exists()) {
                    folder.mkdirs(); 
                }

                if (file.createNewFile()) {
                    try (FileWriter writer = new FileWriter(file)) {
                        writer.write("Class name: " + className);
                        writer.write("\nClass Description: " + "N/A");
                        writer.write("\nClass Announcement: " + "N/A");
                        writer.write("\nClass admin: " + admin);
                        writer.write("\nClass teacher: " + classTeacher);
                        writer.write("\nClass students: " + "0");
                    }
                    logClassCreation(className);
                } else {
                    statusLabel.setText("Class already exists.");
                    statusLabel.setForeground(Color.RED);
                }
            } catch (IOException e) {
                statusLabel.setText("Error creating class.");
                statusLabel.setForeground(Color.RED);
            }
        }

        private void logClassCreation(String className) {
            File logDir = new File("logs");
            if (!logDir.exists()) {
                logDir.mkdir();
            }

            File logFile = new File(logDir, "classcreationlogs.txt");

            String logEntry = "Class '" + className + "' was created.";

            try (FileWriter writer = new FileWriter(logFile, true)) {
                writer.write(logEntry);
                writer.write("\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
