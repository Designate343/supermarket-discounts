package kata.supermarket.discounts;

import kata.supermarket.Item;

import java.math.BigDecimal;
import java.util.List;

public interface Discount {
    BigDecimal applyDiscount(List<Item> basket);

    //future appliesTo(UUID productId) ?
    //to allow granular discount application
}
