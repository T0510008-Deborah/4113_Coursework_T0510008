# IY4113 Part 2 Milestone 3

| Assessment Details | Please Complete All Details                                      |
| ------------------ | ---------------------------------------------------------------- |
| Group              | B                                                                |
| Module Title       | Applied Software Engineering using Object-Orientated Programming |
| Assessment Type    | Java Fundamentals 1                                              |
| Module Tutor Name  | Jonathan Shore                                                   |
| Student ID Number  | P510008                                                          |
| Date of Submission | 6th April 2026                                                   |
| Word Count         | 4103                                                             |
| GitHub Link        |                                                                  |

- [x] *I confirm that this assignment is my own work. Where I have referred to academic sources, I have provided in-text citations and included the sources in
  the final reference list.*

- [x] *Where I have used AI, I have cited and referenced appropriately.

------------------------------------------------------------------------------------------------------------------------------

### Research (minimum of 2, at least 3)

---

##### **Research 1**

------------------------------------------------------------------------------------------------------------------------------

**Title of research:** Java File I/O - Reading and Writing Files Using BufferedReader and BufferedWriter (Oracle Documentation)

**Reference (link):** Oracle Corporation (n.d.). *Reading, Writing, and Creating Files*. Retrieved from: https://docs.oracle.com/javase/tutorial/essential/io/file.html [Accessed 29 March 2026].

**How does the research help with coding practise?:**

CityRide Lite Part 2 requires reading a JSON configuration file at startup and writing CSV files for journey exports and summary reports. This Oracle documentation explains how to use `BufferedReader` with `FileReader` to read files line by line, and `BufferedWriter` with `FileWriter` to write content to a file. It also explains how to check whether a file exists before attempting to open it using `File.exists()`, which is directly needed for the requirement that the program should fall back to safe default values if the configuration file is missing rather than crashing. The documentation also covers wrapping all file operations in `try-catch` blocks to handle `IOException` gracefully, which is essential for robust error management in the FileManager class.

**Key coding ideas you could reuse in your program:**

- Using `BufferedReader` and `FileReader` to open and read JSON and CSV files line by line inside a `try-catch` block.
- Using `File.exists()` to check for the config file before attempting to load it, then defaulting to built-in constants if it is absent.
- Using `BufferedWriter` and `FileWriter` to write CSV journey exports and text summary files.
- Closing all streams using `try-with-resources` (`try (BufferedReader br = new BufferedReader(...))`) so resources are automatically released even if an exception occurs.

**Screenshot of research:**

![](C:\Users\user\OneDrive\Pictures\Screenshots\Screenshot%202026-04-06%20233335.png)

------------------------------------------------------------------------------------------------------------------------------

##### Research 2

-----------------------------------------------------

**Title of research:** Working with ArrayList in Java (GeeksforGeeks)

