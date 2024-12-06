import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class viewclasses {

    public static class ViewClassesFrame extends JFrame {
        private String loggedInUsername;

        public ViewClassesFrame(String username) {
            this.loggedInUsername = username;
            setTitle("View Classes");
            setSize(400, 300);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLayout(new BorderLayout());

            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

            JLabel headerLabel = new JLabel("Classes for " + loggedInUsername);
            headerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(headerLabel);

            List<String> userClasses = getUserClasses(loggedInUsername);

            if (userClasses.isEmpty()) {
                panel.add(new JLabel("No classes found for this user."));
            } else {
                for (String className : userClasses) {
                    panel.add(new JLabel(className));
                }
            }

            JScrollPane scrollPane = new JScrollPane(panel);
            add(scrollPane, BorderLayout.CENTER);

            setLocationRelativeTo(null);
            setVisible(true);
        }

        private List<String> getUserClasses(String username) {
            List<String> userClasses = new ArrayList<>();
            File classesDir = new File("classes");

            if (classesDir.exists() && classesDir.isDirectory()) {
                File[] classFiles = classesDir.listFiles((dir, name) -> name.endsWith(".txt"));

                if (classFiles != null) {
                    for (File classFile : classFiles) {
                        try (BufferedReader reader = new BufferedReader(new FileReader(classFile))) {
                            String line;
                            boolean isStudent = false;

                            while ((line = reader.readLine()) != null) {
                                if (line.startsWith("Class students:")) {
                                    String[] students = line.split(":")[1].trim().split(",\\s*");

                                    for (String student : students) {
                                        if (student.equals(username)) {
                                            isStudent = true;
                                            break;
                                        }
                                    }
                                    if (isStudent) {
                                        String className = classFile.getName().replace(".txt", "");
                                        userClasses.add(className);
                                        break;
                                    }
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            return userClasses;
        }
    }
}
