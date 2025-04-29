package com.example.parcialDos.repositorio;

import com.example.parcialDos.modelo.TipoVehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TipoVehiculoRepositorio extends JpaRepository<TipoVehiculo, Long> {
    Optional<TipoVehiculo> findByNombre(String nombre);
}
