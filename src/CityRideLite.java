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
