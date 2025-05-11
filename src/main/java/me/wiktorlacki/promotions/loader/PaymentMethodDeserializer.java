package me.wiktorlacki.promotions.loader;

import com.google.gson.*;
import me.wiktorlacki.promotions.payment.PaymentMethod;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class PaymentMethodDeserializer implements JsonDeserializer<PaymentMethod> {

    private static final BigDecimal HUNDRED = new BigDecimal(100);

    @Override
    public PaymentMethod deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();

        final var id = obj.get("id").getAsString();
        final var integerDiscount = obj.get("discount").getAsBigDecimal();
        final var decimalDiscount = integerDiscount.divide(HUNDRED, 4, RoundingMode.FLOOR);
        final var limit = obj.get("limit").getAsBigDecimal();

        return new PaymentMethod(id, decimalDiscount, limit);
    }
}
