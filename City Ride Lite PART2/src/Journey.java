import java.math.BigDecimal;

// Journey Class
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