package com.example.parcialDos.controlador;

import com.example.parcialDos.modelo.TipoVehiculo;
import com.example.parcialDos.modelo.Usuario;
import com.example.parcialDos.modelo.Vehiculo;
import com.example.parcialDos.repositorio.TipoVehiculoRepositorio;
import com.example.parcialDos.repositorio.UsuarioRepositorio;
import com.example.parcialDos.servicio.VehiculoServicio;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/vehiculo")
public class VehiculoController {

    private final VehiculoServicio vehiculoServicio;
    private final TipoVehiculoRepositorio tipoVehiculoRepositorio;
    private final UsuarioRepositorio usuarioRepositorio;

    public VehiculoController(VehiculoServicio vehiculoServicio,
                              TipoVehiculoRepositorio tipoVehiculoRepositorio,
                              UsuarioRepositorio usuarioRepositorio) {
        this.vehiculoServicio = vehiculoServicio;
        this.tipoVehiculoRepositorio = tipoVehiculoRepositorio;
        this.usuarioRepositorio = usuarioRepositorio;
    }

    @GetMapping("/home")
    public String home(Model model) {
        List<Vehiculo> vehiculos = vehiculoServicio.listarVehiculosEnParqueadero();
        model.addAttribute("vehiculos", vehiculos);

        // Determinar el rol del usuario para mostrar las acciones adecuadas
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean esAdministrador = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMINISTRADOR"));
        boolean esAcomodador = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ACOMODADOR"));

        model.addAttribute("esAdministrador", esAdministrador);
        model.addAttribute("esAcomodador", esAcomodador);

        return "vehiculo/home";
    }

    @GetMapping("/formulario")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public String mostrarFormulario(Model model) {
        model.addAttribute("vehiculo", new Vehiculo());
        model.addAttribute("tiposVehiculo", tipoVehiculoRepositorio.findAll());
        return "vehiculo/formulario";
    }

    @PostMapping("/guardar")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public String guardar(@Valid @ModelAttribute("vehiculo") Vehiculo vehiculo,
                          BindingResult result,
                          Model model,
                          RedirectAttributes flash) {
        if (result.hasErrors()) {
            model.addAttribute("tiposVehiculo", tipoVehiculoRepositorio.findAll());
            return "vehiculo/formulario";
        }

        try {
            // Obtener usuario autenticado
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Usuario usuarioActual = usuarioRepositorio.findByUsername(auth.getName())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            vehiculoServicio.registrarEntrada(vehiculo, usuarioActual);
            flash.addFlashAttribute("success", "Vehículo registrado correctamente");
        } catch (Exception e) {
            flash.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/vehiculo/home";
    }

    @GetMapping("/editar/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ACOMODADOR')")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model, RedirectAttributes flash) {
        Vehiculo vehiculo = vehiculoServicio.buscarPorId(id)
                .orElse(null);

        if (vehiculo == null || vehiculo.getHoraSalida() != null) {
            flash.addFlashAttribute("error", "El vehículo no existe o ya ha salido del parqueadero");
            return "redirect:/vehiculo/home";
        }

        model.addAttribute("vehiculo", vehiculo);
        model.addAttribute("esAcomodador", true);
        return "vehiculo/editar";
    }

    @PostMapping("/actualizar/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ACOMODADOR')")
    public String actualizar(@PathVariable Long id,
                             @RequestParam String ubicacion,
                             RedirectAttributes flash) {
        try {
            vehiculoServicio.actualizarUbicacion(id, ubicacion);
            flash.addFlashAttribute("success", "Ubicación actualizada correctamente");
        } catch (Exception e) {
            flash.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/vehiculo/home";
    }

    @GetMapping("/registrarSalida/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public String registrarSalida(@PathVariable Long id, RedirectAttributes flash) {
        try {
            vehiculoServicio.registrarSalida(id);
            flash.addFlashAttribute("success", "Salida registrada correctamente");
        } catch (Exception e) {
            flash.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/vehiculo/home";
    }

    @GetMapping("/eliminar/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public String confirmarEliminar(@PathVariable Long id, Model model, RedirectAttributes flash) {
        Vehiculo vehiculo = vehiculoServicio.buscarPorId(id)
                .orElse(null);

        if (vehiculo == null) {
            flash.addFlashAttribute("error", "El vehículo no existe");
            return "redirect:/vehiculo/home";
        }

        model.addAttribute("vehiculo", vehiculo);
        return "vehiculo/eliminar";
    }

    @PostMapping("/eliminar/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public String eliminar(@PathVariable Long id, RedirectAttributes flash) {
        try {
            vehiculoServicio.eliminar(id);
            flash.addFlashAttribute("success", "Vehículo eliminado correctamente");
        } catch (Exception e) {
            flash.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/vehiculo/home";
    }
}