package me.wiktorlacki.promotions.offer;

import me.wiktorlacki.promotions.payment.PaymentMethod;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Represents a finalized discount offer for an order, including the total discounted price,
 * the type of discount applied, and a breakdown of payments by payment method.
 *
 * @param type     the identifier of the discount strategy applied
 * @param price    the total discounted price of the order
 * @param payments a map of {@link PaymentMethod} to the amount each method will pay
 */
public record Offer(String type, BigDecimal price, Map<PaymentMethod, BigDecimal> payments) {

    public static Offer make(String type, BigDecimal price, Map<PaymentMethod, BigDecimal> payments) {
        return new Offer(type, price, payments);
    }

}
