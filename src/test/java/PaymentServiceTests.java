import me.wiktorlacki.promotions.Constants;
import me.wiktorlacki.promotions.payment.PaymentMethod;
import me.wiktorlacki.promotions.payment.PaymentMethodsContainer;
import me.wiktorlacki.promotions.payment.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

public class PaymentServiceTests {

    private PaymentMethodsContainer paymentMethodsContainer;
    private PaymentService paymentService;

    @BeforeEach
    public void setUp() {
        paymentMethodsContainer = mock(PaymentMethodsContainer.class);
        paymentService = new PaymentService(paymentMethodsContainer);
    }

    @Test
    @DisplayName("Test whether a proper payment method is returned by name")
    public void testPaymentMethodReturnedByName() {
        final var name = "VISA";
        final var paymentMethod = new PaymentMethod(name, BigDecimal.ZERO, BigDecimal.ZERO);
        when(paymentMethodsContainer.byName(name)).thenReturn(Optional.of(paymentMethod));

        final var methodByName = paymentService.byName(name);

        assertThat(methodByName).hasValue(paymentMethod);
    }

    @Test
    @DisplayName("Test whether a points payment method is properly returned")
    public void testPointsMethodProperlyReturned() {
        final var name = Constants.POINTS_METHOD;
        final var paymentMethod = new PaymentMethod(name, BigDecimal.ZERO, BigDecimal.ZERO);
        when(paymentMethodsContainer.points()).thenReturn(paymentMethod);

        final var pointsMethod = paymentService.points();

        assertThat(pointsMethod).isEqualTo(paymentMethod);
    }

    @Test
    @DisplayName("Test whether a all payment methods are properly returned")
    public void testAllMethodsProperlyReturned() {
        final var paymentMethod1 = new PaymentMethod("VISA", BigDecimal.ZERO, BigDecimal.ZERO);
        final var paymentMethod2 = new PaymentMethod("MASTERCARD", BigDecimal.ZERO, BigDecimal.ZERO);
        when(paymentMethodsContainer.all()).thenReturn(List.of(paymentMethod1, paymentMethod2));

        final var allMethods = paymentService.all();

        assertThat(allMethods.containsAll(List.of(paymentMethod1, paymentMethod2))).isTrue();
    }

    @Test
    @DisplayName("Test whether payment method's balance is properly returned")
    public void testPaymentMethodBalanceProperlyReturned() {
        final var paymentMethod1 = new PaymentMethod("VISA", BigDecimal.ZERO, BigDecimal.ZERO);
        when(paymentMethodsContainer.balance(paymentMethod1)).thenReturn(paymentMethod1.limit());

        final var balance = paymentService.balance(paymentMethod1);

        assertThat(balance).isEqualTo(paymentMethod1.limit());
    }
}
