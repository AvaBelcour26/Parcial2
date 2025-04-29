package com.example.parcialDos.config;

import com.example.parcialDos.modelo.Rol;
import com.example.parcialDos.modelo.TipoVehiculo;
import com.example.parcialDos.modelo.Usuario;
import com.example.parcialDos.repositorio.RolRepositorio;
import com.example.parcialDos.repositorio.TipoVehiculoRepositorio;
import com.example.parcialDos.repositorio.UsuarioRepositorio;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RolRepositorio rolRepositorio;
    private final UsuarioRepositorio usuarioRepositorio;
    private final TipoVehiculoRepositorio tipoVehiculoRepositorio;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(RolRepositorio rolRepositorio,
                           UsuarioRepositorio usuarioRepositorio,
                           TipoVehiculoRepositorio tipoVehiculoRepositorio,
                           PasswordEncoder passwordEncoder) {
        this.rolRepositorio = rolRepositorio;
        this.usuarioRepositorio = usuarioRepositorio;
        this.tipoVehiculoRepositorio = tipoVehiculoRepositorio;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        crearRoles();
        crearUsuarios();
        crearTiposVehiculos();
    }

    private void crearRoles() {
        if (rolRepositorio.count() == 0) {
            List<Rol> roles = Arrays.asList(
                    new Rol("ADMINISTRADOR"),
                    new Rol("ACOMODADOR"),
                    new Rol("CLIENTE")
            );

            rolRepositorio.saveAll(roles);
        }
    }

    private void crearUsuarios() {
        if (usuarioRepositorio.count() == 0) {
            // Obtener roles
            Rol rolAdmin = rolRepositorio.findByNombre("ADMINISTRADOR")
                    .orElseThrow(() -> new RuntimeException("Rol ADMINISTRADOR no encontrado"));

            Rol rolAcomodador = rolRepositorio.findByNombre("ACOMODADOR")
                    .orElseThrow(() -> new RuntimeException("Rol ACOMODADOR no encontrado"));

            Rol rolCliente = rolRepositorio.findByNombre("CLIENTE")
                    .orElseThrow(() -> new RuntimeException("Rol CLIENTE no encontrado"));

            //Se crea usuario administrador
            Usuario admin = new Usuario();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setNombre("Administrador");
            admin.setActivo(true);
            admin.agregarRol(rolAdmin);

            // Se crea usuario acomodador
            Usuario acomodador = new Usuario();
            acomodador.setUsername("acomodador");
            acomodador.setPassword(passwordEncoder.encode("acomodador123"));
            acomodador.setNombre("Acomodador Parking");
            acomodador.setActivo(true);
            acomodador.agregarRol(rolAcomodador);

            // Se crea usuario cliente
            Usuario cliente = new Usuario();
            cliente.setUsername("cliente");
            cliente.setPassword(passwordEncoder.encode("cliente123"));
            cliente.setNombre("Cliente");
            cliente.setActivo(true);
            cliente.agregarRol(rolCliente);

            // Guardar usuarios
            usuarioRepositorio.saveAll(Arrays.asList(admin, acomodador, cliente));
        }
    }

    private void crearTiposVehiculos() {
        if (tipoVehiculoRepositorio.count() == 0) {
            List<TipoVehiculo> tiposVehiculo = Arrays.asList(
                    new TipoVehiculo("AUTOMÓVIL", "Vehículo de cuatro ruedas para transporte de personas"),
                    new TipoVehiculo("MOTOCICLETA", "Vehículo de dos ruedas"),
                    new TipoVehiculo("CAMIONETA", "Vehículo utilitario deportivo o pickup"),
                    new TipoVehiculo("FURGÓN", "Vehículo para transporte de mercancías"),
                    new TipoVehiculo("BICICLETA", "Vehículo de dos ruedas impulsado a pedal")
            );

            tipoVehiculoRepositorio.saveAll(tiposVehiculo);
        }
    }
}
