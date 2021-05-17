package kata.supermarket;

import kata.supermarket.discounts.BulkVegetableDiscount;
import kata.supermarket.discounts.BuyOneGetOneFree;
import kata.supermarket.discounts.BuyTwoItemsFor1Pound;
import kata.supermarket.discounts.DiscountService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BasketTest {

    @DisplayName("basket provides its total value when containing...")
    @MethodSource
    @ParameterizedTest(name = "{0}")
    void basketProvidesTotalValue(String description, String expectedTotal, Iterable<Item> items) {
        DiscountService mockedDiscounts = Mockito.mock(DiscountService.class);
        Mockito.when(mockedDiscounts.getDiscounts()).thenReturn(Collections.emptyList());
        final Basket basket = new Basket(mockedDiscounts);
        items.forEach(basket::add);
        assertEquals(new BigDecimal(expectedTotal), basket.total());
    }

    @Test
    void buyOneGetOneFreeDiscountShouldBeApplied() {
        DiscountService mockedDiscounts = Mockito.mock(DiscountService.class);
        Mockito.when(mockedDiscounts.getDiscounts()).thenReturn(List.of(new BuyOneGetOneFree()));

        UUID hobnobProductId = UUID.randomUUID();
        Product hobnobs = new Product(new BigDecimal("1.17"), hobnobProductId, "name");

        List<Item> items = List.of(hobnobs.oneOf(), hobnobs.oneOf(), aPintOfMilk());
        final Basket basket = new Basket(mockedDiscounts);
        items.forEach(basket::add);
        BigDecimal expectedTotal = hobnobs.oneOf().price().add(aPintOfMilk().price());
        assertEquals(expectedTotal, basket.total());
    }

    @Test
    void buy2ForOnePoundShouldBeApplied() {
        DiscountService mockedDiscounts = Mockito.mock(DiscountService.class);
        Mockito.when(mockedDiscounts.getDiscounts()).thenReturn(List.of(new BuyTwoItemsFor1Pound()));

        UUID hobnobProductId = UUID.randomUUID();
        Product hobnobs = new Product(new BigDecimal("1.17"), hobnobProductId, "name");

        List<Item> items = List.of(hobnobs.oneOf(), hobnobs.oneOf(), aPintOfMilk());
        final Basket basket = new Basket(mockedDiscounts);
        items.forEach(basket::add);
        BigDecimal expectedTotal = new BigDecimal(1).add(aPintOfMilk().price());
        assertEquals(expectedTotal, basket.total());
    }

    @Test
    void shouldBeHalfPriceIfMoreThan1KiloOfVeg() {
        DiscountService mockedDiscounts = Mockito.mock(DiscountService.class);
        Mockito.when(mockedDiscounts.getDiscounts()).thenReturn(List.of(new BulkVegetableDiscount()));


        /**
         * [15:43] Kanellos Kanellis
         *
         * £2 per 1kg of carrots (vegetable); £3 per 1kg of celery (vegetable); £4.99 per 1kg of American Sweets (snacks)
         *
         * Weight: 500g carrots, 200g celery -- Original price: £1.60 -- Expected discounted price: £0.00
         * Weight: 500g carrots, 1.7kg celery, 2kg carrots, 5kg American Sweets -- Original price: £35.05 -- Expected discounted price: £5.05
         */
        UUID carrotId = UUID.randomUUID();
        //£1
        WeighedProduct carrotsBag1 = new WeighedProduct(new BigDecimal("2"), carrotId, "CARROT");

        //£5.1
        UUID celeryUuid = UUID.randomUUID();
        WeighedProduct celery = new WeighedProduct(new BigDecimal("3"), celeryUuid, "CELERY");

//        WeighedProduct carrotsBag2 = new WeighedProduct(new BigDecimal("2"), carrotId, "CARROT");

        //24.95
        UUID sweetsId = UUID.randomUUID();
        WeighedProduct sweets = new WeighedProduct(new BigDecimal("4.99"), sweetsId, "SWEETS");

        List<Item> items = List.of(carrotsBag1.weighing(new BigDecimal("0.5")), carrotsBag1.weighing(new BigDecimal("2")),
                celery.weighing(new BigDecimal("1.7")), sweets.weighing(new BigDecimal("5")));
        final Basket basket = new Basket(mockedDiscounts);
        items.forEach(basket::add);

        BigDecimal expectedTotal = new BigDecimal("30.00");
        assertEquals(expectedTotal, basket.total());
    }

    @Test
    void ifLessThan1KiloVegShouldBeNoDiscount() {
        DiscountService mockedDiscounts = Mockito.mock(DiscountService.class);
        Mockito.when(mockedDiscounts.getDiscounts()).thenReturn(List.of(new BulkVegetableDiscount()));

        //Weight: 500g carrots, 200g celery -- Original price: £1.60 -- Expected discounted price: £0.00

        UUID carrotId = UUID.randomUUID();
        WeighedProduct carrotsBag1 = new WeighedProduct(new BigDecimal("2"), carrotId, "CARROT");

        UUID celeryUuid = UUID.randomUUID();
        WeighedProduct celery = new WeighedProduct(new BigDecimal("3"), celeryUuid, "CELERY");

        List<Item> items = List.of(carrotsBag1.weighing(new BigDecimal("0.5")), celery.weighing(new BigDecimal("0.2")));
        final Basket basket = new Basket(mockedDiscounts);
        items.forEach(basket::add);

        BigDecimal expectedPrice = new BigDecimal("1.60");
        assertEquals(basket.total(), expectedPrice);
    }

    static Stream<Arguments> basketProvidesTotalValue() {
        return Stream.of(
                noItems(),
                aSingleItemPricedPerUnit(),
                multipleItemsPricedPerUnit(),
                aSingleItemPricedByWeight(),
                multipleItemsPricedByWeight()
        );
    }

    private static Arguments aSingleItemPricedByWeight() {
        return Arguments.of("a single weighed item", "1.25", Collections.singleton(twoFiftyGramsOfAmericanSweets()));
    }

    private static Arguments multipleItemsPricedByWeight() {
        return Arguments.of("multiple weighed items", "1.85",
                Arrays.asList(twoFiftyGramsOfAmericanSweets(), twoHundredGramsOfPickAndMix())
        );
    }

    private static Arguments multipleItemsPricedPerUnit() {
        return Arguments.of("multiple items priced per unit", "2.04",
                Arrays.asList(aPackOfDigestives(), aPintOfMilk()));
    }

    private static Arguments aSingleItemPricedPerUnit() {
        return Arguments.of("a single item priced per unit", "0.49", Collections.singleton(aPintOfMilk()));
    }

    private static Arguments noItems() {
        return Arguments.of("no items", "0.00", Collections.emptyList());
    }

    private static Item aPintOfMilk() {
        return new Product(new BigDecimal("0.49"), UUID.randomUUID(), "name").oneOf();
    }

    private static Item aPackOfDigestives() {
        return new Product(new BigDecimal("1.55"), UUID.randomUUID(), "name").oneOf();
    }

    private static WeighedProduct aKiloOfAmericanSweets() {
        return new WeighedProduct(new BigDecimal("4.99"), UUID.randomUUID(), "name");
    }

    private static Item twoFiftyGramsOfAmericanSweets() {
        return aKiloOfAmericanSweets().weighing(new BigDecimal(".25"));
    }

    private static WeighedProduct aKiloOfPickAndMix() {
        return new WeighedProduct(new BigDecimal("2.99"), UUID.randomUUID(), "name");
    }

    private static Item twoHundredGramsOfPickAndMix() {
        return aKiloOfPickAndMix().weighing(new BigDecimal(".2"));
    }
}