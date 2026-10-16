import java.util.List;
import java.util.Scanner;

// RiderSession Class
public class RiderSession {

    private static final String SEPARATOR    = "--------------------";
    private static final String WIDE_SEP     = "====================";

    private final Scanner scanner;
    private final ConfigManager configManager;
    private final FareCalculator fareCalculator;
    private final JourneyManager journeyManager;
    private final FileManager fileManager;
    private RiderProfile profile;

    public RiderSession(Scanner scanner, ConfigManager configManager) {
        this.scanner       = scanner;
        this.configManager = configManager;
        this.fareCalculator = new FareCalculator(configManager);
        this.journeyManager = new JourneyManager(fareCalculator);
        this.fileManager   = new FileManager();
    }

    public void start() {
        setupProfile();
        runRiderMenu();
    }

    private void setupProfile() {
        System.out.println(SEPARATOR);
        System.out.println("1. Load existing profile");
        System.out.println("2. Create new profile");
        System.out.println(SEPARATOR);
        System.out.print("Choose: ");
        String choice = scanner.nextLine().trim();

        if (choice.equals("1")) {
            loadExistingProfile();
        } else {
            createNewProfile();
        }
    }

    private void loadExistingProfile() {
        System.out.print("Enter profile filename (e.g. deborah_profile.json): ");
        String filePath = scanner.nextLine().trim();
        profile = fileManager.loadProfile(filePath);
        if (profile == null) {
            System.out.println("Could not load profile. Creating a new one instead.");
            createNewProfile();
        }
    }

    private void createNewProfile() {
        System.out.println(SEPARATOR);
        System.out.print("Enter your name: ");
        String name = scanner.nextLine().trim();

        String passengerType = promptPassengerType();

        System.out.print("Default payment option (e.g. CARD, CASH): ");
        String payment = scanner.nextLine().trim().toUpperCase();

        profile = new RiderProfile(name, passengerType, payment);
        System.out.println("Profile created. Welcome, " + name + "!");
    }

    private void runRiderMenu() {
        boolean running = true;
        while (running) {
            showRiderMenu();
            String choice = scanner.nextLine().trim();
            running = handleRiderChoice(choice);
        }
    }

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

    private void handleAddJourney() {
        showFareReference();

        System.out.print("Passenger name (Enter to use profile name): ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            name = profile.getName();
        }

        int fromZone    = promptZone("from");
        int toZone      = promptZone("to");
        String timeBand = promptTimeBand();

        System.out.print("Passenger type [" + profile.getPassengerType()
                + "] (Enter to use profile): ");
        String typeInput     = scanner.nextLine().trim().toUpperCase();
        String passengerType = typeInput.isEmpty() ? profile.getPassengerType() : typeInput;

        journeyManager.addJourney(name, fromZone, toZone, timeBand, passengerType);
    }

    private void showFareReference() {
        System.out.println(SEPARATOR);
        System.out.println("FARE REFERENCE TABLE");
        System.out.println(SEPARATOR);
        System.out.println("Zone Pair | PEAK   | OFF_PEAK");
        System.out.println("----------+--------+---------");
        int[] zones = {1, 2, 3, 4, 5};
        for (int from : zones) {
            for (int to : zones) {
                String peak    = "£" + configManager.getBaseFare(from, to, "PEAK");
                String offPeak = "£" + configManager.getBaseFare(from, to, "OFF_PEAK");
                System.out.printf("  %d  →  %d  | %-6s | %s%n", from, to, peak, offPeak);
            }
        }
        System.out.println(SEPARATOR);
        System.out.println("DISCOUNTS:");
        String[] types = {"ADULT", "STUDENT", "CHILD", "SENIOR_CITIZEN"};
        for (String t : types) {
            java.math.BigDecimal rate = configManager.getDiscountRate(t);
            java.math.BigDecimal cap  = configManager.getDailyCap(t);
            System.out.printf("  %-15s discount: %-4s  daily cap: £%s%n",
                    t,
                    rate.multiply(new java.math.BigDecimal("100")).stripTrailingZeros().toPlainString() + "%",
                    cap);
        }
        System.out.println(SEPARATOR);
    }

    private void handleFilterJourneys() {
        String type = promptPassengerType();
        journeyManager.filterByPassengerType(type);
    }

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

    private void handleExportJourneys() {
        if (!journeyManager.hasJourneys()) {
            System.out.println("No journeys to export.");
            return;
        }
        fileManager.exportJourneysCsv(journeyManager.getJourneys(), profile.getName());
    }

    private void handleExportSummary() {
        if (!journeyManager.hasJourneys()) {
            System.out.println("No journeys to export.");
            return;
        }
        journeyManager.showSummary();
        fileManager.exportSummaryCsv(journeyManager.getJourneys(), profile.getName());
        fileManager.exportSummaryText(buildSummaryText(), profile.getName());
    }

    private void handleImportJourneys() {
        System.out.print("Enter CSV filename to import (e.g. journeys.csv): ");
        String filePath = scanner.nextLine().trim();
        List<String[]> rows = fileManager.importJourneysCsv(filePath);

        int added = 0;
        for (String[] row : rows) {
            try {

                int from     = Integer.parseInt(row[3].trim());
                int to       = Integer.parseInt(row[4].trim());
                String band  = row[5].trim();
                String type  = row[6].trim();
                String name  = row[2].trim();
                journeyManager.addJourney(name, from, to, band, type);
                added++;
            } catch (Exception e) {

            }
        }
        System.out.println(added + " journey(s) added from import.");
    }

    private void handleSaveProfile() {
        String filePath = profile.getName().replaceAll("\\s+", "_").toLowerCase()
                + "_profile.json";
        fileManager.saveProfile(profile, filePath);
    }

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

    private String buildSummaryText() {
        return "CityRide Lite – End of Day Summary\n"
                + "Rider: " + profile.getName() + "\n"
                + "Date : " + java.time.LocalDate.now() + "\n"
                + "See CSV file for full journey breakdown.\n";
    }
}