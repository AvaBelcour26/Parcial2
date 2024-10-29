package com.ejercicio.clases.Controller;

import com.ejercicio.clases.Entity.EstadoEntity;
import com.ejercicio.clases.Service.EstadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/estados")
public class EstadoController {

    private final EstadoService estadoService;

    @Autowired
    public EstadoController(EstadoService estadoService) {
        this.estadoService = estadoService;
    }

    @GetMapping
    public List<EstadoEntity> getAllEstados() {
        return estadoService.getAllEstados();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstadoEntity> getEstadoById(@PathVariable Long id) {
        EstadoEntity estado = estadoService.getEstadoById(id);
        return estado != null ? ResponseEntity.ok(estado) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public EstadoEntity createEstado(@RequestBody EstadoEntity estado) {
        return estadoService.createEstado(estado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EstadoEntity> updateEstado(@PathVariable Long id, @RequestBody EstadoEntity estado) {
        EstadoEntity updatedEstado = estadoService.updateEstado(id, estado);
        return updatedEstado != null ? ResponseEntity.ok(updatedEstado) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEstado(@PathVariable Long id) {
        estadoService.deleteEstado(id);
        return ResponseEntity.noContent().build();
    }
}