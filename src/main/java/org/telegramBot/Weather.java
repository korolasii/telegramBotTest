package org.telegramBot;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

public class Weather {
    public static String getWeather(String city) {
        String cityName = city;
        String apiKey = "4463788bbac8dee7049e375dc33b9924";
        String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=" + cityName + "&appid=" + apiKey;

        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(apiUrl))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                String weatherData = response.body();
                Map<String, Object> jsonMap = parseJsonToMap(weatherData);
                String description = ((Map<String, Object>) ((List<Object>) jsonMap.get("weather")).get(0)).get("description").toString();
                double temperature = Math.round((Double.parseDouble(((Map<String, Object>) jsonMap.get("main")).get("temp").toString())-273.15)*10.0) / 10.0;

                return Double.toString(temperature) + "°C" + "\n" + description;

            } else {
                return "Ошибка";
            }
        } catch (URISyntaxException | IOException | InterruptedException e) {
            e.printStackTrace();
            return "Ошибка";
        }
    }

    private static Map<String, Object> parseJsonToMap(String json) {
        Type type = new TypeToken<Map<String, Object>>(){}.getType();
        return new Gson().fromJson(json, type);
    }
}
