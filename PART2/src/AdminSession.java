import java.math.BigDecimal;
import java.util.Scanner;

// AdminSession Class
public class AdminSession {

    private static final String ADMIN_PASSWORD = "CityRide2026";
    private static final int MAX_ATTEMPTS = 3;
    private static final String SEPARATOR = "--------------------";

    private final Scanner scanner;
    private final ConfigManager configManager;

    public AdminSession(Scanner scanner, ConfigManager configManager) {
        this.scanner = scanner;
        this.configManager = configManager;
    }

    public void start() {
        boolean authenticated = attemptLogin();
        if (!authenticated) {
            System.out.println(SEPARATOR);
            System.out.println("Access denied. Returning to role selection.");
            System.out.println(SEPARATOR);
            return;
        }

        runAdminMenu();
    }

    private boolean attemptLogin() {
        int attempts = 0;
        while (attempts < MAX_ATTEMPTS) {
            System.out.print("Enter admin password: ");
            String input = scanner.nextLine().trim();
            if (input.equals(ADMIN_PASSWORD)) {
                System.out.println("Access granted. Welcome, Admin.");
                return true;
            }
            attempts++;
            int remaining = MAX_ATTEMPTS - attempts;
            if (remaining > 0) {
                System.out.println("Incorrect password. " + remaining + " attempt(s) remaining.");
            }
        }
        return false;
    }

    private void runAdminMenu() {
        boolean adminRunning = true;
        while (adminRunning) {
            showAdminMenu();
            String choice = scanner.nextLine().trim();
            adminRunning = handleAdminChoice(choice);
        }
    }

    private void showAdminMenu() {
        System.out.println("\n" + SEPARATOR);
        System.out.println("  ADMIN MENU");
        System.out.println(SEPARATOR);
        System.out.println("1. View active configuration");
        System.out.println("2. Update a base fare rule");
        System.out.println("3. Update a passenger discount");
        System.out.println("4. Update a daily cap");
        System.out.println("5. Return to role selection");
        System.out.print("Enter choice: ");
    }

    private boolean handleAdminChoice(String choice) {
        switch (choice) {
            case "1":
                configManager.displayConfig();
                return true;
            case "2":
                handleUpdateFare();
                return true;
            case "3":
                handleUpdateDiscount();
                return true;
            case "4":
                handleUpdateCap();
                return true;
            case "5":
                return false;
            default:
                System.out.println("Invalid option. Please enter 1–5.");
                return true;
        }
    }

    private void handleUpdateFare() {
        System.out.print("From zone (1-5): ");
        int fromZone = readInt();
        System.out.print("To zone (1-5): ");
        int toZone = readInt();
        System.out.print("Time band (PEAK / OFF_PEAK): ");
        String timeBand = scanner.nextLine().trim().toUpperCase();
        System.out.print("New fare (e.g. 3.50): ");
        BigDecimal newFare = readBigDecimal();

        boolean updated = configManager.updateBaseFare(fromZone, toZone, timeBand, newFare);
        if (updated) {
            System.out.println("Fare rule updated successfully.");
        }
    }

    private void handleUpdateDiscount() {
        System.out.print("Passenger type (ADULT/STUDENT/CHILD/SENIOR_CITIZEN): ");
        String type = scanner.nextLine().trim().toUpperCase();
        System.out.print("New discount rate (0.00–1.00, e.g. 0.25 for 25%): ");
        BigDecimal rate = readBigDecimal();

        boolean updated = configManager.updateDiscountRate(type, rate);
        if (updated) {
            System.out.println("Discount rate updated successfully.");
        }
    }

    private void handleUpdateCap() {
        System.out.print("Passenger type (ADULT/STUDENT/CHILD/SENIOR_CITIZEN): ");
        String type = scanner.nextLine().trim().toUpperCase();
        System.out.print("New daily cap (e.g. 8.00): ");
        BigDecimal cap = readBigDecimal();

        boolean updated = configManager.updateDailyCap(type, cap);
        if (updated) {
            System.out.println("Daily cap updated successfully.");
        }
    }

    private int readInt() {
        int value = 0;
        boolean valid = false;
        while (!valid) {
            try {
                value = Integer.parseInt(scanner.nextLine().trim());
                valid = true;
            } catch (NumberFormatException e) {
                System.out.print("Please enter a whole number: ");
            }
        }
        return value;
    }

    private BigDecimal readBigDecimal() {
        BigDecimal value = BigDecimal.ZERO;
        boolean valid = false;
        while (!valid) {
            try {
                value = new BigDecimal(scanner.nextLine().trim());
                valid = true;
            } catch (NumberFormatException e) {
                System.out.print("Please enter a valid decimal number (e.g. 3.50): ");
            }
        }
        return value;
    }
}