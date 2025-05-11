package me.wiktorlacki.promotions;

import java.io.IOException;

public class Main {

    /**
     * Main entry point of the application that initializes the application with the file paths for orders and payment methods.
     *
     * The method expects two command-line arguments:
     *  ordersFilePath: Path to the file containing order data in JSON format.
     *  paymentMethodsFilePath: Path to the file containing available payment methods in JSON format.
     *
     * If either of these arguments is missing or invalid, the method prints the usage instructions and exits.
     */
    public static void main(String[] args) {
        if (args.length != 2) {
            printUsage();
            return;
        }

        final var ordersPath = args[0];
        final var paymentMethodsPath = args[1];

        if (ordersPath == null || paymentMethodsPath == null) {
            printUsage();
            return;
        }

        final var app = new Application(ordersPath, paymentMethodsPath);
        try {
            app.run();
        } catch (IOException e) {
            System.out.println("Failed to run: " + e.getMessage());
        }
    }

    private static void printUsage() {
        System.out.println("Usage: java -jar app.jar <ordersFilePath> <paymentMethodsFilePath>");
    }
}