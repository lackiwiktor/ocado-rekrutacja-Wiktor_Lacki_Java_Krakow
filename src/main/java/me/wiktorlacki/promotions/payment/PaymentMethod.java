package me.wiktorlacki.promotions.payment;

import java.math.BigDecimal;

/**
 * Represents a payment method, including any applicable discount
 * and the balance available for use.
 *
 * @param id       the unique identifier of the payment method
 * @param discount the discount rate offered by this payment method (e.g. 0.1 for 10% off)
 * @param limit    spending limit available for this payment method
 */
public record PaymentMethod(String id, BigDecimal discount, BigDecimal limit) { }