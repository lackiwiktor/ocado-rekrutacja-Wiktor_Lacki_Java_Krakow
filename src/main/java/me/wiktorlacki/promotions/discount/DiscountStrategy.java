package me.wiktorlacki.promotions.discount;

import me.wiktorlacki.promotions.offer.Offer;
import me.wiktorlacki.promotions.Order;

import java.util.Optional;

public interface DiscountStrategy {

    /**
     * Applies a discount strategy to the given order and generates an offer if applicable.
     *
     * @param order the order to which the discount strategy should be applied
     * @return an Optional containing the generated Offer if the strategy conditions are met,
     *         or an empty Optional if no valid offer can be created
     */
    Optional<Offer> apply(Order order);
}
