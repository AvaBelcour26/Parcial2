package com.example.parcialDos.controlador;

import com.example.parcialDos.modelo.TipoVehiculo;
import com.example.parcialDos.modelo.Usuario;
import com.example.parcialDos.modelo.Vehiculo;
import com.example.parcialDos.repositorio.TipoVehiculoRepositorio;
import com.example.parcialDos.repositorio.UsuarioRepositorio;
import com.example.parcialDos.servicio.VehiculoServicio;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Gestión de Vehículos", description = "Controlador para la gestión de vehículos en el parqueadero mediante vistas Thymeleaf")
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
    @Operation(summary = "Vista principal de vehículos",
            description = "Muestra la página principal con la lista de vehículos actualmente en el parqueadero")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vista de vehículos cargada correctamente"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
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
    @Operation(summary = "Formulario de registro de vehículo",
            description = "Muestra el formulario para registrar la entrada de un nuevo vehículo (solo administradores)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Formulario cargado correctamente"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado - Solo administradores")
    })
    public String mostrarFormulario(Model model) {
        model.addAttribute("vehiculo", new Vehiculo());
        model.addAttribute("tiposVehiculo", tipoVehiculoRepositorio.findAll());
        return "vehiculo/formulario";
    }

    @PostMapping("/guardar")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @Operation(summary = "Guardar nuevo vehículo",
            description = "Procesa el formulario para registrar la entrada de un nuevo vehículo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "Vehículo registrado correctamente, redirección a home"),
            @ApiResponse(responseCode = "400", description = "Datos de formulario inválidos"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado - Solo administradores")
    })
    public String guardar(
            @Parameter(description = "Datos del vehículo a registrar",
                    schema = @Schema(implementation = Vehiculo.class))
            @Valid @ModelAttribute("vehiculo") Vehiculo vehiculo,
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
    @Operation(summary = "Formulario de edición de ubicación",
            description = "Muestra el formulario para editar la ubicación de un vehículo (administradores y acomodadores)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Formulario de edición cargado correctamente"),
            @ApiResponse(responseCode = "302", description = "Vehículo no encontrado o ya salió, redirección a home"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado - Solo administradores y acomodadores")
    })
    public String mostrarFormularioEditar(
            @Parameter(description = "ID del vehículo a editar")
            @PathVariable Long id,
            Model model,
            RedirectAttributes flash) {
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
    @Operation(summary = "Actualizar ubicación",
            description = "Procesa el formulario para actualizar la ubicación de un vehículo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "Ubicación actualizada correctamente, redirección a home"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado - Solo administradores y acomodadores")
    })
    public String actualizar(
            @Parameter(description = "ID del vehículo a actualizar")
            @PathVariable Long id,
            @Parameter(description = "Nueva ubicación del vehículo")
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
    @Operation(summary = "Registrar salida de vehículo",
            description = "Registra la salida de un vehículo del parqueadero (solo administradores)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "Salida registrada correctamente, redirección a home"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado - Solo administradores")
    })
    public String registrarSalida(
            @Parameter(description = "ID del vehículo a registrar salida")
            @PathVariable Long id,
            RedirectAttributes flash) {
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
    @Operation(summary = "Confirmar eliminación",
            description = "Muestra la página de confirmación para eliminar un vehículo (solo administradores)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Página de confirmación cargada correctamente"),
            @ApiResponse(responseCode = "302", description = "Vehículo no encontrado, redirección a home"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado - Solo administradores")
    })
    public String confirmarEliminar(
            @Parameter(description = "ID del vehículo a eliminar")
            @PathVariable Long id,
            Model model,
            RedirectAttributes flash) {
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
    @Operation(summary = "Eliminar vehículo",
            description = "Procesa la eliminación de un vehículo del sistema (solo administradores)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "Vehículo eliminado correctamente, redirección a home"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado - Solo administradores")
    })
    public String eliminar(
            @Parameter(description = "ID del vehículo a eliminar definitivamente")
            @PathVariable Long id,
            RedirectAttributes flash) {
        try {
            vehiculoServicio.eliminar(id);
            flash.addFlashAttribute("success", "Vehículo eliminado correctamente");
        } catch (Exception e) {
            flash.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/vehiculo/home";
    }
}