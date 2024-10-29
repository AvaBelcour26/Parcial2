package com.ejercicio.clases.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "estados")
public class EstadoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "estado_id")
    private Long id;

    private String nombre;
}