import java.util.List;
import java.util.Scanner;

/**
 * RiderSession – manages the full rider menu loop for a single session.
 * Responsibilities: prompt for and load a rider profile, present the rider
 * menu, and delegate each choice to the appropriate manager or handler.
 * Keeps CityRideLite.main() clean by encapsulating all rider-side logic.
 */
public class RiderSession {

    private static final String SEPARATOR    = "--------------------";
    private static final String WIDE_SEP     = "====================";

    private final Scanner scanner;
    private final ConfigManager configManager;
    private final FareCalculator fareCalculator;
    private final JourneyManager journeyManager;
    private final FileManager fileManager;
    private RiderProfile profile;

    /**
     * Constructs a RiderSession with all required dependencies.
     *
     * @param scanner       shared Scanner for user input
     * @param configManager the active system configuration
     */
    public RiderSession(Scanner scanner, ConfigManager configManager) {
        this.scanner       = scanner;
        this.configManager = configManager;
        this.fareCalculator = new FareCalculator(configManager);
        this.journeyManager = new JourneyManager(fareCalculator);
        this.fileManager   = new FileManager();
    }

    /**
     * Starts the rider session: loads or creates a profile, then runs the menu.
     */
    public void start() {
        setupProfile();
        runRiderMenu();
    }

    // ── Profile setup ─────────────────────────────────────────────

    /** Asks the rider whether to load an existing profile or create a new one. */
    private void setupProfile() {
        System.out.println(SEPARATOR);
        System.out.println("1. Load existing profile");
        System.out.println("2. Create new profile");
        System.out.print("Choose: ");
        String choice = scanner.nextLine().trim();

        if (choice.equals("1")) {
            loadExistingProfile();
        } else {
            createNewProfile();
        }
    }

    /** Prompts for a filename and loads the profile from JSON. */
    private void loadExistingProfile() {
        System.out.print("Enter profile filename (e.g. deborah_profile.json): ");
        String filePath = scanner.nextLine().trim();
        profile = fileManager.loadProfile(filePath);
        if (profile == null) {
            System.out.println("Could not load profile. Creating a new one instead.");
            createNewProfile();
        }
    }

    /** Prompts the rider to enter their details and creates a new profile. */
    private void createNewProfile() {
        System.out.print("Enter your name: ");
        String name = scanner.nextLine().trim();

        String passengerType = promptPassengerType();

        System.out.print("Default payment option (e.g. CARD, CASH): ");
        String payment = scanner.nextLine().trim().toUpperCase();

        profile = new RiderProfile(name, passengerType, payment);
        System.out.println("Profile created. Welcome, " + name + "!");
    }

    // ── Rider menu loop ───────────────────────────────────────────

    /** Displays and processes the rider menu until the rider chooses to exit. */
    private void runRiderMenu() {
        boolean running = true;
        while (running) {
            showRiderMenu();
            String choice = scanner.nextLine().trim();
            running = handleRiderChoice(choice);
        }
    }

    /** Prints the rider menu. */
    private void showRiderMenu() {
        System.out.println("\n" + WIDE_SEP);
        System.out.println("  CITYRIDE LITE – " + profile.getName());
        System.out.println(WIDE_SEP);
        System.out.println("1.  View profile");
        System.out.println("2.  Add journey");
        System.out.println("3.  View all journeys");
        System.out.println("4.  Filter journeys by type");
        System.out.println("5.  Remove journey");
        System.out.println("6.  View running totals");
        System.out.println("7.  End-of-day summary");
        System.out.println("8.  Export journeys to CSV");
        System.out.println("9.  Export summary (CSV + text)");
        System.out.println("10. Import journeys from CSV");
        System.out.println("11. Save profile");
        System.out.println("12. Save and exit");
        System.out.print("Enter choice: ");
    }

    /**
     * Routes the rider's menu choice to the appropriate handler.
     *
     * @param choice the input string from the rider
     * @return false if the rider chose to exit; true to keep the menu running
     */
    private boolean handleRiderChoice(String choice) {
        switch (choice) {
            case "1":  profile.displayProfile();           return true;
            case "2":  handleAddJourney();                 return true;
            case "3":  journeyManager.listJourneys();      return true;
            case "4":  handleFilterJourneys();             return true;
            case "5":  handleRemoveJourney();              return true;
            case "6":  journeyManager.showRunningTotals(); return true;
            case "7":  journeyManager.showSummary();       return true;
            case "8":  handleExportJourneys();             return true;
            case "9":  handleExportSummary();              return true;
            case "10": handleImportJourneys();             return true;
            case "11": handleSaveProfile();                return true;
            case "12": handleSaveAndExit();                return false;
            default:
                System.out.println("Invalid option. Please enter 1–12.");
                return true;
        }
    }

    // ── Journey handlers ──────────────────────────────────────────

    /** Prompts for all journey fields and adds the journey via JourneyManager. */
    private void handleAddJourney() {
        System.out.print("Passenger name (Enter to use profile name): ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            name = profile.getName();
        }

        int fromZone = promptZone("from");
        int toZone   = promptZone("to");
        String timeBand = promptTimeBand();

        System.out.print("Passenger type [" + profile.getPassengerType()
                + "] (Enter to use profile): ");
        String typeInput = scanner.nextLine().trim().toUpperCase();
        String passengerType = typeInput.isEmpty() ? profile.getPassengerType() : typeInput;

        journeyManager.addJourney(name, fromZone, toZone, timeBand, passengerType);
    }

