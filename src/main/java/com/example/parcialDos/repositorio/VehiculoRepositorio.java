package com.example.parcialDos.repositorio;

import com.example.parcialDos.modelo.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehiculoRepositorio extends JpaRepository<Vehiculo, Long> {
    Optional<Vehiculo> findByPlacaAndActivoTrue(String placa);

    List<Vehiculo> findByActivoTrue();

    @Query("SELECT v FROM Vehiculo v WHERE v.horaSalida IS NULL AND v.activo = true")
    List<Vehiculo> findAllVehiculosEnParqueadero();

    boolean existsByPlacaAndHoraSalidaIsNullAndActivoTrue(String placa);
}