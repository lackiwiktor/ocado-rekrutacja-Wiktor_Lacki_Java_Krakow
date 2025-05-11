package me.wiktorlacki.promotions.loader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import me.wiktorlacki.promotions.Order;
import me.wiktorlacki.promotions.payment.PaymentMethod;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * A utility class for loading JSON data into Java objects using Gson.
 */
@RequiredArgsConstructor
public class JSONLoader {

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(PaymentMethod.class, new PaymentMethodDeserializer())
            .create();

    /**
     * Loads a list of {@link Order} objects from a JSON file.
     *
     * @param fileName the path to the JSON file
     * @return a list of orders
     * @throws IOException if the file cannot be read
     */
    public List<Order> loadOrders(String fileName) throws IOException {
        return loadList(fileName, Order[].class);
    }

    /**
     * Loads a list of {@link PaymentMethod} objects from a JSON file.
     *
     * @param fileName the path to the JSON file
     * @return a list of payment methods
     * @throws IOException if the file cannot be read
     */
    public List<PaymentMethod> loadPaymentMethods(String fileName) throws IOException {
        return loadList(fileName, PaymentMethod[].class);
    }

    private <T> List<T> loadList(String fileName, Class<T[]> type) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            T[] loadedArray = gson.fromJson(reader, type);
            return Arrays.asList(loadedArray);
        }
    }
}

