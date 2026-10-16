import java.math.BigDecimal;
import java.math.RoundingMode;

// FareCalculator Class
public class FareCalculator {

    private final ConfigManager configManager;

    public FareCalculator(ConfigManager configManager) {
        this.configManager = configManager;
    }

    public BigDecimal[] calculate(int fromZone, int toZone, String timeBand, String passengerType,
                                  BigDecimal totalSoFar) {

        BigDecimal baseFare = configManager.getBaseFare(fromZone, toZone, timeBand);
        BigDecimal discountRate = configManager.getDiscountRate(passengerType);
        BigDecimal dailyCap = configManager.getDailyCap(passengerType);

        BigDecimal discountApplied = baseFare.multiply(discountRate) .setScale(2,
                RoundingMode.HALF_UP);

        BigDecimal fareAfterDiscount = baseFare.subtract(discountApplied);

        BigDecimal chargedFare = applyDailyCap(fareAfterDiscount, totalSoFar, dailyCap);

        return new BigDecimal[]{
                baseFare, discountApplied, chargedFare
        };
    }

    public BigDecimal getDailyCap(String passengerType) {
        return configManager.getDailyCap(passengerType);

    }

    private BigDecimal applyDailyCap(BigDecimal fare, BigDecimal totalSoFar, BigDecimal cap) {

        BigDecimal projectedTotal = totalSoFar.add(fare);
        if (projectedTotal.compareTo(cap) > 0) {
            BigDecimal remaining = cap.subtract(totalSoFar);
            if (remaining.compareTo(BigDecimal.ZERO) < 0) {
                return BigDecimal.ZERO;
            }
            return remaining;
        }
        return fare;
    }
}