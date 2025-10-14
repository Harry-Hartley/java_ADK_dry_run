package vn.cloud.java_ADK_dry_run;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.util.Scanner;

public class newAndImprovedAPI {
    private static Scanner user_input = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            String API_KEY = System.getenv("polygon_api_key");
            String ticker = "AAPL";

            System.out.println("----------------------");
            System.out.println("1 - View stock information");
            System.out.println("2 - View Moving Average Convergence/Divergence");
            System.out.println("----------------------");
            int menu_option = user_input.nextInt();

            switch(menu_option) {
                case 1 -> ticker_info();
                case 2 -> Moving_Average_Convergence();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            user_input.close();
        }
    }

    private static void Moving_Average_Convergence() {
        try {
            String ticker = "AAPL";
            String API_KEY = System.getenv("polygon_api_key");
            String moving_avg_url = "https://api.polygon.io/v1/indicators/macd/" + ticker +
                    "?timespan=day&adjusted=true&short_window=12&long_window=26&signal_window=9" +
                    "&series_type=close&order=desc&limit=10&apiKey=" + API_KEY;

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(moving_avg_url))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());

            JSONParser parser = new JSONParser();
            JSONObject jsonResponse = (JSONObject) parser.parse(response.body());

            JSONObject results = (JSONObject) jsonResponse.get("results");
            JSONArray values = (JSONArray) results.get("values");

            if (values != null && !values.isEmpty()) {
                JSONObject firstValue = (JSONObject) values.get(0);
                System.out.println("\n=== MACD Data ===");
                System.out.println("Value: " + firstValue.get("value"));
                System.out.println("Signal: " + firstValue.get("signal"));
                System.out.println("Histogram: " + firstValue.get("histogram"));
                System.out.println("Timestamp: " + firstValue.get("timestamp"));
            } else {
                System.out.println("No MACD data available");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void ticker_info() {
        try {
            String ticker = "AAPL";
            String API_KEY = System.getenv("polygon_api_key");
            String url = "https://api.polygon.io/v3/reference/tickers/" + ticker + "?apiKey=" + API_KEY;

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());

            JSONParser parser = new JSONParser();
            JSONObject jsonResponse = (JSONObject) parser.parse(response.body());

            JSONObject results = (JSONObject) jsonResponse.get("results");

            if (results != null) {
                System.out.println("\n=== Stock Information ===");
                System.out.println("Ticker: " + results.get("ticker"));
                System.out.println("Name: " + results.get("name"));
                System.out.println("Market: " + results.get("market"));
                System.out.println("Locale: " + results.get("locale"));
                System.out.println("Primary Exchange: " + results.get("primary_exchange"));
                System.out.println("Type: " + results.get("type"));
                System.out.println("Active: " + results.get("active"));
                System.out.println("Currency: " + results.get("currency_name"));
            } else {
                System.out.println("No ticker information available");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}