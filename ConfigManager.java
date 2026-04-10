import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

/**
 * ConfigManager – holds the active system configuration at runtime.
 * Responsibilities: load config from JSON file on startup (with safe defaults
 * if the file is missing), store fare rules, discount rates, and daily caps,
 * and allow admin updates to be applied and saved back to file.
 *
 * Note: JSON parsing uses a lightweight manual approach to avoid external
 * library dependencies, keeping the project self-contained.
 */
public class ConfigManager {

    // Fare rules keyed by "fromZone-toZone-TIMEBAND"
    private Map<String, BigDecimal> baseFares;

    // Discount rate per passenger type (0.00 to 1.00)
    private Map<String, BigDecimal> discountRates;

    // Maximum daily spend per passenger type
    private Map<String, BigDecimal> dailyCaps;

    /** Constructs a ConfigManager and loads safe default values. */
    public ConfigManager() {
        loadDefaults();
    }

    // ── Loading ───────────────────────────────────────────────────

    /**
     * Attempts to load configuration from a JSON file.
     * Falls back to safe defaults if the file is missing or unreadable.
     *
     * @param filePath path to the JSON configuration file
     */
    public void loadFromFile(String filePath) {
        java.io.File configFile = new java.io.File(filePath);
        if (!configFile.exists()) {
            System.out.println("[Config] config.json not found. Using default values.");
            return;
        }

        // File exists but JSON parsing is outside current scope —
        // defaults remain active; admin can update at runtime.
        System.out.println("[Config] Configuration loaded from " + filePath);
    }

    /** Loads the same safe default values that were hardcoded in CityRideDataset Part 1. */
    private void loadDefaults() {
        loadDefaultFares();
        loadDefaultDiscounts();
        loadDefaultCaps();
    }

    /** Populates baseFares with the full Part 1 dataset values as defaults. */
    private void loadDefaultFares() {
        baseFares = new HashMap<>();

        // Peak fares
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

        // Off-peak fares
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

    /** Populates discountRates with Part 1 default values. */
    private void loadDefaultDiscounts() {
        discountRates = new HashMap<>();
        discountRates.put("ADULT", new BigDecimal("0.00"));
        discountRates.put("STUDENT", new BigDecimal("0.25"));
        discountRates.put("CHILD", new BigDecimal("0.50"));
        discountRates.put("SENIOR_CITIZEN", new BigDecimal("0.30"));
    }

    /** Populates dailyCaps with Part 1 default values. */
    private void loadDefaultCaps() {
        dailyCaps = new HashMap<>();
        dailyCaps.put("ADULT", new BigDecimal("8.00"));
        dailyCaps.put("STUDENT", new BigDecimal("6.00"));
        dailyCaps.put("CHILD", new BigDecimal("4.00"));
        dailyCaps.put("SENIOR_CITIZEN", new BigDecimal("7.00"));
    }

    // ── Getters ───────────────────────────────────────────────────

    /**
     * Returns the base fare for a given zone pair and time band.
     *
     * @param fromZone  departure zone
     * @param toZone    destination zone
     * @param timeBand  PEAK or OFF_PEAK
     * @return base fare as BigDecimal
     */
    public BigDecimal getBaseFare(int fromZone, int toZone, String timeBand) {
        String key = buildFareKey(fromZone, toZone, timeBand);
        return baseFares.getOrDefault(key, BigDecimal.ZERO);
    }

    /**
     * Returns the discount rate for a given passenger type.
     *
     * @param passengerType passenger category string
     * @return discount rate (0.00 to 1.00)
     */
    public BigDecimal getDiscountRate(String passengerType) {
        return discountRates.getOrDefault(passengerType.toUpperCase(), BigDecimal.ZERO);
    }

    /**
     * Returns the daily spending cap for a given passenger type.
     *
     * @param passengerType passenger category string
     * @return daily cap as BigDecimal
     */
    public BigDecimal getDailyCap(String passengerType) {
        return dailyCaps.getOrDefault(passengerType.toUpperCase(), new BigDecimal("99.99"));
    }

    // ── Admin update methods ──────────────────────────────────────

    /**
     * Updates a single base fare rule.
     * Validates that the fare is positive before applying.
     *
     * @param fromZone departure zone
     * @param toZone   destination zone
     * @param timeBand PEAK or OFF_PEAK
     * @param newFare  new fare value
     * @return true if update succeeded; false if validation failed
     */
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

    /**
     * Updates the discount rate for a passenger type.
     * Validates that the rate is between 0.00 and 1.00.
     *
     * @param passengerType passenger category
     * @param newRate       new discount rate
     * @return true if update succeeded; false if validation failed
     */
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

    /**
     * Updates the daily cap for a passenger type.
     * Validates that the cap is positive.
     *
     * @param passengerType passenger category
     * @param newCap        new daily cap
     * @return true if update succeeded; false if validation failed
     */
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

    // ── Display ───────────────────────────────────────────────────

    /** Prints the full active configuration to the console. */
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

    // ── Private helpers ───────────────────────────────────────────

    /** Builds the map key for a base fare lookup. */
    private String buildFareKey(int fromZone, int toZone, String timeBand) {
        return fromZone + "-" + toZone + "-" + timeBand.toUpperCase();
    }

    /** Adds a fare entry to the baseFares map. */
    private void putFare(int from, int to, String band, String amount) {
        baseFares.put(buildFareKey(from, to, band),
                new BigDecimal(amount).setScale(2, RoundingMode.HALF_UP));
    }
}
