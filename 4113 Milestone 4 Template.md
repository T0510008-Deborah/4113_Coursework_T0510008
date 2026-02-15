# IY4113 Milestone 4

| Assessment Details | Please Complete All Details                                       |
| ------------------ | ----------------------------------------------------------------- |
| Group              | B                                                                 |
| Module Title       | Applied Software Engineering using Object Orientiated Programming |
| Assessment Type    | Java Fundamentals 1                                               |
| Module Tutor Name  | Jonathan Shore                                                    |
| Student ID Number  | P510008                                                           |
| Date of Submission | 15 February 2026                                                  |
| Word Count         | 1797                                                              |
| GItHub Link        | https://github.com/T0510008-Deborah/4113_T0510008_PART_1          |

- [x] *I confirm that this assignment is my own work. Where I have referred to academic sources, I have provided in-text citations and included the sources in
  the final reference list.*

- [x] *Where I have used AI, I have cited and referenced appropriately.

------------------------------------------------------------------------------------------------------------------------------

### Program Code

---

**CityRideLite.java (Main Class)**

This is the main entry point of the program and is responsible for controlling the overall flow of the application. It displays the main menu, handles user input, and calls the appropriate methods based on the user’s selection. The class uses a while loop to keep the program running until the user chooses to exit. This class acts as the user interface controller, managing how the user interacts with the system through the console.

------------------------------------------------------------------------------------------------------------------------------

```java
import java.util.Scanner;

public class CityRideLite {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        JourneyManager journeyManager = new JourneyManager();
        int choice;

        do {
            System.out.println("\n--- CityRide Lite ---");
            System.out.println("1. Add Journey");
            System.out.println("2. View Journeys");
            System.out.println("3. Filter Journeys");
            System.out.println("4. Remove Journey");
            System.out.println("5. Daily Summary");
            System.out.println("6. Exit");
            System.out.print("Enter choice: ");
            choice = scanner.nextInt();

            if (choice == 1) {

                int fromZone;
                int toZone;

                do {
                    System.out.print("Enter from zone (1-5): ");
                    fromZone = scanner.nextInt();
                } while (fromZone < 1 || fromZone > 5);

                do {
                    System.out.print("Enter to zone (1-5): ");
                    toZone = scanner.nextInt();
                } while (toZone < 1 || toZone > 5);

                System.out.print("Passenger type (ADULT/STUDENT/CHILD/SENIOR_CITIZEN): ");
                CityRideDataset.PassengerType passengerType =
                        CityRideDataset.PassengerType.valueOf(
                                scanner.next().toUpperCase()
                        );

                System.out.print("Time band (PEAK/OFF_PEAK): ");
                CityRideDataset.TimeBand timeBand =
                        CityRideDataset.TimeBand.valueOf(
                                scanner.next().toUpperCase()
                        );

                journeyManager.addJourney(fromZone, toZone, passengerType, timeBand);

            } else if (choice == 2) {
                journeyManager.listJourneys();

            } else if (choice == 3) {
                System.out.print("Filter by passenger type: ");
                CityRideDataset.PassengerType type =
                        CityRideDataset.PassengerType.valueOf(
                                scanner.next().toUpperCase()
                        );
                journeyManager.filterByPassengerType(type);

            } else if (choice == 4) {
                System.out.print("Enter Journey ID to remove: ");
                int id = scanner.nextInt();
                journeyManager.removeJourney(id);

            } else if (choice == 5) {
                journeyManager.showSummary();

            } else if (choice == 6) {
                System.out.println("Exiting program...");

            } else {
                System.out.println("Invalid option.");
            }

        } while (choice != 6);

        scanner.close();
    }
}
```

------------------------------------------------------------------------------------------------------------------------------

**Journey.java (Data Class)**

The Journey class represents a single public transport journey. It stores all the relevant information about a trip, such as the journey ID, starting zone, destination zone, passenger type, and time band. Each time a new journey is created.

------------------------------------------------------------------------------------------------------------------------------

```java
import java.math.BigDecimal;

public class Journey {

    private int id;
    private int fromZone;
    private int toZone;
    private CityRideDataset.PassengerType passengerType;
    private CityRideDataset.TimeBand timeBand;
    private BigDecimal fare;

    public Journey(int id, int fromZone, int toZone,
                   CityRideDataset.PassengerType passengerType,
                   CityRideDataset.TimeBand timeBand,
                   BigDecimal fare) {
        this.id = id;
        this.fromZone = fromZone;
        this.toZone = toZone;
        this.passengerType = passengerType;
        this.timeBand = timeBand;
        this.fare = fare;
    }

    public int getId() {
        return id;
    }

    public int getFromZone() {
        return fromZone;
    }

    public int getToZone() {
        return toZone;
    }

    public CityRideDataset.PassengerType getPassengerType() {
        return passengerType;
    }

    public CityRideDataset.TimeBand getTimeBand() {
        return timeBand;
    }

    public BigDecimal getFare() {
        return fare;
    }

    public void setFare(BigDecimal fare) {
        this.fare = fare;
    }

    public void displayJourney() {
        System.out.println("ID: " + id);
        System.out.println("From Zone: " + fromZone);
        System.out.println("To Zone: " + toZone);
        System.out.println("Passenger Type: " + passengerType);
        System.out.println("Time Band: " + timeBand);
        System.out.println("Fare: £" + fare);
    }
}
```

