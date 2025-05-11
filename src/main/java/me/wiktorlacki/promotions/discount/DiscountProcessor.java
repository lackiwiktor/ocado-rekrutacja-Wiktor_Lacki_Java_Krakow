package me.wiktorlacki.promotions.discount;

import lombok.RequiredArgsConstructor;
import me.wiktorlacki.promotions.offer.Offer;
import me.wiktorlacki.promotions.offer.OfferComparator;
import me.wiktorlacki.promotions.Order;
import me.wiktorlacki.promotions.payment.PaymentService;

import java.util.List;
import java.util.Optional;

/**
 * Handles the evaluation and selection of the most beneficial discount strategy for a given order.
 */
@RequiredArgsConstructor
public class DiscountProcessor {

    private final PaymentService paymentService;
    private final List<DiscountStrategy> strategies;

    /**
     * Attempts to determine and return the best available offer for the given order.
     *
     * @param order the order for which the optimal discount should be evaluated
     * @return an Optional containing the most beneficial Offer if one is applicable,
     *         or an empty Optional if no suitable offer is found
     */
    public Optional<Offer> process(Order order) {
        return strategies.stream()
                .map(it -> it.apply(order))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .min(new OfferComparator(paymentService));
    }

}
