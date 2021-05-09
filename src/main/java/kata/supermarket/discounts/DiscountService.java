package kata.supermarket.discounts;

import java.util.Collections;
import java.util.List;

public class DiscountService {

    /**
     * Gets hardcoded list of discounts. Would be extended to fetch discounts from another service or db etc.
     * @return the discounts to be applied
     */
    public List<Discount> getDiscounts() {
        return Collections.emptyList();
    }
}
