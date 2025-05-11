package me.wiktorlacki.promotions;

import java.math.BigDecimal;
import java.util.List;

/**
 * Represents an order with its unique identifier, total value, and associated promotions.
 * The {@link Order} object is used to capture details of a customer's purchase, including the order
 * amount and any promotions or discounts that may apply.
 *
 * @param id        the unique identifier for the order
 * @param value     the total value of the order, before any discounts are applied
 * @param promotions a list of promotion identifiers applicable to the order, if any
 */
public record Order(String id, BigDecimal value, List<String> promotions) { }
