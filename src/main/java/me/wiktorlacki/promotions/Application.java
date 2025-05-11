package me.wiktorlacki.promotions;

import lombok.RequiredArgsConstructor;
import me.wiktorlacki.promotions.discount.DiscountProcessor;
import me.wiktorlacki.promotions.discount.DiscountService;
import me.wiktorlacki.promotions.discount.impl.FullCardPayment;
import me.wiktorlacki.promotions.discount.impl.FullPointsPayment;
import me.wiktorlacki.promotions.discount.impl.PartialPointsPayment;
import me.wiktorlacki.promotions.loader.JSONLoader;
import me.wiktorlacki.promotions.payment.PaymentMethod;
import me.wiktorlacki.promotions.payment.PaymentMethodsContainer;
import me.wiktorlacki.promotions.payment.PaymentService;

import java.io.IOException;
import java.util.List;

/**
 * Main entry point for the application that loads order data and payment method data, processes the applicable discounts,
 * and prints a report of the final spending distribution across the available payment methods.
 */
@RequiredArgsConstructor
public class Application {

    private final String ordersPath;
    private final String paymentMethodsPath;

    /**
     * Runs the application, loading the orders and payment methods from the provided file paths,
     * processing the discounts, and printing the final spending report.
     *
     * @throws IOException If an I/O error occurs while reading the JSON files.
     */
    public void run() throws IOException {
        final var jsonLoader = new JSONLoader();
        final var orders = jsonLoader.loadOrders(ordersPath);
        final var paymentMethods = jsonLoader.loadPaymentMethods(paymentMethodsPath);
        final var discountService = buildDiscountService(paymentMethods);

        final var report = discountService.calculate(orders);
        System.out.println(report);
    }

    private DiscountService buildDiscountService(List<PaymentMethod> paymentMethods) {
        final var paymentService = new PaymentService(new PaymentMethodsContainer(paymentMethods));
        final var discountProcessor = new DiscountProcessor(paymentService, List.of(
                new FullPointsPayment(paymentService),
                new PartialPointsPayment(paymentService),
                new FullCardPayment(paymentService)
        ));

        return new DiscountService(
                discountProcessor,
                paymentService
        );
    }
}
