import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.*;

class ManageClassesFrame extends JFrame {
    private String loggedInUser;
    private JComboBox<String> classComboBox;
    private JButton manageButton;
    private JButton setDescriptionButton;
    private JButton setAnnouncementButton;
    private JButton viewStudentsButton;

    public ManageClassesFrame(String user) {
        this.loggedInUser = user;

        setTitle("Manage Classes");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(5, 1));

        classComboBox = new JComboBox<>(getClassesForUser(user));
        add(new JLabel("Select Class:"));
        add(classComboBox);

        manageButton = new JButton("Manage Class");
        manageButton.addActionListener(new ManageClassActionListener());
        add(manageButton);

        setDescriptionButton = new JButton("Set Class Description");
        setDescriptionButton.addActionListener(e -> setClassDescription());
        add(setDescriptionButton);

        setAnnouncementButton = new JButton("Set Class Announcement");
        setAnnouncementButton.addActionListener(e -> setClassAnnouncement());
        add(setAnnouncementButton);

        viewStudentsButton = new JButton("View All Students");
        viewStudentsButton.addActionListener(e -> viewAllStudents());
        add(viewStudentsButton);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private String[] getClassesForUser(String user) {
        File classesFolder = new File("classes");
        ArrayList<String> accessibleClasses = new ArrayList<>();
        if (classesFolder.exists()) {
            File[] classFiles = classesFolder.listFiles((dir, name) -> name.endsWith(".txt"));
            if (classFiles != null) {
                for (File classFile : classFiles) {
                    try (BufferedReader reader = new BufferedReader(new FileReader(classFile))) {
                        String line;
                        boolean isTeacher = false;
                        boolean isAdmin = false;

                        while ((line = reader.readLine()) != null) {
                            if (line.startsWith("Class teacher: " + user)) {
                                isTeacher = true;
                            }
                            if (line.startsWith("Class admin: " + user)) {
                                isAdmin = true;
                            }
                        }

                        if (isTeacher || isAdmin) {
                            accessibleClasses.add(classFile.getName().replace("_", " ").replace(".txt", ""));
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
        return accessibleClasses.toArray(new String[0]);
    }

    private void setClassDescription() {
        String selectedClass = (String) classComboBox.getSelectedItem();
        if (selectedClass != null) {
            String newDescription = JOptionPane.showInputDialog(this, "Enter new class description:");
            if (newDescription != null && !newDescription.trim().isEmpty()) {
                updateClassInfo(selectedClass, "Class Description", newDescription.trim());
                refreshClassComboBox();
            }
        }
    }

    private void setClassAnnouncement() {
        String selectedClass = (String) classComboBox.getSelectedItem();
        if (selectedClass != null) {
            String newAnnouncement = JOptionPane.showInputDialog(this, "Enter new class announcement:");
            if (newAnnouncement != null && !newAnnouncement.trim().isEmpty()) {
                updateClassInfo(selectedClass, "Class Announcement", newAnnouncement.trim());
                refreshClassComboBox();
            }
        }
    }

    private void updateClassInfo(String className, String infoType, String newInfo) {
        File classFile = new File("classes", className.replace(" ", "_") + ".txt");
        try {
            StringBuilder fileContent = new StringBuilder();
            String line;
            boolean updated = false;
            try (BufferedReader reader = new BufferedReader(new FileReader(classFile))) {
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith(infoType + ":")) {
                        fileContent.append(infoType).append(": ").append(newInfo).append("\n");
                        updated = true;
                    } else {
                        fileContent.append(line).append("\n");
                    }
                }
            }

            if (!updated) {
                fileContent.append(infoType).append(": ").append(newInfo).append("\n");
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(classFile))) {
                writer.write(fileContent.toString());
            }

            JOptionPane.showMessageDialog(this, infoType + " updated successfully!");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void viewAllStudents() {
        String selectedClass = (String) classComboBox.getSelectedItem();
        if (selectedClass != null) {
            File classFile = new File("classes", selectedClass.replace(" ", "_") + ".txt");
            try (BufferedReader reader = new BufferedReader(new FileReader(classFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("Class students:")) {
                        String students = line.substring("Class students:".length()).trim();
                        String[] studentArray = students.split(",\\s*");
                        studentArray = Arrays.stream(studentArray)
                                .filter(s -> !s.equals("0"))
                                .toArray(String[]::new);

                        String studentsList = studentArray.length > 0
                                ? String.join(", ", studentArray)
                                : "No students assigned.";
                        JOptionPane.showMessageDialog(this, "Students in class: " + studentsList);
                        break;
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void refreshClassComboBox() {
        classComboBox.setModel(new DefaultComboBoxModel<>(getClassesForUser(loggedInUser)));
    }

    private class ManageClassActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String selectedClass = (String) classComboBox.getSelectedItem();
            if (selectedClass != null) {
                SwingUtilities.invokeLater(() -> new ClassDetailsFrame(selectedClass, loggedInUser));
            }
        }
    }
}
