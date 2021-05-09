package kata.supermarket;

import kata.supermarket.discounts.Discount;
import kata.supermarket.discounts.DiscountService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Basket {
    private final List<Item> items;
    private final DiscountService discountService;

    public Basket() {
        this.items = new ArrayList<>();
        this.discountService = new DiscountService();
    }

    public void add(final Item item) {
        this.items.add(item);
    }

    List<Item> items() {
        return Collections.unmodifiableList(items);
    }

    public BigDecimal total() {
        return new TotalCalculator().calculate();
    }

    private class TotalCalculator {
        private final List<Item> items;

        TotalCalculator() {
            this.items = items();
        }

        private BigDecimal subtotal() {
            return items.stream().map(Item::price)
                    .reduce(BigDecimal::add)
                    .orElse(BigDecimal.ZERO)
                    .setScale(2, RoundingMode.HALF_UP);
        }

        /**
         * TODO: This could be a good place to apply the results of
         *  the discount calculations.
         *  It is not likely to be the best place to do those calculations.
         *  Think about how Basket could interact with something
         *  which provides that functionality.
         *
         *
         *  Could have some kind of discount service which provides whatever discounts are needed for the day
         */
        private BigDecimal discounts() {
            BigDecimal totalDiscounts = BigDecimal.ZERO;
            for (Discount discount : discountService.getDiscounts()) {
                totalDiscounts = totalDiscounts.add(discount.applyDiscount(items()));
            }
            return totalDiscounts;
        }

        private BigDecimal calculate() {
            return subtotal().subtract(discounts());
        }
    }
}
