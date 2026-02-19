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
//