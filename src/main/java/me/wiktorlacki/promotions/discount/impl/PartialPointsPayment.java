package me.wiktorlacki.promotions.discount.impl;

import lombok.RequiredArgsConstructor;
import me.wiktorlacki.promotions.Constants;
import me.wiktorlacki.promotions.offer.Offer;
import me.wiktorlacki.promotions.Order;
import me.wiktorlacki.promotions.discount.DiscountStrategy;
import me.wiktorlacki.promotions.payment.PaymentService;

import java.math.RoundingMode;
import java.util.Map;
import java.util.Optional;

/**
 * A {@link DiscountStrategy} that applies a discount when a portion of the order is paid using points.
 *
 * This strategy checks whether the user has enough points to meet a minimum percentage threshold
 * of the order value (defined by {@link Constants#PARTIAL_POINTS_DISCOUNT_THRESHOLD}).
 * If eligible, a partial discount (defined by {@link Constants#PARTIAL_POINTS_PAYMENT_DISCOUNT}) is applied.
 * The remaining amount is covered by another available payment method with sufficient balance.
 */
@RequiredArgsConstructor
public class PartialPointsPayment implements DiscountStrategy {

    private final PaymentService paymentService;

    @Override
    public Optional<Offer> apply(Order order) {
        final var pointsMethod = paymentService.points();
        final var points = paymentService.balance(pointsMethod);
        final var percentage = points.divide(order.value(), RoundingMode.DOWN);

        if (percentage.compareTo(Constants.PARTIAL_POINTS_DISCOUNT_THRESHOLD) < 0) return Optional.empty();

        final var discount = order.value().multiply(Constants.PARTIAL_POINTS_PAYMENT_DISCOUNT);
        final var discountedPrice = order.value().subtract(discount);
        final var pointsSpent = order.value().multiply(Constants.PARTIAL_POINTS_PAYMENT_DISCOUNT);
        final var priceLeft = discountedPrice.subtract(pointsSpent);
        final var remainingPaymentMethod = paymentService.findPaymentMethodWithBalanceGreaterThan(priceLeft);

        if (remainingPaymentMethod.isEmpty()) return Optional.empty();

        final var offer = Offer.make("PARTIAL_POINTS", discountedPrice,
                Map.of(pointsMethod, pointsSpent,
                        remainingPaymentMethod.get(), priceLeft));

        return Optional.of(offer);
    }
}
