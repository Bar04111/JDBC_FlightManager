import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class AdvancedFlightDispatcher {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/FlightDB?allowLoadLocalInfile=true&allowPublicKeyRetrieval=true&serverTimezone=Asia/Jerusalem&useSSL=false";
        String user = "root";

        try (Connection conn = DriverManager.getConnection(url, user, getUserInput("Enter MySQL root password: "))) {
            List<Integer> arrivalIds = Arrays.asList(1, 3, 5);
            String newStatus = "Landed - Final";

            updateMultipleDelays(conn, arrivalIds, newStatus);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateMultipleDelays(Connection conn, List<Integer> arrivalIds, String newStatus) throws SQLException {
        String sql = "UPDATE Arrivals SET delay_status = ? WHERE id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (int id : arrivalIds) {
                pstmt.setString(1, newStatus); // Set the status parameter once
                pstmt.setInt(2, id);           // Set the ID parameter for each iteration
                pstmt.addBatch();               // Add the current statement to the batch
            }

            int[] results = pstmt.executeBatch();
            int totalAffectedRows = 0;

            for (int count : results) {
                totalAffectedRows += count;
            }

            System.out.println(totalAffectedRows + " rows(s) updated.");
        }
    }

    private static String getUserInput(String prompt) {
        System.out.print(prompt);
        return new Scanner(System.in).nextLine();
    }
}