package kata.supermarket.discounts;

import kata.supermarket.Item;
import kata.supermarket.ItemByUnit;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class BuyTwoItemsFor1Pound implements Discount {

    private static final Map<UUID, Integer> itemCounts = new HashMap<>();

    @Override
    public BigDecimal applyDiscount(List<Item> basket) {
        BigDecimal discount = BigDecimal.ZERO;
        for (Item item : basket) {
            if (item instanceof ItemByUnit) {
                if (itemCounts.containsKey(item.itemId())) {
                    int count = itemCounts.get(item.itemId()) + 1;
                    if (count % 2 == 0) {
                        discount = discount.add(item.price().multiply(new BigDecimal(2)));
                        discount = discount.subtract(new BigDecimal(1));
                    }
                    itemCounts.put(item.itemId(), count);
                } else {
                    itemCounts.put(item.itemId(), 1);
                }
            }
        }
        return discount;
    }
}
