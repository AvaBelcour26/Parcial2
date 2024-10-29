package com.ejercicio.clases.Controller;

import com.ejercicio.clases.Entity.RegistroAsignatura;
import com.ejercicio.clases.Service.RegistroAsignaturaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/registro-asignaturas")
public class RegistroAsignaturaController {

    private final RegistroAsignaturaService registroAsignaturaService;

    @Autowired
    public RegistroAsignaturaController(RegistroAsignaturaService registroAsignaturaService) {
        this.registroAsignaturaService = registroAsignaturaService;
    }

    @GetMapping
    public List<RegistroAsignatura> getAllRegistroAsignaturas() {
        return registroAsignaturaService.getAllRegistroAsignaturas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RegistroAsignatura> getRegistroAsignaturaById(@PathVariable Long id) {
        RegistroAsignatura registroAsignatura = registroAsignaturaService.getRegistroAsignaturaById(id);
        return registroAsignatura != null ? ResponseEntity.ok(registroAsignatura) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public RegistroAsignatura createRegistroAsignatura(@RequestBody RegistroAsignatura registroAsignatura) {
        return registroAsignaturaService.createRegistroAsignatura(registroAsignatura);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RegistroAsignatura> updateRegistroAsignatura(@PathVariable Long id, @RequestBody RegistroAsignatura registroAsignatura) {
        RegistroAsignatura updatedRegistro = registroAsignaturaService.updateRegistroAsignatura(id, registroAsignatura);
        return updatedRegistro != null ? ResponseEntity.ok(updatedRegistro) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRegistroAsignatura(@PathVariable Long id) {
        registroAsignaturaService.deleteRegistroAsignatura(id);
        return ResponseEntity.noContent().build();
    }
}

