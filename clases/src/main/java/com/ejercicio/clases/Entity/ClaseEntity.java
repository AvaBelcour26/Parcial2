package com.ejercicio.clases.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalTime;

@Entity
@Table(name = "clases")
@Data
public class ClaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, length = 30)
    private String nombre;

    @Column(name = "horaInicio", nullable = false)
    private LocalTime horaInicio;

    @Column(name = "horaFinal", nullable = false)
    private LocalTime horaFinal;

    @ManyToOne
    @JoinColumn(name = "docente_id", referencedColumnName = "user_id", nullable = false)
    private UserEntity docente;

    @Column(name = "salon", nullable = false)
    private String salon;

    @ManyToOne
    @JoinColumn(name = "estado_id", referencedColumnName = "estado_id", nullable = false)
    private EstadoEntity estado;
}