------------------------------------------------------------------------------------------------------------------------------

###### JourneyManager.java (Data Class)

The JourneyManager class manages all journey related operations, including storing journeys, removing journeys, filtering journeys, and generating daily summaries. It also handles fare calculations by using the rules provided in the CityRideDataset class.

GeeksforGeeks (2025, July). *BigDecimal divide() Method in Java with Examples*. Retrieves from: https://www.geeksforgeeks.org/java/bigdecimal-divide-method-in-java-with-examples/ [Accessed 13 February 2026]

------------------------------------------------------------------------------------------------------------------------------

```java
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
        System.out.println("Off - peak journeys: " + offPeakCount);
    }
}

```

------------------------------------------------------------------------------------------------------------------------------

###### CityRideDataset.java

The CityRideDataset class is a utility class that contains all fixed fare data, discounts, and daily caps. It provides static methods that allow the system to retrieve base fares and apply rules consistently.

------------------------------------------------------------------------------------------------------------------------------

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

##### Updated Algorithm

This is the updated Algorithm flowchart for MainMenu.

![](C:\Users\user\Desktop\MainMenu_flowchart.png)

------------------------------------------------------------------------------------------------------------------------------

#### Updated Class Diagram

This is the updated Class Diagram

![](C:\Users\user\Desktop\Class%20Diagram.png)

------------------------------------------------------------------------------------------------------------------------------

#### Input Process Output Table

This is the update for the input Process Output Table with the appropriate data types of the inputs.

| **Feature / Task**         | **Inputs**                                                                                            | **Processing (what the system does)**                                               | **Outputs**                |
| -------------------------- | ----------------------------------------------------------------------------------------------------- | ----------------------------------------------------------------------------------- | -------------------------- |
| **Start program**          | None (program launch)                                                                                 | Initialise data structures and running totals                                       | Main menu shown            |
| **Display menu (FR1)**     | User menu choice (1 - 5) (int)                                                                        | Validate choice and route to correct function                                       | Selected screen displayed  |
| **Add journey** **(FR2)**  | Date(String), from zone(int), to zone(int), passenger type(String), time band(String) - peak/off-peak | Validate inputs, calculate zones crossed, lookup fare, apply discount and daily cap | Journey added confirmation |
| **Calculate fare (FR3)**   | From zone (int), to zone(int), time band (String), passenger type (String)                            | Calculates zones crossed, get base fare, apply discount and daily cap               | Final charged fare         |
| **Store journey (FR4)**    | Journey details (Data Structure)                                                                      | Create Journey object and assign ID                                                 | Journey stored in memory   |
| **List journeys**          | View Request (Boolean: Y/N )                                                                          | Loop through journey list                                                           | Display all journeys       |
| **Filter< journeys (FR5)** | Filter Criteria (Type: String, Value: String/Integer)                                                 | Search journey list                                                                 | Filtered journey list      |
| **Remove journey (FR6)**   | Journey ID (Integer)                                                                                  | Validate ID, remove journey, recalculate totals                                     | Confirmation or error      |
| **Daily summary (FR7)**    | Summary Request (Boolean: Y/N)                                                                        | Calculate totals, averages, max fare                                                | Summary report             |
| **Reset day**              | Confirmation (Boolean: Y/N)                                                                           | Clear all lists and totals                                                          | System reset message       |
| **Exit**                   | Menu choice – 5 : Exit Command (int)                                                                  | Terminate program                                                                   | Goodbye message            |

------------------------------------------------------------------------------------------------------------------------------

### Updated Gantt Chart

---

![](C:\Users\user\OneDrive\Pictures\Screenshots\Screenshot%202026-02-15%20234402.png)

------------------------------------------------------------------------------------------------------------------------------

### Diary Entries

------------------------------------------------------------------------------------------------------------------------------

##### 13/02/2026  Diary Entry 1 – Program Implementation

In this part of the milestone, I focused mainly on completing the core program code for the CityRide Lite system. I implemented the JourneyManager class to handle the storage and management of journeys, including adding, listing, filtering, and removing journeys. I also integrated the provided CityRideDataset class into the project as a separate utility class to ensure that all fares, discounts, and daily caps are calculated using the fixed dataset rules.

##### 15/02/2026  Diary Entry 2 – Algorithm Design Updates and Gantt Chart

After completing most of the coding, I reviewed and updated the design documents to match the final implementation. This included improving the main menu flowchart, refining the class diagram to show clearer relationships between classes, correcting the Input Process Output table based on lecturer feedback, and updating the Gantt chart to reflect the progress made in Milestone 4.

------------------------------------------------------------------------------------------------------------------------------
