import java.awt.*;
import javax.swing.*;

public class homeGUI {

    public static void main(String[] args) {
        String userRole = "student";

        SwingUtilities.invokeLater(() -> new HomeFrame(userRole));
    }

    static class HomeFrame extends JFrame {
        public HomeFrame(String userRole) {
            setTitle("Home");
            setSize(400, 300);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLayout(new GridLayout(8, 1));

            JButton viewClassesButton = new JButton("View Classes");
            JButton viewHomeworkButton = new JButton("View Homework");
            add(viewClassesButton);
            add(viewHomeworkButton);

            if ("admin".equals(userRole) || "staff".equals(userRole)) {
                JButton assignHomeworkButton = new JButton("Assign Homework");
                add(assignHomeworkButton);
            }

            if ("admin".equals(userRole)) {
                JButton createClassesButton = new JButton("Create Classes");
                JButton createUserButton = new JButton("Create User");
                JButton deleteUserButton = new JButton("Delete User");
                add(createClassesButton);
                add(createUserButton);
                add(deleteUserButton);

                createClassesButton.addActionListener(e -> {
                    SwingUtilities.invokeLater(() -> new CreateClassFrame());
                });

                viewClassesButton.addActionListener(e -> {
                    new viewclasses.ViewClassesFrame(loginSys.loggedInUsername);
                });

                createUserButton.addActionListener(e -> {
                    SwingUtilities.invokeLater(createUserGUI::createAndShowGUI);
                });

                deleteUserButton.addActionListener(e -> {
                    SwingUtilities.invokeLater(DeleteUserFrame::new);
                });
            }

            if ("admin".equals(userRole) || "staff".equals(userRole)) {
                JButton manageClassesButton = new JButton("Manage Classes");
                manageClassesButton.addActionListener(e -> {
                    SwingUtilities.invokeLater(AuthenticationFrame::new);
                });
                add(manageClassesButton);
            }

            setLocationRelativeTo(null);
            setVisible(true);
        }
    }
}
