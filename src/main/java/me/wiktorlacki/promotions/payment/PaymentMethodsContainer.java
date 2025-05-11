package me.wiktorlacki.promotions.payment;

import me.wiktorlacki.promotions.Constants;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Container class for managing available {@link PaymentMethod}s and their respective balances.
 */
public class PaymentMethodsContainer {

    private final Map<String, PaymentMethod> paymentMethods;
    private final Map<PaymentMethod, BigDecimal> balances;

    public PaymentMethodsContainer(List<PaymentMethod> paymentMethods) {
        this.paymentMethods = paymentMethods.stream()
                .collect(Collectors.toMap(PaymentMethod::id, k -> k));
        this.balances = paymentMethods.stream()
                .collect(Collectors.toMap(k -> k, PaymentMethod::limit));
    }

    /**
     * Retrieves a payment method by its unique identifier.
     *
     * @param name the identifier of the payment method
     * @return an {@link Optional} containing the {@link PaymentMethod} if found, or empty if not
     */
    public Optional<PaymentMethod> byName(String name) {
        return Optional.ofNullable(paymentMethods.get(name));
    }

    /**
     * Retrieves the special points-based payment method.
     *
     * @return the {@link PaymentMethod} associated with {@link Constants#POINTS_METHOD}
     * @throws NullPointerException if the points method is not configured
     */
    public PaymentMethod points() {
        return Objects.requireNonNull(paymentMethods.get(Constants.POINTS_METHOD));
    }

    /**
     * Returns an unmodifiable view of all available payment methods.
     *
     * @return all {@link PaymentMethod}s in this container
     */
    public Collection<PaymentMethod> all() {
        return Collections.unmodifiableCollection(paymentMethods.values());
    }

    /**
     * Returns the current available balance for a specific payment method.
     *
     * @param paymentMethod the payment method to query
     * @return the remaining balance
     * @throws NullPointerException if the payment method has no recorded balance
     */
    public BigDecimal balance(PaymentMethod paymentMethod) {
        return Objects.requireNonNull(balances.get(paymentMethod));
    }

    /**
     * Updates the balance for a specific payment method.
     *
     * @param paymentMethod the payment method to update
     * @param balance       the new balance to assign
     */
    public void balance(PaymentMethod paymentMethod, BigDecimal balance) {
        balances.put(paymentMethod, balance);
    }
}
