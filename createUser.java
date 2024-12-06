import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class createUser {

    public static void createUser(String fullName, String username, String password, String role) {
        if (!(role.equals("admin") || role.equals("student") || role.equals("staff"))) {
            System.out.println("Invalid role. Must be 'admin', 'student', or 'staff'.");
            return;
        }

        String userData = fullName + ", " + username + ", " + password + ", " + role;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("users.txt", true))) {
            writer.write(userData);
            writer.newLine();
            System.out.println("User added successfully.");
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
            e.printStackTrace();
        }

        logUserCreation(username, role);
    }

    private static void logUserCreation(String username, String role) {
        File logDir = new File("logs");
        if (!logDir.exists()) {
            logDir.mkdir();
        }

        File logFile = new File(logDir, "usercreationlogs.txt");

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm 'GMT', EEEE, d MMMM yyyy");
        String currentTime = sdf.format(new Date());

        String logEntry = "User '" + username + "' created at " + currentTime + ", assigned in the " + role + " group";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, true))) {
            writer.write(logEntry);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("An error occurred while writing the log.");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        createUser("Student Four", "stu4", "stu4", "student");
    }
}
