package kata.supermarket;

import java.math.BigDecimal;
import java.util.UUID;

public class Product {

    private final BigDecimal pricePerUnit;
    private final UUID productId;

    public Product(final BigDecimal pricePerUnit, UUID productId) {
        this.pricePerUnit = pricePerUnit;
        this.productId = productId;
    }

    BigDecimal pricePerUnit() {
        return pricePerUnit;
    }

    //I might suggest adding an 'N'of method here
    public Item oneOf() {
        return new ItemByUnit(this);
    }

    public UUID getProductId() {
        return productId;
    }
}
