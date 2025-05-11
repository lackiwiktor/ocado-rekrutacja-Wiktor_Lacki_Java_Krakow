package me.wiktorlacki.promotions.offer;

import lombok.RequiredArgsConstructor;
import me.wiktorlacki.promotions.payment.PaymentService;

import java.util.Comparator;

@RequiredArgsConstructor
public class OfferComparator implements Comparator<Offer> {

    private final PaymentService paymentService;

    /**
     * Compares two {@link Offer} instances based on the total price and points usage.
     * The comparison is done in the following order:
     *  First, compare the discounted prices of the offers. Smaller is better.
     *  If the prices are equal, compare the amount of points used in the offer:
     *      Prefer the offer with greater amount of points used for payment.
     *      If one offer does not use points, it is considered worse.
     *
     * @param o1 the first offer to compare
     * @param o2 the second offer to compare
     * @return the result of comparison of o1 and o2
     */
    @Override
    public int compare(Offer o1, Offer o2) {
        final var comparison = o1.price().compareTo(o2.price());
        if (comparison != 0) {
            return comparison;
        }

        final var points1 = o1.payments().get(paymentService.points());
        final var points2 = o2.payments().get(paymentService.points());

        if (points1 != null && points2 != null) {
            return points2.compareTo(points1);
        }

        if (points1 == null) {
            return 1;
        }

        if (points2 == null) {
            return -1;
        }

        return 0;
    }
}
