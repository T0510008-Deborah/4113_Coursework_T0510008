/**
 * RiderProfile – stores the personal details of a single rider.
 * Responsibilities: hold name, passenger type, and default payment option.
 * Loaded from and saved to JSON by FileManager.
 */
public class RiderProfile {

    private String name;
    private String passengerType;
    private String defaultPayment;

    /** Constructs a RiderProfile with all required fields. */
    public RiderProfile(String name, String passengerType, String defaultPayment) {
        this.name = name;
        this.passengerType = passengerType;
        this.defaultPayment = defaultPayment;
    }

    /** Returns the rider's name. */
    public String getName() {
        return name;
    }

    /** Returns the rider's passenger type (e.g. ADULT, STUDENT). */
    public String getPassengerType() {
        return passengerType;
    }

    /** Returns the rider's default payment option. */
    public String getDefaultPayment() {
        return defaultPayment;
    }

    /** Updates the rider's name. */
    public void setName(String name) {
        this.name = name;
    }

    /** Updates the rider's passenger type. */
    public void setPassengerType(String passengerType) {
        this.passengerType = passengerType;
    }

    /** Updates the rider's default payment option. */
    public void setDefaultPayment(String defaultPayment) {
        this.defaultPayment = defaultPayment;
    }

    /** Returns a formatted summary of the profile for display. */
    public void displayProfile() {
        System.out.println("--------------------");
        System.out.println("Name           : " + name);
        System.out.println("Passenger Type : " + passengerType);
        System.out.println("Default Payment: " + defaultPayment);
        System.out.println("--------------------");
    }
}