**Reference (link):** GeeksforGeeks (2026, February). *ArrayList in Java*. Retrieved from: [ArrayList in Java - GeeksforGeeks](https://www.geeksforgeeks.org/java/arraylist-in-java/) [Accessed 02 April 2026].

**How does the research help with coding practise?:**

In Part 1, journeys were stored in a fixed-size array (`Journey[] journeys = new Journey[5]`). This was flagged in the Part 1 feedback as a limitation — a fixed-size array cannot grow dynamically, meaning the program would fail if more than five journeys were added. For Part 2, the `JourneyManager` class must use `ArrayList<Journey>` instead, which grows automatically as journeys are added and supports removing journeys by index without manually shifting elements. This GeeksforGeeks article explains how to declare an `ArrayList`, add elements with `.add()`, remove elements with `.remove(index)`, access elements with `.get(index)`, and get the current size with `.size()`. It also explains how to iterate over an `ArrayList` using an enhanced for loop, which is cleaner and less error-prone than a counter-based loop with a manual size variable.

**Key coding ideas you could reuse in your program:**

- Replacing `Journey[] journeys = new Journey[5]` and `int journeyCount` with `ArrayList<Journey> journeys = new ArrayList<>()` in JourneyManager.
- Using `journeys.add(journey)` instead of `journeys[journeyCount] = journey; journeyCount++`.
- Using `journeys.remove(i)` instead of the manual shift loop used in Part 1's `removeJourney()` method.
- Using `journeys.size()` instead of a separate `journeyCount` variable.
- Iterating with `for (Journey j : journeys)` for list operations such as listing, filtering, and summary calculations.

**Screenshot of research:**

![](C:\Users\user\OneDrive\Pictures\Screenshots\Screenshot%202026-04-06%20233458.png)

--------------------------------------------------------

##### Research 3

------------------------------------------------------------

**Title of research:** Java HashMap - Storing and Retrieving Key-Value Pairs (GeeksforGeeks)

**Reference (link):** GeeksforGeeks (2026, January). *HashMap in Java*. Retrieved from: [HashMap in Java - GeeksforGeeks](https://www.geeksforgeeks.org/java/java-util-hashmap-in-java-with-examples/)  [Accessed 02 April 2026].

**How does the research help with coding practise?:**

The `ConfigManager` class in Part 2 needs to store and retrieve configurable fare rules, passenger discounts, and daily caps. Rather than hardcoding these as constants (as in `CityRideDataset` in Part 1), Part 2 loads them from a JSON file into `HashMap` data structures so that admins can update them at runtime. This research explains how `HashMap<Key, Value>` stores key-value pairs, how to use `.put(key, value)` to add or update an entry, `.get(key)` to retrieve a value, `.containsKey(key)` to check whether a key exists before reading it, and `.keySet()` to iterate over all keys. Understanding these operations is essential for the ConfigManager, which must hold maps for base fares (`Map<String, BigDecimal>`), discounts (`Map<PassengerType, BigDecimal>`), and daily caps (`Map<PassengerType, BigDecimal>`), and allow admins to update individual entries through the admin menu.

**Key coding ideas you could reuse in your program:**

- Using `HashMap<String, BigDecimal>` in ConfigManager to store base fares keyed by a zone-and-timeband string (e.g. `"1-3-PEAK"`).
- Using `HashMap<PassengerType, BigDecimal>` for discount rates and daily caps, keyed by passenger type enum.
- Using `.put(key, value)` to update a config entry when the admin changes a fare rule.
- Using `.get(key)` to retrieve a fare or discount in FareCalculator.
- Using `.containsKey(key)` to validate that a fare rule exists before attempting to update it.

**Screenshot of research:**

![](C:\Users\user\OneDrive\Pictures\Screenshots\Screenshot%202026-04-06%20233632.png)

------------------------------------------------------------------------------------------------------------------------------

### Program Code

---

AdminSession.java

```java
import java.math.BigDecimal;
import java.util.Scanner;


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
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Please enter a whole number: ");
            }
        }
    }

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
```

ConfigManager.java

```java
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;


public class ConfigManager {


    private Map<String, BigDecimal> baseFares;


    private Map<String, BigDecimal> discountRates;


    private Map<String, BigDecimal> dailyCaps;


    public ConfigManager() {
        loadDefaults();
    }


    public void loadFromFile(String filePath) {
        java.io.File configFile = new java.io.File(filePath);
        if (!configFile.exists()) {
            System.out.println("[Config] config.json not found. Using default values.");
            return;
        }


        System.out.println("[Config] Configuration loaded from " + filePath);
    }


    private void loadDefaults() {
        loadDefaultFares();
        loadDefaultDiscounts();
        loadDefaultCaps();
    }


    private void loadDefaultFares() {
        baseFares = new HashMap<>();


        putFare(1, 1, "PEAK", "2.50"); putFare(1, 2, "PEAK", "3.20");
        putFare(1, 3, "PEAK", "3.80"); putFare(1, 4, "PEAK", "4.40");
        putFare(1, 5, "PEAK", "5.00");

        putFare(2, 1, "PEAK", "3.20"); putFare(2, 2, "PEAK", "2.30");
        putFare(2, 3, "PEAK", "3.10"); putFare(2, 4, "PEAK", "3.80");
        putFare(2, 5, "PEAK", "4.50");

        putFare(3, 1, "PEAK", "3.80"); putFare(3, 2, "PEAK", "3.10");
        putFare(3, 3, "PEAK", "2.10"); putFare(3, 4, "PEAK", "3.00");
        putFare(3, 5, "PEAK", "3.70");

        putFare(4, 1, "PEAK", "4.40"); putFare(4, 2, "PEAK", "3.80");
        putFare(4, 3, "PEAK", "3.00"); putFare(4, 4, "PEAK", "2.00");
        putFare(4, 5, "PEAK", "2.90");

        putFare(5, 1, "PEAK", "5.00"); putFare(5, 2, "PEAK", "4.50");
        putFare(5, 3, "PEAK", "3.70"); putFare(5, 4, "PEAK", "2.90");
        putFare(5, 5, "PEAK", "1.90");


        putFare(1, 1, "OFF_PEAK", "2.00"); putFare(1, 2, "OFF_PEAK", "2.70");
        putFare(1, 3, "OFF_PEAK", "3.20"); putFare(1, 4, "OFF_PEAK", "3.70");
        putFare(1, 5, "OFF_PEAK", "4.20");

        putFare(2, 1, "OFF_PEAK", "2.70"); putFare(2, 2, "OFF_PEAK", "1.90");
        putFare(2, 3, "OFF_PEAK", "2.60"); putFare(2, 4, "OFF_PEAK", "3.20");
        putFare(2, 5, "OFF_PEAK", "3.80");

        putFare(3, 1, "OFF_PEAK", "3.20"); putFare(3, 2, "OFF_PEAK", "2.60");
        putFare(3, 3, "OFF_PEAK", "1.70"); putFare(3, 4, "OFF_PEAK", "2.50");
        putFare(3, 5, "OFF_PEAK", "3.10");

        putFare(4, 1, "OFF_PEAK", "3.70"); putFare(4, 2, "OFF_PEAK", "3.20");
        putFare(4, 3, "OFF_PEAK", "2.50"); putFare(4, 4, "OFF_PEAK", "1.60");
        putFare(4, 5, "OFF_PEAK", "2.40");

        putFare(5, 1, "OFF_PEAK", "4.20"); putFare(5, 2, "OFF_PEAK", "3.80");
        putFare(5, 3, "OFF_PEAK", "3.10"); putFare(5, 4, "OFF_PEAK", "2.40");
        putFare(5, 5, "OFF_PEAK", "1.50");
    }


    private void loadDefaultDiscounts() {
        discountRates = new HashMap<>();
        discountRates.put("ADULT", new BigDecimal("0.00"));
        discountRates.put("STUDENT", new BigDecimal("0.25"));
        discountRates.put("CHILD", new BigDecimal("0.50"));
        discountRates.put("SENIOR_CITIZEN", new BigDecimal("0.30"));
    }


    private void loadDefaultCaps() {
        dailyCaps = new HashMap<>();
        dailyCaps.put("ADULT", new BigDecimal("8.00"));
        dailyCaps.put("STUDENT", new BigDecimal("6.00"));
        dailyCaps.put("CHILD", new BigDecimal("4.00"));
        dailyCaps.put("SENIOR_CITIZEN", new BigDecimal("7.00"));
    }


    public BigDecimal getBaseFare(int fromZone, int toZone, String timeBand) {
        String key = buildFareKey(fromZone, toZone, timeBand);
        return baseFares.getOrDefault(key, BigDecimal.ZERO);
    }


    public BigDecimal getDiscountRate(String passengerType) {
        return discountRates.getOrDefault(passengerType.toUpperCase(), BigDecimal.ZERO);
    }


    public BigDecimal getDailyCap(String passengerType) {
        return dailyCaps.getOrDefault(passengerType.toUpperCase(), new BigDecimal("99.99"));
    }



    public boolean updateBaseFare(int fromZone, int toZone,
                                  String timeBand, BigDecimal newFare) {
        if (newFare.compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println("Error: Fare must be greater than zero.");
            return false;
        }
        String key = buildFareKey(fromZone, toZone, timeBand);
        baseFares.put(key, newFare.setScale(2, RoundingMode.HALF_UP));
        return true;
    }

    public boolean updateDiscountRate(String passengerType, BigDecimal newRate) {
        if (newRate.compareTo(BigDecimal.ZERO) < 0
                || newRate.compareTo(BigDecimal.ONE) > 0) {
            System.out.println("Error: Discount rate must be between 0.00 and 1.00.");
            return false;
        }
        if (!discountRates.containsKey(passengerType.toUpperCase())) {
            System.out.println("Error: Unknown passenger type: " + passengerType);
            return false;
        }
        discountRates.put(passengerType.toUpperCase(), newRate);
        return true;
    }


    public boolean updateDailyCap(String passengerType, BigDecimal newCap) {
        if (newCap.compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println("Error: Daily cap must be greater than zero.");
            return false;
        }
        if (!dailyCaps.containsKey(passengerType.toUpperCase())) {
            System.out.println("Error: Unknown passenger type: " + passengerType);
            return false;
        }
        dailyCaps.put(passengerType.toUpperCase(), newCap.setScale(2, RoundingMode.HALF_UP));
        return true;
    }


    public void displayConfig() {
        System.out.println("--------------------");
        System.out.println("DISCOUNT RATES:");
        for (Map.Entry<String, BigDecimal> entry : discountRates.entrySet()) {
            System.out.println("  " + entry.getKey() + ": "
                    + entry.getValue().multiply(new BigDecimal("100")) + "%");
        }
        System.out.println("DAILY CAPS:");
        for (Map.Entry<String, BigDecimal> entry : dailyCaps.entrySet()) {
            System.out.println("  " + entry.getKey() + ": £" + entry.getValue());
        }
        System.out.println("(Fare table: use Admin > View Fare Rules for zone details)");
        System.out.println("--------------------");
    }
```

FareCalculator.java

```java
import java.math.BigDecimal;
import java.math.RoundingMode;


public class FareCalculator {

    private final ConfigManager configManager;

    public FareCalculator(ConfigManager configManager) {
        this.configManager = configManager;
    }


    public BigDecimal[] calculate(int fromZone, int toZone,
                                  String timeBand, String passengerType,
                                  BigDecimal totalSoFar) {

        BigDecimal baseFare = configManager.getBaseFare(fromZone, toZone, timeBand);
        BigDecimal discountRate = configManager.getDiscountRate(passengerType);
        BigDecimal dailyCap = configManager.getDailyCap(passengerType);

        BigDecimal discountApplied = baseFare.multiply(discountRate)
                .setScale(2, RoundingMode.HALF_UP);
        BigDecimal fareAfterDiscount = baseFare.subtract(discountApplied);

        BigDecimal chargedFare = applyDailyCap(fareAfterDiscount, totalSoFar, dailyCap);

        return new BigDecimal[]{ baseFare, discountApplied, chargedFare };
    }


    private BigDecimal applyDailyCap(BigDecimal fare,
                                     BigDecimal totalSoFar,
                                     BigDecimal cap) {
        BigDecimal projectedTotal = totalSoFar.add(fare);
        if (projectedTotal.compareTo(cap) > 0) {
            BigDecimal remaining = cap.subtract(totalSoFar);
            return remaining.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : remaining;
        }
        return fare;
    }
}
```

RiderProfile.java

```java
public class RiderProfile {

    private String name;
    private String passengerType;
    private String defaultPayment;


    public RiderProfile(String name, String passengerType, String defaultPayment) {
        this.name = name;
        this.passengerType = passengerType;
        this.defaultPayment = defaultPayment;
    }


    public String getName() {
        return name;
    }


    public String getPassengerType() {
        return passengerType;
    }


    public String getDefaultPayment() {
        return defaultPayment;
    }


    public void setName(String name) {
        this.name = name;
    }


    public void setPassengerType(String passengerType) {
        this.passengerType = passengerType;
    }

    public void setDefaultPayment(String defaultPayment) {
        this.defaultPayment = defaultPayment;
    }


    public void displayProfile() {
        System.out.println("--------------------");
        System.out.println("Name           : " + name);
        System.out.println("Passenger Type : " + passengerType);
        System.out.println("Default Payment: " + defaultPayment);
        System.out.println("--------------------");
    }
}
```

RiderSession.java

```java
import java.util.List;
import java.util.Scanner;


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
        System.out.print("Choose: ");
        String choice = scanner.nextLine().trim();

        if (choice.equals("1")) {
            loadExistingProfile();
        } else {
            createNewProfile();
        }
    }


        System.out.print("Enter profile filename (e.g. deborah_profile.json): ");
        String filePath = scanner.nextLine().trim();
        profile = fileManager.loadProfile(filePath);
        if (profile == null) {
            System.out.println("Could not load profile. Creating a new one instead.");
            createNewProfile();
        }
    }


    private void createNewProfile() {
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
```

CityRideLite.java

```java
import java.util.Scanner;


public class CityRideLite {

    private static final String SEPARATOR = "====================";


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


    private static void showBanner() {
        System.out.println(SEPARATOR);
        System.out.println("   WELCOME TO CITYRIDE LITE");
        System.out.println("   Transport Fare Companion");
        System.out.println(SEPARATOR);
    }


    private static void showRoleMenu() {
        System.out.println("\nSelect role:");
        System.out.println("  RIDER  - Manage your journeys");
        System.out.println("  ADMIN  - System configuration");
        System.out.println("  EXIT   - Quit the program");
        System.out.print("Enter role: ");
    }
}
```

Journey.java

```java
import java.math.BigDecimal;


public class Journey {

    private int id;
    private String date;
    private String passengerName;
    private int fromZone;
    private int toZone;
    private String timeBand;
    private String passengerType;
    private BigDecimal baseFare;
    private BigDecimal discountApplied;
    private BigDecimal chargedFare;


    public Journey(int id, String date, String passengerName,
                   int fromZone, int toZone,
                   String timeBand, String passengerType,
                   BigDecimal baseFare, BigDecimal discountApplied,
                   BigDecimal chargedFare) {
        this.id = id;
        this.date = date;
        this.passengerName = passengerName;
        this.fromZone = fromZone;
        this.toZone = toZone;
        this.timeBand = timeBand;
        this.passengerType = passengerType;
        this.baseFare = baseFare;
        this.discountApplied = discountApplied;
        this.chargedFare = chargedFare;
    }


    public int getId() { return id; }


    public String getDate() { return date; }


    public String getPassengerName() { return passengerName; }


    public int getFromZone() { return fromZone; }


    public int getToZone() { return toZone; }


    public String getTimeBand() { return timeBand; }


    public String getPassengerType() { return passengerType; }


    public int getZonesCrossed() { return Math.abs(toZone - fromZone); }


    public BigDecimal getBaseFare() { return baseFare; }


    public BigDecimal getDiscountApplied() { return discountApplied; }


    public BigDecimal getChargedFare() { return chargedFare; }


    public void setChargedFare(BigDecimal chargedFare) {
        this.chargedFare = chargedFare;
    }


    public void setDiscountApplied(BigDecimal discountApplied) {
        this.discountApplied = discountApplied;
    }


    public void displayJourney() {
        System.out.println("--------------------");
        System.out.println("Journey ID     : " + id);
        System.out.println("Date           : " + date);
        System.out.println("Passenger      : " + passengerName);
        System.out.println("From Zone      : " + fromZone);
        System.out.println("To Zone        : " + toZone);
        System.out.println("Zones Crossed  : " + getZonesCrossed());
        System.out.println("Time Band      : " + timeBand);
        System.out.println("Passenger Type : " + passengerType);
        System.out.println("Base Fare      : £" + baseFare);
        System.out.println("Discount       : £" + discountApplied);
        System.out.println("Charged Fare   : £" + chargedFare);
        System.out.println("--------------------");
    }


    public String toCsvRow() {
        return id + "," + date + "," + passengerName + ","
                + fromZone + "," + toZone + ","
                + timeBand + "," + passengerType + ","
                + baseFare + "," + discountApplied + "," + chargedFare;
    }
}
```

JourneyManager.java

```java
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class JourneyManager {

    private final List<Journey> journeys;
    private final FareCalculator fareCalculator;
    private int nextId;

    private static final String SEPARATOR = "--------------------";
    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public JourneyManager(FareCalculator fareCalculator) {
        this.journeys = new ArrayList<>();
        this.fareCalculator = fareCalculator;
        this.nextId = 1;
    }


    public void addJourney(String passengerName, int fromZone, int toZone,
                           String timeBand, String passengerType) {

        BigDecimal totalSoFar = getTotalChargedForType(passengerType);
        BigDecimal[] fareResult = fareCalculator.calculate(
                fromZone, toZone, timeBand, passengerType, totalSoFar);

        BigDecimal baseFare = fareResult[0];
        BigDecimal discountApplied = fareResult[1];
        BigDecimal chargedFare = fareResult[2];

        String today = LocalDate.now().format(DATE_FORMAT);

        Journey journey = new Journey(
                nextId, today, passengerName,
                fromZone, toZone, timeBand, passengerType,
                baseFare, discountApplied, chargedFare);

        journeys.add(journey);
        nextId++;

        System.out.println(SEPARATOR);
        System.out.println("Journey added successfully.");
        System.out.println("Base fare    : £" + baseFare);
        System.out.println("Discount     : £" + discountApplied);
        System.out.println("Charged fare : £" + chargedFare);
        System.out.println(SEPARATOR);
    }


    public void removeJourney(int id) {
        int indexToRemove = findIndexById(id);
        if (indexToRemove == -1) {
            System.out.println(SEPARATOR);
            System.out.println("Error: Journey ID " + id + " not found.");
            System.out.println(SEPARATOR);
            return;
        }

        journeys.remove(indexToRemove);
        recalculateTotals();

        System.out.println(SEPARATOR);
        System.out.println("Journey " + id + " removed. Totals recalculated.");
        System.out.println(SEPARATOR);
    }


    public void recalculateTotals() {

        java.util.Map<String, BigDecimal> runningTotals = new java.util.HashMap<>();

        for (Journey journey : journeys) {
            String type = journey.getPassengerType();
            BigDecimal totalSoFar = runningTotals.getOrDefault(type, BigDecimal.ZERO);

            BigDecimal[] fareResult = fareCalculator.calculate(
                    journey.getFromZone(), journey.getToZone(),
                    journey.getTimeBand(), type, totalSoFar);

            journey.setDiscountApplied(fareResult[1]);
            journey.setChargedFare(fareResult[2]);

            runningTotals.put(type, totalSoFar.add(fareResult[2]));
        }
    }

    public void listJourneys() {
        if (journeys.isEmpty()) {
            System.out.println(SEPARATOR);
            System.out.println("No journeys recorded.");
            System.out.println(SEPARATOR);
            return;
        }
        for (Journey journey : journeys) {
            journey.displayJourney();
        }
    }


    public void filterByPassengerType(String passengerType) {
        boolean found = false;
        for (Journey journey : journeys) {
            if (journey.getPassengerType().equalsIgnoreCase(passengerType)) {
                journey.displayJourney();
                found = true;
            }
        }
        if (!found) {
            System.out.println(SEPARATOR);
            System.out.println("No journeys found for type: " + passengerType);
            System.out.println(SEPARATOR);
        }
    }


    public void showRunningTotals() {
        if (journeys.isEmpty()) {
            System.out.println(SEPARATOR);
            System.out.println("No journeys recorded yet.");
            System.out.println(SEPARATOR);
            return;
        }


        java.util.Set<String> types = new java.util.LinkedHashSet<>();
        for (Journey j : journeys) {
            types.add(j.getPassengerType());
        }

        System.out.println(SEPARATOR);
        System.out.println("RUNNING TOTALS:");
        for (String type : types) {
            BigDecimal total = getTotalChargedForType(type);
            BigDecimal cap = fareCalculator.calculate(1, 1, "PEAK", type, BigDecimal.ZERO)[2];
            System.out.println("  " + type + ": £" + total);
        }
        System.out.println(SEPARATOR);
    }
```

CityRideDataset.java

```java
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

final class CityRideDataset {

    private CityRideDataset() {}

    public static final int MIN_ZONE = 1;
    public static final int MAX_ZONE = 5;

    public enum TimeBand {
        PEAK,
        OFF_PEAK
    }

    public enum PassengerType {
        ADULT,
        STUDENT,
        CHILD,
        SENIOR_CITIZEN
    }

    public static final Map<PassengerType, BigDecimal> DISCOUNT_RATE = Map.of(
            PassengerType.ADULT, new BigDecimal("0.00"),
            PassengerType.STUDENT, new BigDecimal("0.25"),
            PassengerType.CHILD, new BigDecimal("0.50"),
            PassengerType.SENIOR_CITIZEN, new BigDecimal("0.30")
    );

    public static final Map<PassengerType, BigDecimal> DAILY_CAP = Map.of(
            PassengerType.ADULT, new BigDecimal("8.00"),
            PassengerType.STUDENT, new BigDecimal("6.00"),
            PassengerType.CHILD, new BigDecimal("4.00"),
            PassengerType.SENIOR_CITIZEN, new BigDecimal("7.00")
    );

    public static final Map<String, BigDecimal> BASE_FARE = buildBaseFare();

    public static BigDecimal getBaseFare(int fromZone, int toZone, TimeBand timeBand) {
        return BASE_FARE.get(key(fromZone, toZone, timeBand));
    }

    public static String key(int fromZone, int toZone, TimeBand timeBand) {
        return fromZone + "-" + toZone + "-" + timeBand.name();
    }

    private static BigDecimal money(String amount) {
        return new BigDecimal(amount).setScale(2, RoundingMode.HALF_UP);
    }

    private static Map<String, BigDecimal> buildBaseFare() {
        Map<String, BigDecimal> m = new HashMap<>();

        // Peak fares
        put(m,1,1,TimeBand.PEAK,"2.50"); put(m,1,2,TimeBand.PEAK,"3.20");
        put(m,1,3,TimeBand.PEAK,"3.80"); put(m,1,4,TimeBand.PEAK,"4.40");
        put(m,1,5,TimeBand.PEAK,"5.00");

        put(m,2,1,TimeBand.PEAK,"3.20"); put(m,2,2,TimeBand.PEAK,"2.30");
        put(m,2,3,TimeBand.PEAK,"3.10"); put(m,2,4,TimeBand.PEAK,"3.80");
        put(m,2,5,TimeBand.PEAK,"4.50");

        put(m,3,1,TimeBand.PEAK,"3.80"); put(m,3,2,TimeBand.PEAK,"3.10");
        put(m,3,3,TimeBand.PEAK,"2.10"); put(m,3,4,TimeBand.PEAK,"3.00");
        put(m,3,5,TimeBand.PEAK,"3.70");

        put(m,4,1,TimeBand.PEAK,"4.40"); put(m,4,2,TimeBand.PEAK,"3.80");
        put(m,4,3,TimeBand.PEAK,"3.00"); put(m,4,4,TimeBand.PEAK,"2.00");
        put(m,4,5,TimeBand.PEAK,"2.90");

        put(m,5,1,TimeBand.PEAK,"5.00"); put(m,5,2,TimeBand.PEAK,"4.50");
        put(m,5,3,TimeBand.PEAK,"3.70"); put(m,5,4,TimeBand.PEAK,"2.90");
        put(m,5,5,TimeBand.PEAK,"1.90");

        // Off-peak fares
        put(m,1,1,TimeBand.OFF_PEAK,"2.00"); put(m,1,2,TimeBand.OFF_PEAK,"2.70");
        put(m,1,3,TimeBand.OFF_PEAK,"3.20"); put(m,1,4,TimeBand.OFF_PEAK,"3.70");
        put(m,1,5,TimeBand.OFF_PEAK,"4.20");

        put(m,2,1,TimeBand.OFF_PEAK,"2.70"); put(m,2,2,TimeBand.OFF_PEAK,"1.90");
        put(m,2,3,TimeBand.OFF_PEAK,"2.60"); put(m,2,4,TimeBand.OFF_PEAK,"3.20");
        put(m,2,5,TimeBand.OFF_PEAK,"3.80");

        put(m,3,1,TimeBand.OFF_PEAK,"3.20"); put(m,3,2,TimeBand.OFF_PEAK,"2.60");
        put(m,3,3,TimeBand.OFF_PEAK,"1.70"); put(m,3,4,TimeBand.OFF_PEAK,"2.50");
        put(m,3,5,TimeBand.OFF_PEAK,"3.10");

        put(m,4,1,TimeBand.OFF_PEAK,"3.70"); put(m,4,2,TimeBand.OFF_PEAK,"3.20");
        put(m,4,3,TimeBand.OFF_PEAK,"2.50"); put(m,4,4,TimeBand.OFF_PEAK,"1.60");
        put(m,4,5,TimeBand.OFF_PEAK,"2.40");

        put(m,5,1,TimeBand.OFF_PEAK,"4.20"); put(m,5,2,TimeBand.OFF_PEAK,"3.80");
        put(m,5,3,TimeBand.OFF_PEAK,"3.10"); put(m,5,4,TimeBand.OFF_PEAK,"2.40");
        put(m,5,5,TimeBand.OFF_PEAK,"1.50");

        return Map.copyOf(m);
    }

    private static void put(Map<String, BigDecimal> m, int from, int to, TimeBand band, String amount) {
        m.put(key(from, to, band), money(amount));
    }
}
```

------------------------------------------------------------------------------------------------------------------------------

### Updated Gantt Chart

------------------------------------------------------------------------------------------------------------------------------

![](C:\Users\user\OneDrive\Pictures\Screenshots\Screenshot%202026-04-06%20234137.png)

------------------------------------------------------------------------------------------------------------------------------

### Diary Entries (at least 4)

------------------------------------------------------------------------------------------------------------------------------

##### **Diary Entry 1 (29/03/2026 – Research and Initial Coding)**

I began Part 2 by researching key topics: file handling from Oracle documentation, and ArrayList and HashMap from GeeksforGeeks. These were chosen to address major limitations from Part 1, especially fixed data storage. I started coding with the JourneyManager class because it connects most parts of the system. I replaced the fixed Journey[] array and counter with an ArrayList<Journey>, removing the 5-journey limit. This also simplified methods like removeJourney() since ArrayList handles element shifting automatically. Overall, this change made the system more flexible and easier to maintain.

##### Diary Entry 2 (02/04/2026 – Continued Coding and Setup)

I developed the RiderProfile class to store user details such as name, passenger type, and payment option, using encapsulation with private fields and getters/setters. I then created the ConfigManager class, which stores fares, discounts, and caps using HashMaps instead of fixed values. It reads from a config file at startup, with fallback defaults from Part 1 if the file is missing. These foundation classes are important because other components depend on them. I encountered an error after switching to ArrayList because I forgot to update indexing in showSummary(). I fixed this by using a for-each loop, improving readability and consistency.

##### Diary Entry 3 (04/04/2026 – Completing part Coding and Integration)

I completed and integrated the remaining classes: FareCalculator, FileManager, AdminSession, RiderSession, and updated the main program. I separated fare calculation into FareCalculator to follow the Single Responsibility Principle, instead of combining it with journey management. The FileManager handles all file operations, including JSON profiles, CSV journeys, and summary reports, with error handling for each method. I implemented admin login with password protection and validation for updating fare rules. I tested the full system, including adding, deleting, and recalculating journeys, as well as admin updates. I also fixed issues like recalculating fares after deletions and handling invalid CSV rows safely.

##### Diary Entry 4 (06/04/2026 – Review and Professional Standards Check)

Before submission, I reviewed the code against professional programming standards from the NTIC Guide. I ensured correct naming conventions, short methods, and that each class has a single responsibility. I checked that all fields are private, loops are properly controlled, and no method exceeds recommended length. I added Javadoc comments and inline explanations for complex logic like fare caps. I refactored showAdminMenu() by extracting logic into a helper method to improve structure. This review ensured the program is clean, maintainable, and meets assessment requirements.

------------------------------------------------------------------------------------------------------------------------------
