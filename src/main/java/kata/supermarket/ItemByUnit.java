package kata.supermarket;

import java.math.BigDecimal;
import java.util.UUID;

public class ItemByUnit implements Item {

    private final Product product;

    ItemByUnit(final Product product) {
        this.product = product;
    }

    public BigDecimal price() {
        return product.pricePerUnit();
    }

    @Override
    public UUID itemId() {
        return product.getProductId();
    }
}
