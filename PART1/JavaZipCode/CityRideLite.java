import java.util.Scanner;
import java.math.BigDecimal;

public class CityRideLite {

    //oracle
    private static void viewFareTable() {

        System.out.println("\n--- Fare Table ---");

        for (CityRideDataset.TimeBand band : CityRideDataset.TimeBand.values()) {

            System.out.println("\nTime Band: " + band);
            System.out.println("----------------------------------");

            for (int from = CityRideDataset.MIN_ZONE; from <= CityRideDataset.MAX_ZONE; from++) {
                for (int to = CityRideDataset.MIN_ZONE; to <= CityRideDataset.MAX_ZONE; to++) {

                    BigDecimal fare = CityRideDataset.getBaseFare(from, to, band);

                    System.out.println("From Zone " + from +
                            " to Zone " + to +
                            " : £" + fare);
                }
            }
        }

        System.out.println("\n--- Discount Rates ---");
        for (CityRideDataset.PassengerType type : CityRideDataset.PassengerType.values()) {
            System.out.println(type + " Discount: "
                    + CityRideDataset.DISCOUNT_RATE.get(type).multiply(new BigDecimal("100"))
                    + "%");
        }

        System.out.println("\n--- Daily Caps ---");
        for (CityRideDataset.PassengerType type : CityRideDataset.PassengerType.values()) {
            System.out.println(type + " Daily Cap: £"
                    + CityRideDataset.DAILY_CAP.get(type));
        }
    }

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        JourneyManager journeyManager = new JourneyManager();
        int choice;

            System.out.println("\nStarting program...\n");
            System.out.print("====================\n");
            System.out.println("     WELCOME");
            System.out.println("       TO");
        do {
            System.out.println("  CITYRIDE LITE!");
            System.out.print("====================\n");
            System.out.println("1. View Fare Table");
            System.out.println("2. Add Journey");
            System.out.println("3. View Journeys");
            System.out.println("4. Filter Journeys");
            System.out.println("5. Remove Journey");
            System.out.println("6. Daily Summary");
            System.out.println("7. Exit");
            System.out.print("--------------------\n");
            System.out.print("Enter choice: ");
            choice = scanner.nextInt();

            if (choice == 1) {
                viewFareTable();
            }

            else if (choice == 2) {

                int fromZone;
                int toZone;
                System.out.print("--------------------\n");
                System.out.print("Enter passenger name: ");
                scanner.nextLine();
                String passengerName = scanner.nextLine();

                do {
                    System.out.print("--------------------\n");
                    System.out.print("Enter from zone (1-5): ");
                    fromZone = scanner.nextInt();
                } while (fromZone < 1 || fromZone > 5);

                do {
                    System.out.print("--------------------\n");
                    System.out.print("Enter to zone (1-5): ");
                    toZone = scanner.nextInt();
                } while (toZone < 1 || toZone > 5);

                System.out.print("--------------------\n");
                System.out.print("ADULT\nSTUDENT\nCHILD\nSENIOR_CITIZEN\n");
                System.out.print("--------------------\n");
                System.out.print("Passenger type: ");
                CityRideDataset.PassengerType passengerType =
                        CityRideDataset.PassengerType.valueOf(
                                scanner.next().toUpperCase()
                        );

                System.out.print("--------------------\n");
                System.out.print("PEAK\nOFF_PEAK\n");
                System.out.print("--------------------\n");
                System.out.print("Time band: ");
                CityRideDataset.TimeBand timeBand =
                        CityRideDataset.TimeBand.valueOf(
                                scanner.next().toUpperCase()
                        );

                journeyManager.addJourney(passengerName, fromZone, toZone, passengerType, timeBand);

            } else if (choice == 3) {
                journeyManager.listJourneys();

            } else if (choice == 4) {
                System.out.print("--------------------\n");
                System.out.print("Filter by passenger type: ");
                CityRideDataset.PassengerType type =
                        CityRideDataset.PassengerType.valueOf(
                                scanner.next().toUpperCase()
                        );
                journeyManager.filterByPassengerType(type);

            } else if (choice == 5) {
                if (journeyManager.hasJourneys()) {
                    journeyManager.listJourneys();
                    System.out.print("Enter Journey ID to remove: ");
                    int id = scanner.nextInt();
                    journeyManager.removeJourney(id);
                } else {
                    System.out.print("--------------------\n");
                    System.out.println("No journeys available to remove.");
                    System.out.print("--------------------\n");
            }
            }

            else if (choice == 6) {
                journeyManager.showSummary();
            }

            else if (choice == 7) {
                System.out.print("--------------------\n");
                System.out.println("Goodbye!\n");
                System.out.println("Exiting program...");

            } else {
                System.out.print("--------------------\n");
                System.out.println("Invalid option.\n");
                System.out.print("--------------------\n");

            }

        } while (choice != 7);

        scanner.close();
    }
}
