package com.conversormoneda;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Scanner;

public class Conversor {
    private static final DecimalFormat df = new DecimalFormat("#,##0.00");
    private final ClaseAPI api;
    private final Scanner scanner;

    // Corrección: El constructor debe tener el mismo nombre que la clase
    public Conversor() {
        this.api = new ClaseAPI();
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("CONVERSOR DE MONEDAS");
        System.out.println("-------------------------------------");

        try {
            // Mostrar menú de monedas
            System.out.println("\nMonedas disponibles:");
            System.out.println("1. ARS - Peso argentino");
            System.out.println("2. BOB - Boliviano boliviano");
            System.out.println("3. BRL - Real brasileño");
            System.out.println("4. CLP - Peso chileno");
            System.out.println("5. COP - Peso colombiano");
            System.out.println("6. USD - Dólar estadounidense");

            // Obtener moneda base
            System.out.print("\nSelecciona el número de tu moneda base: ");
            String baseCurrency = getCurrencyByNumber(scanner.nextInt());
            scanner.nextLine(); // Limpiar buffer

            // Obtener cantidad a convertir
            System.out.print("Ingresa la cantidad a convertir: ");
            double amount = scanner.nextDouble();
            scanner.nextLine(); // Limpiar buffer

            // Obtener moneda objetivo
            System.out.print("Selecciona el número de la moneda objetivo: ");
            String targetCurrency = getCurrencyByNumber(scanner.nextInt());
            scanner.nextLine(); // Limpiar buffer

            // Obtener tasas de cambio
            ClaseAPI.apiResponse response = api.getExchangeRates(baseCurrency);

            if ("success".equalsIgnoreCase(response.getResult())) {
                double rate = getConversionRate(response.getConversionRates(), targetCurrency);
                double convertedAmount = amount * rate;

                System.out.println("\nResultado:");
                System.out.println(df.format(amount) + " " + baseCurrency + " = " +
                        df.format(convertedAmount) + " " + targetCurrency);
            } else {
                System.out.println("Error al obtener las tasas de cambio: " + response.getResult());
            }

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Ocurrió un error inesperado: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }

    private String getCurrencyByNumber(int number) {
        switch (number) {
            case 1: return "ARS";
            case 2: return "BOB";
            case 3: return "BRL";
            case 4: return "CLP";
            case 5: return "COP";
            case 6: return "USD";
            default: throw new IllegalArgumentException("Número de moneda no válido");
        }
    }

    private double getConversionRate(ClaseAPI.apiResponse.ConversionRates rates, String targetCurrency) {
        switch (targetCurrency) {
            case "USD": return rates.getUSD();
            case "ARS": return rates.getARS();
            case "BOB": return rates.getBOB();
            case "BRL": return rates.getBRL();
            case "CLP": return rates.getCLP();
            case "COP": return rates.getCOP();
            default: throw new IllegalArgumentException("Moneda no soportada: " + targetCurrency);
        }
    }
}