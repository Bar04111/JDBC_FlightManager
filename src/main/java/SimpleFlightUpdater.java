import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SimpleFlightUpdater {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/FlightDB?allowLoadLocalInfile=true&allowPublicKeyRetrieval=true&serverTimezone=Asia/Jerusalem&useSSL=false";
        String user = "root";

        try (Connection conn = DriverManager.getConnection(url, user, getUserInput("Enter MySQL root password: "))) {
            // Create a PreparedStatement with parameterized queries to prevent SQL injection
            String sql = "UPDATE Arrivals SET delay_status = ? WHERE flight_number = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            System.out.print("Enter new delay status for flight LX354: ");
            String userInput = getUserInput("");

                // Set the parameters of the PreparedStatement with user input
                pstmt.setString(1, userInput);
                pstmt.setString(2, "LX354");
                int rowsAffected = pstmt.executeUpdate();

            System.out.println(rowsAffected + " row(s) affected.");
        }
        } catch (SQLException e) {
            e.printStackTrace();
    }
    }

    private static String getUserInput(String prompt) {
        System.out.print(prompt);
        return new java.util.Scanner(System.in).nextLine();
}
}
