package me.wiktorlacki.promotions;

import me.wiktorlacki.promotions.payment.PaymentMethod;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Represents a spending report that breaks down total spending per payment method.
 *
 * This record holds a mapping between {@link PaymentMethod} instances and the
 * amounts spent using each method.
 *
 * @param values a map of payment methods to the total amount spent using each
 */
public record SpendingReport(Map<PaymentMethod, BigDecimal> values) {

    @Override
    public String toString() {
        final var sb = new StringBuilder();
        values.forEach((method, amount) ->
                sb.append(method.id())
                        .append(": ")
                        .append(amount.toPlainString())
                        .append("\n"));

        return sb.toString().trim();
    }
}
