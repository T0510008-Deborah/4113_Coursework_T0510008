import java.math.BigDecimal;
import java.math.RoundingMode;

public class JourneyManager {

    private Journey[] journeys;
    private int journeyCount;
    private int nextId;

    public JourneyManager() {
        journeys = new Journey[5];
        journeyCount = 0;
        nextId = 1;
    }

    public void addJourney(int fromZone, int toZone,
                           CityRideDataset.PassengerType passengerType,
                           CityRideDataset.TimeBand timeBand) {

        BigDecimal baseFare =
                CityRideDataset.getBaseFare(fromZone, toZone, timeBand);

        BigDecimal discountRate =
                CityRideDataset.DISCOUNT_RATE.get(passengerType);

        BigDecimal discount =
                baseFare.multiply(discountRate);

        BigDecimal finalFare =
                baseFare.subtract(discount);

        BigDecimal totalSoFar = getTotalForPassenger(passengerType);
        BigDecimal cap = CityRideDataset.DAILY_CAP.get(passengerType);

        if (totalSoFar.add(finalFare).compareTo(cap) > 0) {
            finalFare = cap.subtract(totalSoFar);
            if (finalFare.compareTo(BigDecimal.ZERO) < 0) {
                finalFare = BigDecimal.ZERO;
            }
        }

        Journey journey = new Journey(
                nextId,
                fromZone,
                toZone,
                passengerType,
                timeBand,
                finalFare
        );

        journeys[journeyCount] = journey;
        journeyCount++;
        nextId++;

        System.out.println("Journey added successfully.");
        System.out.println("Fare charged: £" + finalFare);
    }

    private BigDecimal getTotalForPassenger(CityRideDataset.PassengerType type) {
        BigDecimal total = BigDecimal.ZERO;
        for (int i = 0; i < journeyCount; i++) {
            if (journeys[i].getPassengerType() == type) {
                total = total.add(journeys[i].getFare());
            }
        }
        return total;
    }

    public void listJourneys() {
        if (journeyCount == 0) {
            System.out.println("No journeys recorded.");
        } else {
            for (int i = 0; i < journeyCount; i++) {
                System.out.println();
                journeys[i].displayJourney();
            }
        }
    }

    public void removeJourney(int id) {
        boolean found = false;

        for (int i = 0; i < journeyCount; i++) {
            if (journeys[i].getId() == id) {
                found = true;

                for (int j = i; j < journeyCount - 1; j++) {
                    journeys[j] = journeys[j + 1];
                }
                journeyCount--;
                System.out.println("Journey removed.");
                break;
            }
        }

        if (!found) {
            System.out.println("Journey ID not found.");
        }
    }

    public void filterByPassengerType(CityRideDataset.PassengerType type) {
        boolean found = false;

        for (int i = 0; i < journeyCount; i++) {
            if (journeys[i].getPassengerType() == type) {
                journeys[i].displayJourney();
                found = true;
                System.out.println();
            }
        }

        if (!found) {
            System.out.println("No journeys found for that passenger type.");
        }
    }

    public void showSummary() {
        if (journeyCount == 0) {
            System.out.println("No journeys to summarise.");
            return;
        }

        BigDecimal total = BigDecimal.ZERO;
        BigDecimal max = BigDecimal.ZERO;
        int peakCount = 0;
        int offPeakCount = 0;

        for (int i = 0; i < journeyCount; i++) {
            total = total.add(journeys[i].getFare());

            if (journeys[i].getFare().compareTo(max) > 0) {
                max = journeys[i].getFare();
            }

            if (journeys[i].getTimeBand() == CityRideDataset.TimeBand.PEAK) {
                peakCount++;
            } else {
                offPeakCount++;
            }
        }

        BigDecimal average =
                total.divide(BigDecimal.valueOf(journeyCount), 2, RoundingMode.HALF_UP);

        System.out.println("\n--- Daily Summary ---");
        System.out.println("Total journeys: " + journeyCount);
        System.out.println("Total cost: £" + total);
        System.out.println("Average cost: £" + average);
        System.out.println("Most expensive journey: £" + max);
        System.out.println("Peak journeys: " + peakCount);
        System.out.println("Off-peak journeys: " + offPeakCount);
    }
}
