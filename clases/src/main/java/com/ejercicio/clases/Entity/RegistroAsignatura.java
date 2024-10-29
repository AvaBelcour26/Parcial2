package com.ejercicio.clases.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "registros_asignatura")
public class RegistroAsignatura {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", referencedColumnName = "user_id")
    private UserEntity usuario;

    @ManyToOne
    private ClaseEntity clase;

    private String accion; // crear, modificar, eliminar, visualizar
    private String fechaHora;
}
