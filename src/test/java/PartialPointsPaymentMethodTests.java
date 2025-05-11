import me.wiktorlacki.promotions.Constants;
import me.wiktorlacki.promotions.Order;
import me.wiktorlacki.promotions.discount.impl.PartialPointsPayment;
import me.wiktorlacki.promotions.payment.PaymentMethod;
import me.wiktorlacki.promotions.payment.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PartialPointsPaymentMethodTests {

    private PaymentService paymentService;

    @BeforeEach
    void resetPaymentService() {
        paymentService = mock(PaymentService.class);
    }

    @Test
    @DisplayName("Test whether an Offer can be made for given Order with insufficient amount of balance on points payment method")
    public void testInsufficientBalance() {
        final var order = new Order("1", BigDecimal.TEN, Collections.emptyList());
        final var pointsMethod = new PaymentMethod(Constants.POINTS_METHOD, new BigDecimal("0.1"), BigDecimal.ZERO);
        when(paymentService.points()).thenReturn(pointsMethod);
        when(paymentService.balance(pointsMethod)).thenReturn(pointsMethod.limit());
        final var offer = new PartialPointsPayment(paymentService).apply(order);

        assertThat(offer).isEmpty();
    }

    @Test
    @DisplayName("Test whether an Offer can be made for given Order with sufficient amount of balance on points payment method")
    public void testSufficientBalance() {
        final var order = new Order("1", BigDecimal.TEN, Collections.emptyList());
        final var pointsMethod = new PaymentMethod(Constants.POINTS_METHOD, new BigDecimal("0.1"), order.value().multiply(Constants.PARTIAL_POINTS_DISCOUNT_THRESHOLD));
        final var otherPaymentMethod = new PaymentMethod("VISA", new BigDecimal("0.2"), BigDecimal.TEN);
        when(paymentService.points()).thenReturn(pointsMethod);
        when(paymentService.balance(pointsMethod)).thenReturn(pointsMethod.limit());
        when(paymentService.balance(otherPaymentMethod)).thenReturn(otherPaymentMethod.limit());
        when(paymentService.all()).thenReturn(List.of(pointsMethod, otherPaymentMethod));
        when(paymentService.findPaymentMethodWithBalanceGreaterThan(any())).thenReturn(Optional.of(otherPaymentMethod));

        final var offer = new PartialPointsPayment(paymentService).apply(order);

        assertThat(offer)
                .isPresent()
                .get()
                .satisfies(o -> {
                    assertThat(o.payments().containsKey(pointsMethod)).isTrue();
                    assertThat(o.payments().containsKey(otherPaymentMethod)).isTrue();
                });
    }

    @Test
    @DisplayName("Test whether a proper discount is applied if Order can be partially paid with points")
    public void testProperDiscount() {
        final var order = new Order("1", BigDecimal.TEN, Collections.emptyList());
        final var pointsMethod = new PaymentMethod(Constants.POINTS_METHOD, new BigDecimal("0.1"), order.value().multiply(Constants.PARTIAL_POINTS_DISCOUNT_THRESHOLD));
        final var otherPaymentMethod = new PaymentMethod("VISA", new BigDecimal("0.2"), BigDecimal.TEN);
        final var discountedPrice = order.value().subtract(order.value().multiply(Constants.PARTIAL_POINTS_PAYMENT_DISCOUNT));
        when(paymentService.points()).thenReturn(pointsMethod);
        when(paymentService.balance(pointsMethod)).thenReturn(pointsMethod.limit());
        when(paymentService.balance(otherPaymentMethod)).thenReturn(otherPaymentMethod.limit());
        when(paymentService.all()).thenReturn(List.of(pointsMethod, otherPaymentMethod));
        when(paymentService.findPaymentMethodWithBalanceGreaterThan(any())).thenReturn(Optional.of(otherPaymentMethod));

        final var offer = new PartialPointsPayment(paymentService).apply(order);

        assertThat(offer)
                .isPresent()
                .get()
                .satisfies(o -> {
                    assertThat(o.price()).isEqualTo(discountedPrice);
                });
    }

}
