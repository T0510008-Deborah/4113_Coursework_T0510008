import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

// JourneyManager Class
public class JourneyManager {

    private final List<Journey> journeys;
    private final FareCalculator fareCalculator;
    private int nextId;

    private static final String SEPARATOR = "--------------------";
    private static final String WIDE_SEP  = "========================================";
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

        String today = LocalDate.now().format(DATE_FORMAT);
        Journey journey = new Journey(nextId, today, passengerName,
                fromZone, toZone, timeBand, passengerType,
                fareResult[0], fareResult[1], fareResult[2]);

        journeys.add(journey);
        nextId++;
        printAddConfirmation(fareResult);
    }

    public void removeJourney(int id) {
        int index = findIndexById(id);
        if (index == -1) {
            System.out.println(SEPARATOR);
            System.out.println("Error: Journey ID " + id + " not found.");
            System.out.println(SEPARATOR);
            return;
        }
        journeys.remove(index);
        recalculateTotals();
        System.out.println(SEPARATOR);
        System.out.println("Journey " + id + " removed. Totals recalculated.");
        System.out.println(SEPARATOR);
    }

    public void recalculateTotals() {
        Map<String, BigDecimal> runningTotals = new HashMap<>();
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
        Set<String> types = collectPassengerTypes();
        System.out.println(SEPARATOR);
        System.out.println("RUNNING TOTALS:");
        for (String type : types) {
            BigDecimal total = getTotalChargedForType(type);
            BigDecimal cap   = fareCalculator.getDailyCap(type);
            System.out.println("  " + type + ": £" + total + " / £" + cap + " cap");
        }
        System.out.println(SEPARATOR);
    }

    public void showSummary() {
        if (journeys.isEmpty()) {
            System.out.println(SEPARATOR);
            System.out.println("No journeys to summarise.");
            System.out.println(SEPARATOR);
            return;
        }
        int[]        counts  = countTimeBands();
        BigDecimal[] totals  = calculateTotals();
        int          maxId   = findMostExpensiveId();

        BigDecimal totalCharged = totals[0];
        BigDecimal totalUncapped = totals[1];
        BigDecimal average = totalCharged.divide(
                BigDecimal.valueOf(journeys.size()), 2, RoundingMode.HALF_UP);
        BigDecimal savings = totalUncapped.subtract(totalCharged);

        printSummary(totalCharged, average, maxId, savings, counts[0], counts[1]);
    }

    public boolean hasJourneys() {
        return !journeys.isEmpty();
    }

    public List<Journey> getJourneys() {
        return Collections.unmodifiableList(journeys);
    }

    private BigDecimal getTotalChargedForType(String passengerType) {
        BigDecimal total = BigDecimal.ZERO;
        for (Journey journey : journeys) {
            if (journey.getPassengerType().equalsIgnoreCase(passengerType)) {
                total = total.add(journey.getChargedFare());
            }
        }
        return total;
    }

    private int findIndexById(int id) {
        for (int i = 0; i < journeys.size(); i++) {
            if (journeys.get(i).getId() == id) {
                return i;
            }
        }
        return -1;
    }

    private Set<String> collectPassengerTypes() {
        Set<String> types = new LinkedHashSet<>();
        for (Journey j : journeys) {
            types.add(j.getPassengerType());
        }
        return types;
    }

    private int[] countTimeBands() {
        int peak = 0;
        int offPeak = 0;
        for (Journey j : journeys) {
            if (j.getTimeBand().equalsIgnoreCase("PEAK")) {
                peak++;
            } else {
                offPeak++;
            }
        }
        return new int[]{peak, offPeak};
    }

    private BigDecimal[] calculateTotals() {
        BigDecimal totalCharged  = BigDecimal.ZERO;
        BigDecimal totalUncapped = BigDecimal.ZERO;
        for (Journey j : journeys) {
            totalCharged  = totalCharged.add(j.getChargedFare());
            totalUncapped = totalUncapped.add(
                    j.getBaseFare().subtract(j.getDiscountApplied()));
        }
        return new BigDecimal[]{totalCharged, totalUncapped};
    }

    private int findMostExpensiveId() {
        int maxId = -1;
        BigDecimal maxFare = BigDecimal.ZERO;
        for (Journey j : journeys) {
            if (j.getChargedFare().compareTo(maxFare) > 0) {
                maxFare = j.getChargedFare();
                maxId   = j.getId();
            }
        }
        return maxId;
    }

    private void printAddConfirmation(BigDecimal[] fareResult) {
        System.out.println(SEPARATOR);
        System.out.println("Journey added successfully.");
        System.out.println("Base fare    : £" + fareResult[0]);
        System.out.println("Discount     : £" + fareResult[1]);
        System.out.println("Charged fare : £" + fareResult[2]);
        System.out.println(SEPARATOR);
    }

    private void printSummary(BigDecimal total, BigDecimal average,
                              int maxId, BigDecimal savings,
                              int peakCount, int offPeakCount) {
        System.out.println(WIDE_SEP);
        System.out.println("  END OF DAY SUMMARY");
        System.out.println(WIDE_SEP);
        System.out.println("Total journeys         : " + journeys.size());
        System.out.println("Total cost             : £" + total);
        System.out.println("Average cost           : £" + average);
        System.out.println("Most expensive journey : ID " + maxId);
        System.out.println("Savings from cap       : £" + savings);
        System.out.println("Peak journeys          : " + peakCount);
        System.out.println("Off-peak journeys      : " + offPeakCount);
        System.out.println(WIDE_SEP);
    }
}