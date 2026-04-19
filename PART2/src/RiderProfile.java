// RiderProfile Class
public class RiderProfile {

    private String name;
    private String passengerType;
    private String defaultPayment;

    public RiderProfile(String name, String passengerType, String defaultPayment) {
        this.name = name;
        this.passengerType = passengerType;
        this.defaultPayment = defaultPayment;
    }

    public String getName() {
        return name;
    }

    public String getPassengerType() {
        return passengerType;
    }

    public String getDefaultPayment() {
        return defaultPayment;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassengerType(String passengerType) {
        this.passengerType = passengerType;
    }

    public void setDefaultPayment(String defaultPayment) {
        this.defaultPayment = defaultPayment;
    }

    public void displayProfile() {
        System.out.println("--------------------");
        System.out.println("Name           : " + name);
        System.out.println("Passenger Type : " + passengerType);
        System.out.println("Default Payment: " + defaultPayment);
        System.out.println("--------------------");
    }
}