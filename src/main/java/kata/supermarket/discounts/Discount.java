package kata.supermarket.discounts;

import kata.supermarket.Item;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public interface Discount {
    BigDecimal applyDiscount(List<Item> basket);
}
