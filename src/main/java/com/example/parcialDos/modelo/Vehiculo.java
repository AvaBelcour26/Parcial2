package com.example.parcialDos.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "vehiculos")
public class Vehiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "La placa es obligatoria")
    @Size(max = 6, message = "La placa no debe superar los 6 caracteres")
    @Column(nullable = false, length = 6)
    private String placa;

    @NotNull(message = "La hora de entrada es obligatoria")
    @Column(nullable = false)
    private LocalDateTime horaEntrada;

    @Column
    private LocalDateTime horaSalida;

    @NotBlank(message = "La ubicación es obligatoria")
    @Column(nullable = false)
    private String ubicacion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tipo_vehiculo_id", nullable = false)
    private TipoVehiculo tipoVehiculo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_registra_id", nullable = false)
    private Usuario usuarioRegistra;

    @Column(nullable = false)
    private boolean activo = true;

    // Método para verificar si el vehículo está dentro del parqueadero
    public boolean estaEnParqueadero() {
        return this.horaSalida == null;
    }
}