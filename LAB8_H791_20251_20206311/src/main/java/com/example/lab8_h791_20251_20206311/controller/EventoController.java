package com.example.lab8_h791_20251_20206311.controller;

import com.example.lab8_h791_20251_20206311.entity.Evento;
import com.example.lab8_h791_20251_20206311.service.EventoService;
import com.example.lab8_h791_20251_20206311.service.WeatherApiService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/eventos")
public class EventoController {

    private final WeatherApiService weatherApiService;
    private final EventoService eventoService;

    public EventoController(WeatherApiService weatherApiService, EventoService eventoService) {
        this.weatherApiService = weatherApiService;
        this.eventoService = eventoService;
    }

    @GetMapping("/{ciudad}")
    public ResponseEntity<List<Map<String, Object>>> getEventosDeportivos(
            @PathVariable String ciudad) {
        List<Map<String, Object>> eventos = weatherApiService.getEventosDeportivos(ciudad);
        return ResponseEntity.ok(eventos);
    }

    @GetMapping("/{ciudad}/pronostico/{fecha}")
    public ResponseEntity<Map<String, Object>> getPronosticoClima(
            @PathVariable String ciudad,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {

        Map<String, Object> pronostico = weatherApiService.getPronosticoClima(ciudad, fecha);
        if (pronostico != null) {
            return ResponseEntity.ok(pronostico);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Evento> registrarEvento(@RequestBody Evento evento) {
        Evento guardado = eventoService.guardarEvento(evento);
        return ResponseEntity.ok(guardado);
    }
}
