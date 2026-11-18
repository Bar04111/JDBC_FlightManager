import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class FlightViewer {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/FlightDB?allowLoadLocalInfile=true&allowPublicKeyRetrieval=true&serverTimezone=Asia/Jerusalem&useSSL=false";
        String user = "root";

        try (Connection conn = DriverManager.getConnection(url, user, getUserInput("Enter MySQL root password: "))) {
            System.out.print("Enter the terminal number (2, 3, 4, or 5): ");
            int terminalNumber = Integer.parseInt(getUserInput(""));

            if (!isValidTerminal(terminalNumber)) {
                System.out.println("Invalid terminal number. Please enter 2, 3, 4, or 5.");
                return;
            }

            String sql = "SELECT a.flight_number, ar.from_city, a.scheduled, a.delay_status, a.terminal FROM Arrivals a INNER JOIN FlightRoutes ar ON a.flight_number = ar.flight_number WHERE a.terminal = ? ORDER BY a.scheduled ASC";

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, String.valueOf(terminalNumber));
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (!rs.next()) {
                        System.out.println("No flights found for terminal " + terminalNumber);
                    } else {
                        do {
                            printFlightInfo(rs.getString("flight_number"),
                                    rs.getString("from_city"),
                                    rs.getString("scheduled"),
                                    rs.getString("delay_status"),
                                    rs.getString("terminal"));
                        } while (rs.next());
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static String getUserInput(String prompt) {
        System.out.print(prompt);
        return new Scanner(System.in).nextLine();
    }

    private static boolean isValidTerminal(int terminalNumber) {
        return terminalNumber >= 2 && terminalNumber <= 5;
    }

    private static void printFlightInfo(String flightNumber, String fromCity, String scheduled, String delayStatus, String terminal) {
        System.out.println("Flight Number: " + flightNumber);
        System.out.println("Departure City: " + fromCity);
        System.out.println("Scheduled Time: " + scheduled);
        System.out.println("Delay Status: " + delayStatus);
        System.out.println("Terminal: " + terminal);
        System.out.println("-----------------------------");
    }
}