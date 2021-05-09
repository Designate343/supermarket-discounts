package kata.supermarket.discounts;

import kata.supermarket.Basket;
import kata.supermarket.Item;

import java.math.BigDecimal;
import java.util.List;

public interface Discount {
    BigDecimal applyDiscount(List<Item> basket);
}
