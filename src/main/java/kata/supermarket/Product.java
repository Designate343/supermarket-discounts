package kata.supermarket;

import java.math.BigDecimal;
import java.util.UUID;

public class Product {

    private final BigDecimal pricePerUnit;
    private final UUID productId;
    private final String productName;

    public Product(final BigDecimal pricePerUnit, UUID productId, String productName) {
        this.pricePerUnit = pricePerUnit;
        this.productId = productId;
        this.productName = productName;
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

    public String getProductName() {
        return productName;
    }
}
