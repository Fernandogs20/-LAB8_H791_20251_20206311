package com.example.lab8_h791_20251_20206311.service;

import com.example.lab8_h791_20251_20206311.entity.Evento;
import com.example.lab8_h791_20251_20206311.repository.EventoRepository;
import org.springframework.stereotype.Service;

@Service
public class EventoService {

    private final EventoRepository eventoRepository;

    public EventoService(EventoRepository eventoRepository) {
        this.eventoRepository = eventoRepository;
    }

    public Evento guardarEvento(Evento evento) {
        return eventoRepository.save(evento);
    }
}