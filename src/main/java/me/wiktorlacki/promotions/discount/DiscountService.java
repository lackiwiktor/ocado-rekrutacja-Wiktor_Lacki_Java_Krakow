package me.wiktorlacki.promotions.discount;

import lombok.RequiredArgsConstructor;
import me.wiktorlacki.promotions.Order;
import me.wiktorlacki.promotions.payment.PaymentService;
import me.wiktorlacki.promotions.SpendingReport;

import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
public class DiscountService {

    private final DiscountProcessor discountProcessor;
    private final PaymentService paymentService;

    /**
     * Calculates the total spending report by applying discounts and processing payments for the given list of orders.
     *
     * @param orders the list of orders to process
     * @return a {@link SpendingReport} summarizing all payments made
     * @throws IllegalStateException if no valid discount strategy is found for an order
     */
    public SpendingReport calculate(List<Order> orders) {
        orders.sort(Comparator.comparing(Order::value));

        orders.stream()
                .sorted(Comparator.comparing(Order::value))
                .forEach(order -> {
                    final var discount = discountProcessor.process(order)
                            .orElseThrow(() -> new IllegalStateException("Could not find proper discount strategy for order: " + order));
                    discount.payments().forEach(paymentService::take);
                });

        return paymentService.generateReport();
    }

}
