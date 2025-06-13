package com.example.lab8_h791_20251_20206311.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class WeatherApiService {

    @Value("${weatherapi.key}") 
    private String apiKey;

    private static final String SPORTS_URL = "https://api.weatherapi.com/v1/sports.json";
    private static final String FORECAST_URL = "https://api.weatherapi.com/v1/forecast.json";

    private final RestTemplate restTemplate;

    public WeatherApiService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }


    public List<Map<String, Object>> getEventosDeportivos(String ciudad) {
        String url = SPORTS_URL + "?key=" + apiKey + "&q=" + ciudad;

        try {
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            if (response == null) return Collections.emptyList();

            List<Map<String, Object>> todosEventos = new ArrayList<>();
            todosEventos.addAll(obtenerLista(response, "football"));
            todosEventos.addAll(obtenerLista(response, "cricket"));
            todosEventos.addAll(obtenerLista(response, "golf"));

            LocalDate hoy = LocalDate.now();
            LocalDate en7Dias = hoy.plusDays(7);

            return todosEventos.stream()
                    .filter(evento -> {
                        String start = (String) evento.get("start");
                        ZonedDateTime fechaEvento = ZonedDateTime.parse(start);
                        LocalDate fecha = fechaEvento.toLocalDate();
                        return !fecha.isBefore(hoy) && fecha.isBefore(en7Dias);
                    })
                    .collect(Collectors.toList());

        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    private List<Map<String, Object>> obtenerLista(Map<String, Object> response, String key) {
        Object lista = response.get(key);
        if (lista instanceof List) {
            return (List<Map<String, Object>>) lista;
        } else {
            return Collections.emptyList();
        }
    }

    public Map<String, Object> getPronosticoClima(String ciudad, LocalDate fecha) {
        String url = FORECAST_URL + "?key=" + apiKey + "&q=" + ciudad + "&days=7";

        try {
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            if (response == null) return null;

            Map<String, Object> forecast = (Map<String, Object>) response.get("forecast");
            List<Map<String, Object>> forecastDays = (List<Map<String, Object>>) forecast.get("forecastday");

            for (Map<String, Object> day : forecastDays) {
                if (fecha.equals(LocalDate.parse((String) day.get("date")))) {
                    Map<String, Object> dayData = (Map<String, Object>) day.get("day");
                    Map<String, Object> condition = (Map<String, Object>) dayData.get("condition");

                    Map<String, Object> pronostico = new HashMap<>();
                    pronostico.put("condicion", condition.get("text"));
                    pronostico.put("temp_max", dayData.get("maxtemp_c"));
                    pronostico.put("temp_min", dayData.get("mintemp_c"));

                    return pronostico;
                }
            }

            return null;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
