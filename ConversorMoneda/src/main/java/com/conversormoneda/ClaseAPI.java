package com.conversormoneda;

import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import java.io.IOException;

public class ClaseAPI {
    private static final String API_KEY = "f8277beb9b6c7274c043296b";
    private static final String BASE_URL = "https://v6.exchangerate-api.com/v6/";

    private final Gson gson = new Gson();

    public apiResponse getExchangeRates(String baseCurrency) throws IOException {
        String url = BASE_URL + API_KEY + "/latest/" + baseCurrency;

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    String result = EntityUtils.toString(entity);
                    return gson.fromJson(result, apiResponse.class);
                }
            }
        }
        throw new IOException("\nNo se pudo obtener informacion.\n");
    }

    public static class apiResponse {
        private String result;
        private String base_code;
        private String time_last_update_utc;
        private ConversionRates conversion_rates;

        public String getResult() { return result; }
        public String getBaseCode() { return base_code; }
        public String getTimeLastUpdateUtc() { return time_last_update_utc; }
        public ConversionRates getConversionRates() { return conversion_rates; }

        public static class ConversionRates {
            private double USD;
            private double ARS; // Peso argentino
            private double BOB; // Boliviano boliviano
            private double BRL; // Real brasileño
            private double CLP; // Peso chileno
            private double COP; // Peso colombiano

            // Getters.
            public double getUSD() { return USD; }
            public double getARS() { return ARS; }
            public double getBOB() { return BOB; }
            public double getBRL() { return BRL; }
            public double getCLP() { return CLP; }
            public double getCOP() { return COP; }
        }
    }
}