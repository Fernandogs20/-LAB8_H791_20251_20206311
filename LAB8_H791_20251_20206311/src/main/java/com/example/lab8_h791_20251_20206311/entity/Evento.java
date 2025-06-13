package com.example.lab8_h791_20251_20206311.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "evento")
@Data
public class Evento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_evento", nullable = false, length = 100)
    private String nombreEvento;

    @Column(nullable = false, length = 50)
    private String ciudad;

    @Column(name = "fecha_evento", nullable = false)
    private LocalDate fechaEvento;

    @Column(name = "condicion_climatica", nullable = false, length = 50)
    private String condicionClimatica;

    @Column(name = "temperatura_maxima", nullable = false)
    private Double temperaturaMaxima;

    @Column(name = "temperatura_minima", nullable = false)
    private Double temperaturaMinima;
}