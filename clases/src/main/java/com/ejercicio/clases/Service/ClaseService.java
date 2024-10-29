package com.ejercicio.clases.Service;

import com.ejercicio.clases.Entity.ClaseEntity;
import com.ejercicio.clases.Repository.ClaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClaseService {

    private final ClaseRepository claseRepository;

    @Autowired
    public ClaseService(ClaseRepository claseRepository) {
        this.claseRepository = claseRepository;
    }

    public List<ClaseEntity> getAllClases() {
        return claseRepository.findAll();
    }

    public ClaseEntity getClaseById(Long id) {
        return claseRepository.findById(id).orElse(null);
    }

    public ClaseEntity createClase(ClaseEntity clase) {
        return claseRepository.save(clase);
    }

    public ClaseEntity updateClase(Long id, ClaseEntity clase) {
        Optional<ClaseEntity> existingClase = claseRepository.findById(id);
        if (existingClase.isPresent()) {
            ClaseEntity updatedClase = existingClase.get();
            // Actualizar propiedades
            return claseRepository.save(updatedClase);
        } else {
            return null;
        }
    }

    public void deleteClase(Long id) {
        claseRepository.deleteById(id);
    }
}
