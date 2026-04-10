import java.util.Scanner;

/**
 * CityRideLite – main entry point for the CityRide Lite Part 2 application.
 * Responsibilities: initialise shared resources, display role selection,
 * and route the user to either RiderSession or AdminSession.
 */
public class CityRideLite {

    private static final String SEPARATOR = "====================";

    /**
     * Application entry point. Loads config, shows role menu, delegates to sessions.
     */
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        ConfigManager configManager = new ConfigManager();
        configManager.loadFromFile("config.json");

        showBanner();

        boolean running = true;
        while (running) {
            showRoleMenu();
            String input = scanner.nextLine().trim().toUpperCase();

            if (input.equals("RIDER")) {
                RiderSession riderSession = new RiderSession(scanner, configManager);
                riderSession.start();

            } else if (input.equals("ADMIN")) {
                AdminSession adminSession = new AdminSession(scanner, configManager);
                adminSession.start();

            } else if (input.equals("EXIT")) {
                running = false;

            } else {
                System.out.println("Invalid choice. Please enter RIDER, ADMIN, or EXIT.");
            }
        }

        System.out.println(SEPARATOR);
        System.out.println("Thank you for using CityRide Lite. Goodbye!");
        System.out.println(SEPARATOR);
        scanner.close();
    }

    /** Displays the application welcome banner. */
    private static void showBanner() {
        System.out.println(SEPARATOR);
        System.out.println("   WELCOME TO CITYRIDE LITE");
        System.out.println("   Transport Fare Companion");
        System.out.println(SEPARATOR);
    }

    /** Displays the role selection prompt. */
    private static void showRoleMenu() {
        System.out.println("\nSelect role:");
        System.out.println("  RIDER  - Manage your journeys");
        System.out.println("  ADMIN  - System configuration");
        System.out.println("  EXIT   - Quit the program");
        System.out.print("Enter role: ");
    }
}
