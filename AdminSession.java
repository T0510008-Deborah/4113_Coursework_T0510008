import java.math.BigDecimal;
import java.util.Scanner;

/**
 * AdminSession – manages the password-protected admin menu.
 * Responsibilities: authenticate the admin, display the admin menu,
 * and delegate config updates to ConfigManager with full validation.
 */
public class AdminSession {

    private static final String ADMIN_PASSWORD = "CityRide2026";
    private static final int MAX_ATTEMPTS = 3;
    private static final String SEPARATOR = "--------------------";

    private final Scanner scanner;
    private final ConfigManager configManager;

    /**
     * Constructs an AdminSession.
     *
     * @param scanner       shared Scanner for user input
     * @param configManager the active system configuration
     */
    public AdminSession(Scanner scanner, ConfigManager configManager) {
        this.scanner = scanner;
        this.configManager = configManager;
    }

    /**
     * Starts the admin session by prompting for a password.
     * Allows up to MAX_ATTEMPTS attempts before denying access.
     */
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

    // ── Login ─────────────────────────────────────────────────────

    /**
     * Prompts for the admin password up to MAX_ATTEMPTS times.
     *
     * @return true if the correct password was entered, false otherwise
     */
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

    // ── Admin menu loop ───────────────────────────────────────────

    /** Displays and processes the admin menu until the admin chooses to exit. */
    private void runAdminMenu() {
        boolean adminRunning = true;
        while (adminRunning) {
            showAdminMenu();
            String choice = scanner.nextLine().trim();
            adminRunning = handleAdminChoice(choice);
        }
    }

    /** Prints the admin menu options. */
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

    /**
     * Routes the admin's menu choice to the appropriate handler.
     *
     * @param choice the input string from the admin
     * @return false if the admin chose to exit; true to keep the menu running
     */
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

    // ── Update handlers ───────────────────────────────────────────

    /** Prompts for and applies an update to a base fare rule. */
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

    /** Prompts for and applies an update to a passenger discount rate. */
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

    /** Prompts for and applies an update to a daily spending cap. */
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

    // ── Input helpers ─────────────────────────────────────────────

    /**
     * Reads an integer from the scanner, re-prompting on invalid input.
     *
     * @return the integer entered by the user
     */
    private int readInt() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Please enter a whole number: ");
            }
        }
    }

    /**
     * Reads a BigDecimal from the scanner, re-prompting on invalid input.
     *
     * @return the BigDecimal entered by the user
     */
    private BigDecimal readBigDecimal() {
        while (true) {
            try {
                return new BigDecimal(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Please enter a valid decimal number (e.g. 3.50): ");
            }
        }
    }
}
