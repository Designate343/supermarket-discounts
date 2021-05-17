package kata.supermarket.discounts;

import kata.supermarket.Item;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Set;

public class BulkVegetableDiscount implements Discount {

    private Set<String> appliesTo = Set.of("CELERY", "CARROT");

    @Override
    public BigDecimal applyDiscount(List<Item> basket) {
        BigDecimal totalInKilos = BigDecimal.ZERO;
        BigDecimal totalCostOfVeg = BigDecimal.ZERO;
        for (var item : basket) {
            if (appliesTo.contains(item.name())) {
                totalInKilos = totalInKilos.add(item.amountOf());
                totalCostOfVeg = totalCostOfVeg.add(item.price());
            }
        }

        if (totalInKilos.compareTo(BigDecimal.valueOf(1)) > 0) {
            return totalCostOfVeg.divide(new BigDecimal(2), RoundingMode.HALF_DOWN);
        }

        return BigDecimal.ZERO;
    }

}
