package me.wiktorlacki.promotions;

import java.math.BigDecimal;

public class Constants {

    /**
     * The discount rate applied when a qualifying portion of an order is paid using points.
     * Represented as a decimal (e.g. 0.1 = 10% discount).
     */
    public static final BigDecimal PARTIAL_POINTS_PAYMENT_DISCOUNT = new BigDecimal("0.1");

    /**
     * The minimum portion (as a decimal) of an order that must be payable with points
     * to qualify for a partial points-based discount.
     * Represented as a decimal (e.g. 0.1 = 10% of an order must be paid with points).
     */
    public static final BigDecimal PARTIAL_POINTS_DISCOUNT_THRESHOLD = new BigDecimal("0.1");

    /**
     * The identifier used to refer to the points-based payment method.
     */
    public static final String POINTS_METHOD = "PUNKTY";
}
