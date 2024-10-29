package com.ejercicio.clases.Controller;

import com.ejercicio.clases.Entity.ClaseEntity;
import com.ejercicio.clases.Service.ClaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clases")
public class ClaseController {

    private final ClaseService claseService;

    @Autowired
    public ClaseController(ClaseService claseService) {
        this.claseService = claseService;
    }

    @GetMapping
    public List<ClaseEntity> getAllClases() {
        return claseService.getAllClases();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClaseEntity> getClaseById(@PathVariable Long id) {
        ClaseEntity clase = claseService.getClaseById(id);
        return clase != null ? ResponseEntity.ok(clase) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ClaseEntity createClase(@RequestBody ClaseEntity clase) {
        return claseService.createClase(clase);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClaseEntity> updateClase(@PathVariable Long id, @RequestBody ClaseEntity clase) {
        ClaseEntity updatedClase = claseService.updateClase(id, clase);
        return updatedClase != null ? ResponseEntity.ok(updatedClase) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClase(@PathVariable Long id) {
        claseService.deleteClase(id);
        return ResponseEntity.noContent().build();
    }
}
