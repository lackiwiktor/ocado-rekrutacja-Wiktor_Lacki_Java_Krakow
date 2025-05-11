import me.wiktorlacki.promotions.Constants;
import me.wiktorlacki.promotions.Order;
import me.wiktorlacki.promotions.discount.DiscountProcessor;
import me.wiktorlacki.promotions.discount.impl.FullCardPayment;
import me.wiktorlacki.promotions.discount.impl.FullPointsPayment;
import me.wiktorlacki.promotions.discount.impl.PartialPointsPayment;
import me.wiktorlacki.promotions.payment.PaymentMethod;
import me.wiktorlacki.promotions.payment.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DiscountProcessorTests {

    private PaymentService paymentService;

    @BeforeEach
    public void setUp() {
        paymentService = mock(PaymentService.class);
    }

    @Test
    @DisplayName("Test whether a DiscountProcessor will return empty Optional if no payment method has sufficient balance")
    public void testInsufficientBalance() {
        final var discountProcessor = new DiscountProcessor(paymentService,
                List.of(new FullCardPayment(paymentService),
                        new PartialPointsPayment(paymentService),
                        new FullPointsPayment(paymentService)
                ));
        when(paymentService.points()).thenReturn(new PaymentMethod(Constants.POINTS_METHOD, BigDecimal.ZERO, BigDecimal.ZERO));
        when(paymentService.balance(any())).thenReturn(BigDecimal.ZERO);

        final var order = new Order("1", BigDecimal.ONE, Collections.emptyList());

        final var offer = discountProcessor.process(order);

        assertThat(offer).isEmpty();
    }

    @Test
    @DisplayName("Test whether a DiscountProcessor will return Optional with offer if any payment method has sufficient balance")
    public void testSufficientBalance() {
        final var discountProcessor = new DiscountProcessor(paymentService,
                List.of(new FullCardPayment(paymentService),
                        new PartialPointsPayment(paymentService),
                        new FullPointsPayment(paymentService)
                ));
        when(paymentService.points()).thenReturn(new PaymentMethod(Constants.POINTS_METHOD, BigDecimal.ZERO, BigDecimal.ZERO));
        when(paymentService.balance(any())).thenReturn(BigDecimal.ONE);

        final var order = new Order("1", BigDecimal.ONE, Collections.emptyList());

        final var offer = discountProcessor.process(order);

        assertThat(offer).isNotEmpty();
    }
}
