package kata.supermarket;

import java.math.BigDecimal;
import java.util.UUID;

public class WeighedProduct {

    private final BigDecimal pricePerKilo;
    private final UUID productId;

    public WeighedProduct(final BigDecimal pricePerKilo, UUID productId) {
        this.pricePerKilo = pricePerKilo;
        this.productId = productId;
    }

    BigDecimal pricePerKilo() {
        return pricePerKilo;
    }

    public Item weighing(final BigDecimal kilos) {
        return new ItemByWeight(this, kilos);
    }

    public UUID getProductId() {
        return productId;
    }
}