    /** Prompts for a passenger type and filters the journey list. */
    private void handleFilterJourneys() {
        String type = promptPassengerType();
        journeyManager.filterByPassengerType(type);
    }

    /** Prompts for a journey ID and removes that journey. */
    private void handleRemoveJourney() {
        if (!journeyManager.hasJourneys()) {
            System.out.println(SEPARATOR);
            System.out.println("No journeys to remove.");
            System.out.println(SEPARATOR);
            return;
        }
        journeyManager.listJourneys();
        System.out.print("Enter journey ID to remove: ");
        int id = readInt();
        journeyManager.removeJourney(id);
    }

    // ── File handlers ─────────────────────────────────────────────

    /** Exports all journeys to a CSV file. */
    private void handleExportJourneys() {
        if (!journeyManager.hasJourneys()) {
            System.out.println("No journeys to export.");
            return;
        }
        fileManager.exportJourneysCsv(journeyManager.getJourneys(), profile.getName());
    }

    /** Exports the summary as both a CSV and a text file. */
    private void handleExportSummary() {
        if (!journeyManager.hasJourneys()) {
            System.out.println("No journeys to export.");
            return;
        }
        journeyManager.showSummary();
        fileManager.exportSummaryCsv(journeyManager.getJourneys(), profile.getName());
        fileManager.exportSummaryText(buildSummaryText(), profile.getName());
    }

    /** Imports journeys from a CSV file, creating Journey objects for each valid row. */
    private void handleImportJourneys() {
        System.out.print("Enter CSV filename to import (e.g. journeys.csv): ");
        String filePath = scanner.nextLine().trim();
        List<String[]> rows = fileManager.importJourneysCsv(filePath);

        int added = 0;
        for (String[] row : rows) {
            try {
                // CSV column order: id, date, name, from, to, band, type, base, discount, charged
                int from     = Integer.parseInt(row[3].trim());
                int to       = Integer.parseInt(row[4].trim());
                String band  = row[5].trim();
                String type  = row[6].trim();
                String name  = row[2].trim();
                journeyManager.addJourney(name, from, to, band, type);
                added++;
            } catch (Exception e) {
                // Row could not be processed — already counted as rejected by FileManager
            }
        }
        System.out.println(added + " journey(s) added from import.");
    }

    /** Saves the current profile to a JSON file. */
    private void handleSaveProfile() {
        String filePath = profile.getName().replaceAll("\\s+", "_").toLowerCase()
                + "_profile.json";
        fileManager.saveProfile(profile, filePath);
    }

    /** Offers to save profile and journeys, then ends the session. */
    private void handleSaveAndExit() {
        System.out.print("Save profile and journeys before exiting? (Y/N): ");
        String confirm = scanner.nextLine().trim().toUpperCase();
        if (confirm.equals("Y")) {
            handleSaveProfile();
            handleExportJourneys();
        }
        System.out.println(SEPARATOR);
        System.out.println("Session ended. Goodbye, " + profile.getName() + "!");
        System.out.println(SEPARATOR);
    }

    // ── Input helpers ─────────────────────────────────────────────

    /**
     * Prompts for a zone number, looping until a valid value (1–5) is entered.
     *
     * @param label "from" or "to" for the prompt text
     * @return the validated zone number
     */
    private int promptZone(String label) {
        int zone = 0;
        boolean valid = false;
        while (!valid) {
            System.out.print("Enter " + label + " zone (1-5): ");
            zone = readInt();
            if (zone >= 1 && zone <= 5) {
                valid = true;
            } else {
                System.out.println("Zone must be between 1 and 5.");
            }
        }
        return zone;
    }

    /**
     * Prompts for a time band, looping until PEAK or OFF_PEAK is entered.
     *
     * @return the validated time band string
     */
    private String promptTimeBand() {
        String band = "";
        boolean valid = false;
        while (!valid) {
            System.out.print("Time band (PEAK / OFF_PEAK): ");
            band = scanner.nextLine().trim().toUpperCase();
            if (band.equals("PEAK") || band.equals("OFF_PEAK")) {
                valid = true;
            } else {
                System.out.println("Please enter PEAK or OFF_PEAK.");
            }
        }
        return band;
    }

    /**
     * Prompts for a passenger type, looping until a valid value is entered.
     *
     * @return the validated passenger type string
     */
    private String promptPassengerType() {
        String type = "";
        boolean valid = false;
        while (!valid) {
            System.out.println("Passenger types: ADULT | STUDENT | CHILD | SENIOR_CITIZEN");
            System.out.print("Enter type: ");
            type = scanner.nextLine().trim().toUpperCase();
            if (type.equals("ADULT") || type.equals("STUDENT")
                    || type.equals("CHILD") || type.equals("SENIOR_CITIZEN")) {
                valid = true;
            } else {
                System.out.println("Invalid type. Please try again.");
            }
        }
        return type;
    }

    /**
     * Reads an integer, re-prompting on invalid input.
     *
     * @return the integer value entered
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
     * Builds a plain-text summary string for file export.
     *
     * @return formatted summary text
     */
    private String buildSummaryText() {
        return "CityRide Lite – End of Day Summary\n"
                + "Rider: " + profile.getName() + "\n"
                + "Date : " + java.time.LocalDate.now() + "\n"
                + "See CSV file for full journey breakdown.\n";
    }
}
