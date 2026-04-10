import java.math.BigDecimal;

public class Journey {

    private String passengerName;
    private int id;
    private int fromZone;
    private int toZone;
    private CityRideDataset.PassengerType passengerType;
    private CityRideDataset.TimeBand timeBand;
    private BigDecimal fare;

    public Journey(String passengerName, int id, int fromZone, int toZone,
                   CityRideDataset.PassengerType passengerType,
                   CityRideDataset.TimeBand timeBand,
                   BigDecimal fare) {

        this.passengerName = passengerName;
        this.id = id;
        this.fromZone = fromZone;
        this.toZone = toZone;
        this.passengerType = passengerType;
        this.timeBand = timeBand;
        this.fare = fare;
    }

    //geeksforgeeks
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

    public int getZonesCrossed() {return Math.abs(toZone - fromZone); }

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
        System.out.print("--------------------\n");
        System.out.println("Passenger Name: " + passengerName);
        System.out.println("Journey ID: " + id);
        System.out.println("From Zone: " + fromZone);
        System.out.println("To Zone: " + toZone);
        System.out.println("Passenger Type: " + passengerType);
        System.out.println("Zones Crossed: " + getZonesCrossed());
        System.out.println("Time Band: " + timeBand);
        System.out.println("Fare: £" + fare);
        System.out.print("--------------------\n");
    }
}