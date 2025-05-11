package me.wiktorlacki.promotions.discount.impl;

import lombok.RequiredArgsConstructor;
import me.wiktorlacki.promotions.offer.Offer;
import me.wiktorlacki.promotions.Order;
import me.wiktorlacki.promotions.discount.DiscountStrategy;
import me.wiktorlacki.promotions.payment.PaymentMethod;
import me.wiktorlacki.promotions.payment.PaymentService;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

/**
 * A {@link DiscountStrategy} implementation that applies a discount when the entire order is paid
 * using a single payment method with sufficient balance.
 *
 * If the order includes eligible promotions, this strategy prioritizes payment methods that offer
 * a promotional discount. If none are found, it falls back to any available payment method
 * with a sufficient balance to cover the full order amount.
 */
@RequiredArgsConstructor
public class FullCardPayment implements DiscountStrategy {

    private final PaymentService paymentService;


    @Override
    public Optional<Offer> apply(Order order) {
        Optional<PaymentMethod> optionalPaymentMethod;
        var discount = BigDecimal.ZERO;
        if (order.promotions() == null) {
            optionalPaymentMethod = paymentService.findPaymentMethodWithBalanceGreaterThan(order.value());
        } else {
            final var promotions = order.promotions()
                    .stream()
                    .map(paymentService::byName)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .toList();

            optionalPaymentMethod = paymentService.findBestDiscountPaymentMethodWithBalanceGreaterThan(promotions, order.value())
                    .or(() -> paymentService.findPaymentMethodWithBalanceGreaterThan(order.value()));
        }

        if (optionalPaymentMethod.isEmpty()) return Optional.empty();

        final var paymentMethod = optionalPaymentMethod.get();
        if (order.promotions() != null && order.promotions().contains(paymentMethod.id())) {
            discount = paymentMethod.discount();
        }

        final var discountedPrice = order.value().subtract(order.value().multiply(discount));
        final var offer = Offer.make("FULL_CARD", discountedPrice, Map.of(paymentMethod, discountedPrice));

        return Optional.of(offer);
    }

}
