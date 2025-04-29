package com.example.parcialDos.servicio;

import com.example.parcialDos.modelo.Usuario;
import com.example.parcialDos.modelo.Vehiculo;
import com.example.parcialDos.repositorio.VehiculoRepositorio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class VehiculoServicio {

    private final VehiculoRepositorio vehiculoRepositorio;

    public VehiculoServicio(VehiculoRepositorio vehiculoRepositorio) {
        this.vehiculoRepositorio = vehiculoRepositorio;
    }

    @Transactional(readOnly = true)
    public List<Vehiculo> listarTodos() {
        return vehiculoRepositorio.findByActivoTrue();
    }

    @Transactional(readOnly = true)
    public List<Vehiculo> listarVehiculosEnParqueadero() {
        return vehiculoRepositorio.findAllVehiculosEnParqueadero();
    }

    @Transactional(readOnly = true)
    public Optional<Vehiculo> buscarPorId(Long id) {
        return vehiculoRepositorio.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Vehiculo> buscarPorPlaca(String placa) {
        return vehiculoRepositorio.findByPlacaAndActivoTrue(placa);
    }

    @Transactional
    public Vehiculo registrarEntrada(Vehiculo vehiculo, Usuario usuarioRegistra) {
        if (vehiculoRepositorio.existsByPlacaAndHoraSalidaIsNullAndActivoTrue(vehiculo.getPlaca())) {
            throw new RuntimeException("El vehículo con placa " + vehiculo.getPlaca() + " ya se encuentra en el parqueadero");
        }

        vehiculo.setUsuarioRegistra(usuarioRegistra);
        vehiculo.setHoraEntrada(LocalDateTime.now());
        return vehiculoRepositorio.save(vehiculo);
    }

    @Transactional
    public Vehiculo registrarSalida(Long id) {
        Vehiculo vehiculo = vehiculoRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehículo no encontrado con id: " + id));

        if (vehiculo.getHoraSalida() != null) {
            throw new RuntimeException("El vehículo ya tiene registrada su salida");
        }

        vehiculo.setHoraSalida(LocalDateTime.now());
        return vehiculoRepositorio.save(vehiculo);
    }

    @Transactional
    public Vehiculo actualizarUbicacion(Long id, String nuevaUbicacion) {
        Vehiculo vehiculo = vehiculoRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehículo no encontrado con id: " + id));

        if (vehiculo.getHoraSalida() != null) {
            throw new RuntimeException("No se puede actualizar la ubicación de un vehículo que ya salió");
        }

        vehiculo.setUbicacion(nuevaUbicacion);
        return vehiculoRepositorio.save(vehiculo);
    }

    @Transactional
    public void eliminar(Long id) {
        Vehiculo vehiculo = vehiculoRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehículo no encontrado con id: " + id));

        vehiculo.setActivo(false);
        vehiculoRepositorio.save(vehiculo);
    }

    @Transactional(readOnly = true)
    public boolean existeVehiculoEnParqueadero(String placa) {
        return vehiculoRepositorio.existsByPlacaAndHoraSalidaIsNullAndActivoTrue(placa);
    }
}