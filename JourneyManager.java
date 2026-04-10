import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * JourneyManager – manages the list of journeys for the current session.
 * Responsibilities: add, remove, list, filter journeys and generate
 * the end-of-day summary. Uses FareCalculator for all fare logic.
 *
 * Part 2 upgrade: replaced fixed-size array with ArrayList to remove
 * the five-journey limit that was a known limitation of Part 1.
 */
public class JourneyManager {

    private final List<Journey> journeys;
    private final FareCalculator fareCalculator;
    private int nextId;

    private static final String SEPARATOR = "--------------------";
    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Constructs a JourneyManager with an empty journey list.
     *
     * @param fareCalculator the calculator used to compute journey fares
     */
    public JourneyManager(FareCalculator fareCalculator) {
        this.journeys = new ArrayList<>();
        this.fareCalculator = fareCalculator;
        this.nextId = 1;
    }

    // ── Journey management ────────────────────────────────────────

    /**
     * Adds a new journey after calculating the fare.
     *
     * @param passengerName name of the passenger
     * @param fromZone      departure zone (1–5)
     * @param toZone        destination zone (1–5)
     * @param timeBand      PEAK or OFF_PEAK
     * @param passengerType passenger category
     */
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

    /**
     * Removes a journey by ID and recalculates all totals.
     *
     * @param id the journey ID to remove
     */
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

    /**
     * Recalculates all journey fares from scratch after a deletion.
     * Processes journeys in order so cap enforcement remains correct.
     */
    public void recalculateTotals() {
        // Track running totals per passenger type during recalculation
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

    // ── Display methods ───────────────────────────────────────────

    /** Lists all journeys stored in the current session. */
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

    /**
     * Lists only journeys matching the given passenger type.
     *
     * @param passengerType the type to filter by
     */
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

    /** Displays running totals and cap status for all passenger types seen today. */
    public void showRunningTotals() {
        if (journeys.isEmpty()) {
            System.out.println(SEPARATOR);
            System.out.println("No journeys recorded yet.");
            System.out.println(SEPARATOR);
            return;
        }

        // Collect unique passenger types
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

    /** Displays the end-of-day summary report. */
    public void showSummary() {
        if (journeys.isEmpty()) {
            System.out.println(SEPARATOR);
            System.out.println("No journeys to summarise.");
            System.out.println(SEPARATOR);
            return;
        }

        BigDecimal totalCharged = BigDecimal.ZERO;
        BigDecimal totalUncapped = BigDecimal.ZERO;
        BigDecimal maxFare = BigDecimal.ZERO;
        int maxFareId = -1;
        int peakCount = 0;
        int offPeakCount = 0;

        for (Journey journey : journeys) {
            totalCharged = totalCharged.add(journey.getChargedFare());
            BigDecimal uncapped = journey.getBaseFare().subtract(journey.getDiscountApplied());
            totalUncapped = totalUncapped.add(uncapped);

            if (journey.getChargedFare().compareTo(maxFare) > 0) {
                maxFare = journey.getChargedFare();
                maxFareId = journey.getId();
            }

            if (journey.getTimeBand().equalsIgnoreCase("PEAK")) {
                peakCount++;
            } else {
                offPeakCount++;
            }
        }

        BigDecimal average = totalCharged.divide(
                BigDecimal.valueOf(journeys.size()), 2, RoundingMode.HALF_UP);
        BigDecimal savings = totalUncapped.subtract(totalCharged);

        System.out.println("========== END OF DAY SUMMARY ==========");
        System.out.println("Total journeys         : " + journeys.size());
        System.out.println("Total cost             : £" + totalCharged);
        System.out.println("Average cost           : £" + average);
        System.out.println("Most expensive journey : ID " + maxFareId + " (£" + maxFare + ")");
        System.out.println("Savings (cap applied)  : £" + savings);
        System.out.println("Peak journeys          : " + peakCount);
        System.out.println("Off-peak journeys      : " + offPeakCount);
        System.out.println("========================================");
    }

    // ── Query helpers ─────────────────────────────────────────────

    /** Returns true if at least one journey has been recorded. */
    public boolean hasJourneys() {
        return !journeys.isEmpty();
    }

    /** Returns an unmodifiable view of all journeys (for FileManager export). */
    public List<Journey> getJourneys() {
        return java.util.Collections.unmodifiableList(journeys);
    }

    /**
     * Returns the total amount already charged to a given passenger type today.
     *
     * @param passengerType the type to total
     * @return cumulative charged fare
     */
    private BigDecimal getTotalChargedForType(String passengerType) {
        BigDecimal total = BigDecimal.ZERO;
        for (Journey journey : journeys) {
            if (journey.getPassengerType().equalsIgnoreCase(passengerType)) {
                total = total.add(journey.getChargedFare());
            }
        }
        return total;
    }

    /**
     * Finds the list index of a journey by its ID.
     *
     * @param id the journey ID to search for
     * @return list index, or -1 if not found
     */
    private int findIndexById(int id) {
        for (int i = 0; i < journeys.size(); i++) {
            if (journeys.get(i).getId() == id) {
                return i;
            }
        }
        return -1;
    }
}
