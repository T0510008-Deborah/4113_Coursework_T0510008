import java.math.BigDecimal;

/**
 * Journey – stores all data for a single public transport journey.
 * Responsibilities: hold journey details including zones, time band,
 * passenger type, and all three fare components (base, discount, charged).
 * Unchanged fields from Part 1 are retained; new fields added for Part 2.
 */
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

    /**
     * Constructs a Journey with all required fields.
     *
     * @param id             unique journey identifier
     * @param date           date of journey (dd/MM/yyyy)
     * @param passengerName  name of the passenger
     * @param fromZone       departure zone (1–5)
     * @param toZone         destination zone (1–5)
     * @param timeBand       PEAK or OFF_PEAK
     * @param passengerType  passenger category
     * @param baseFare       fare before discount
     * @param discountApplied discount amount deducted
     * @param chargedFare    final amount charged after discount and cap
     */
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

    // ── Getters ──────────────────────────────────────────────────

    /** Returns the unique journey ID. */
    public int getId() { return id; }

    /** Returns the journey date. */
    public String getDate() { return date; }

    /** Returns the passenger name. */
    public String getPassengerName() { return passengerName; }

    /** Returns the departure zone. */
    public int getFromZone() { return fromZone; }

    /** Returns the destination zone. */
    public int getToZone() { return toZone; }

    /** Returns the time band (PEAK or OFF_PEAK). */
    public String getTimeBand() { return timeBand; }

    /** Returns the passenger type. */
    public String getPassengerType() { return passengerType; }

    /** Returns the number of zones crossed. */
    public int getZonesCrossed() { return Math.abs(toZone - fromZone); }

    /** Returns the base fare before any discount. */
    public BigDecimal getBaseFare() { return baseFare; }

    /** Returns the discount amount applied. */
    public BigDecimal getDiscountApplied() { return discountApplied; }

    /** Returns the final charged fare after discount and daily cap. */
    public BigDecimal getChargedFare() { return chargedFare; }

    // ── Setters ──────────────────────────────────────────────────

    /** Updates the charged fare (used by recalculateTotals). */
    public void setChargedFare(BigDecimal chargedFare) {
        this.chargedFare = chargedFare;
    }

    /** Updates the discount applied (used by recalculateTotals). */
    public void setDiscountApplied(BigDecimal discountApplied) {
        this.discountApplied = discountApplied;
    }

    // ── Display ──────────────────────────────────────────────────

    /** Prints a formatted summary of this journey to the console. */
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

    /**
     * Returns this journey as a CSV row string.
     * Column order: id, date, passengerName, fromZone, toZone,
     *               timeBand, passengerType, baseFare, discountApplied, chargedFare
     */
    public String toCsvRow() {
        return id + "," + date + "," + passengerName + ","
                + fromZone + "," + toZone + ","
                + timeBand + "," + passengerType + ","
                + baseFare + "," + discountApplied + "," + chargedFare;
    }
}
