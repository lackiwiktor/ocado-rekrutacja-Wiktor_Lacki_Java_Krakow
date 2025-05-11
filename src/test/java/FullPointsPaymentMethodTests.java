import me.wiktorlacki.promotions.Order;
import me.wiktorlacki.promotions.discount.impl.FullCardPayment;
import me.wiktorlacki.promotions.discount.impl.FullPointsPayment;
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

public class FullPointsPaymentMethodTests {

    private PaymentService paymentService;

    @BeforeEach
    void resetPaymentService() {
        paymentService = mock(PaymentService.class);
    }

    @Test
    @DisplayName("Test whether an Offer can be made for given Order with insufficient amount of balance on points payment method")
    public void testInsufficientBalance() {
        final var order = new Order("1", BigDecimal.TEN, Collections.emptyList());
        final var pointsMethod = new PaymentMethod("PUNKTY", new BigDecimal("0.1"), BigDecimal.ZERO);
        when(paymentService.points()).thenReturn(pointsMethod);
        when(paymentService.balance(pointsMethod)).thenReturn(pointsMethod.limit());

        final var offer = new FullPointsPayment(paymentService).apply(order);

        assertThat(offer).isEmpty();
    }

    @Test
    @DisplayName("Test whether an Offer can be made for given Order with sufficient amount of balance on points payment method")
    public void testSufficientBalance() {
        final var order = new Order("1", BigDecimal.TEN, Collections.emptyList());
        final var pointsMethod = new PaymentMethod("PUNKTY", new BigDecimal("0.1"), BigDecimal.TEN);
        when(paymentService.points()).thenReturn(pointsMethod);
        when(paymentService.balance(pointsMethod)).thenReturn(pointsMethod.limit());

        final var offer = new FullPointsPayment(paymentService).apply(order);

        assertThat(offer).isNotEmpty();
    }

    @Test
    @DisplayName("Test whether a proper discount is applied if Order can be fully paid with points")
    public void testCorrectPaymentMethodChoiceBasedOnDiscount() {
        final var order = new Order("1", BigDecimal.TEN, Collections.emptyList());
        final var pointsMethod = new PaymentMethod("PUNKTY", new BigDecimal("0.15"), BigDecimal.TEN);
        when(paymentService.points()).thenReturn(pointsMethod);
        when(paymentService.balance(pointsMethod)).thenReturn(pointsMethod.limit());

        final var offer = new FullPointsPayment(paymentService).apply(order);

        assertThat(offer)
                .isPresent()
                .get()
                .satisfies(o -> assertThat(o.payments().containsKey(pointsMethod)).isTrue());
    }

    @Test
    @DisplayName("Test whether a payment method without promotion will be chosen over a method with insufficient balance")
    public void testCorrectPaymentMethodChoiceBasedOnBalance() {
        final var worseDiscountMethod = new PaymentMethod("VISA", new BigDecimal("0.1"), BigDecimal.TEN);
        final var betterDiscountMethod = new PaymentMethod("MASTERCARD", new BigDecimal("0.25"), BigDecimal.ONE);
        final var order = new Order("1", BigDecimal.TEN, List.of(betterDiscountMethod.id()));
        when(paymentService.byName(worseDiscountMethod.id())).thenReturn(Optional.of(worseDiscountMethod));
        when(paymentService.byName(betterDiscountMethod.id())).thenReturn(Optional.of(betterDiscountMethod));
        when(paymentService.balance(worseDiscountMethod)).thenReturn(worseDiscountMethod.limit());
        when(paymentService.balance(betterDiscountMethod)).thenReturn(betterDiscountMethod.limit());
        when(paymentService.all()).thenReturn(List.of(worseDiscountMethod, betterDiscountMethod));
        when(paymentService.findPaymentMethodWithBalanceGreaterThan(any())).thenReturn(Optional.of(worseDiscountMethod));
        final var offer = new FullCardPayment(paymentService).apply(order);

        assertThat(offer)
                .isPresent()
                .get()
                .satisfies(o -> assertThat(o.payments().containsKey(worseDiscountMethod)).isTrue());
    }

}
