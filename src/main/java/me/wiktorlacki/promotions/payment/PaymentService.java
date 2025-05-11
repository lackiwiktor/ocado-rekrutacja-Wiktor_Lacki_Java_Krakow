package me.wiktorlacki.promotions.payment;

import me.wiktorlacki.promotions.Constants;
import me.wiktorlacki.promotions.SpendingReport;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service responsible for managing payment methods, their balances, and related operations.
 */
public class PaymentService {

    private final PaymentMethodsContainer paymentMethodsContainer;

    public PaymentService(PaymentMethodsContainer paymentMethodsContainer) {
        this.paymentMethodsContainer = paymentMethodsContainer;
    }

    /**
     * Retrieves a payment method by its unique identifier.
     *
     * @param name the identifier of the payment method
     * @return an {@link Optional} containing the {@link PaymentMethod} if found, or empty if not
     */
    public Optional<PaymentMethod> byName(String name) {
        return paymentMethodsContainer.byName(name);
    }

    /**
     * Retrieves the special points-based payment method.
     *
     * @return the {@link PaymentMethod} associated with {@link Constants#POINTS_METHOD}
     * @throws NullPointerException if the points method is not configured
     */
    public PaymentMethod points() {
        return paymentMethodsContainer.points();
    }

    /**
     * Returns an unmodifiable view of all available payment methods.
     *
     * @return all {@link PaymentMethod}s in this container
     */
    public Collection<PaymentMethod> all() {
        return paymentMethodsContainer.all();
    }

    /**
     * Retrieves the current balance for the given payment method.
     *
     * @param paymentMethod the payment method
     * @return the current balance
     */
    public BigDecimal balance(PaymentMethod paymentMethod) {
        return paymentMethodsContainer.balance(paymentMethod);
    }

    /**
     * Updates the balance for a specific payment method.
     *
     * @param paymentMethod the payment method to update
     * @param balance       the new balance to assign
     */
    public void balance(PaymentMethod paymentMethod, BigDecimal balance) {
        paymentMethodsContainer.balance(paymentMethod, balance);
    }

    /**
     * Deducts a specific amount from the given payment method's balance.
     *
     * @param paymentMethod the payment method to be debited
     * @param amount        the amount to deduct
     * @throws IllegalArgumentException if the current balance is less than the amount
     */
    public void take(PaymentMethod paymentMethod, BigDecimal amount) {
        final var balance = balance(paymentMethod);
        if (balance.compareTo(amount) < 0)
            throw new IllegalArgumentException("Amount must be less than or equal to " + balance);
        balance(paymentMethod, balance.subtract(amount));
    }

    /**
     * Finds the payment method (excluding points) from a given list that has a balance greater than or equal
     * to the specified value and provides the best discount.
     *
     * @param methodList the list of payment methods to consider
     * @param value      the minimum required balance
     * @return an Optional containing the best matching payment method, or empty if none found
     */
    public Optional<PaymentMethod> findBestDiscountPaymentMethodWithBalanceGreaterThan(List<PaymentMethod> methodList, BigDecimal value) {
        return methodList
                .stream()
                .filter(it -> it != points() && balance(it).compareTo(value) >= 0)
                .max(Comparator.comparing(PaymentMethod::discount));
    }

    /**
     * Finds any payment method (excluding points) that has a balance greater than or equal to the specified value.
     *
     * @param value the minimum required balance
     * @return an Optional containing a matching payment method, or empty if none found
     */
    public Optional<PaymentMethod> findPaymentMethodWithBalanceGreaterThan(BigDecimal value) {
        return all()
                .stream()
                .filter(it -> it != points() && balance(it).compareTo(value) >= 0)
                .findFirst();
    }

    /**
     * Generates a spending report based on the limits and current balances of all payment methods.
     * The report shows how much has been spent from each method.
     *
     * @return a SpendingReport summarizing spending per payment method
     */
    public SpendingReport generateReport() {
        return new SpendingReport(
                paymentMethodsContainer.all()
                        .stream()
                        .collect(Collectors.toMap(k -> k, k -> k.limit().subtract(balance(k))))
        );
    }
}

