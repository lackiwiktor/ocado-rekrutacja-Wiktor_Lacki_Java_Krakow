package me.wiktorlacki.promotions.discount.impl;

import lombok.RequiredArgsConstructor;
import me.wiktorlacki.promotions.offer.Offer;
import me.wiktorlacki.promotions.Order;
import me.wiktorlacki.promotions.discount.DiscountStrategy;
import me.wiktorlacki.promotions.payment.PaymentService;

import java.util.Map;
import java.util.Optional;

/**
 * A {@link DiscountStrategy} that applies a discount when the entire order is paid using points.
 *
 * This strategy checks if the points payment method has sufficient balance to cover the full discounted
 * order value. If so, it generates an {@link Offer} using only points as the payment method.
 */
@RequiredArgsConstructor
public class FullPointsPayment implements DiscountStrategy {

    private final PaymentService paymentService;

    @Override
    public Optional<Offer> apply(Order order) {
        final var pointsMethod = paymentService.points();
        final var discount = pointsMethod.discount();
        final var discountedPrice = order.value().subtract(order.value().multiply(discount));
        final var pointsBalance = paymentService.balance(pointsMethod);

        if (pointsBalance.compareTo(discountedPrice) < 0) return Optional.empty();

        final var offer = Offer.make("FULL_POINTS", discountedPrice, Map.of(pointsMethod, discountedPrice));
        return Optional.of(offer);
    }
}
