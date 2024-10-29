package com.ejercicio.clases.Service;


import com.ejercicio.clases.Entity.EstadoEntity;
import com.ejercicio.clases.Repository.EstadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EstadoService {

    private final EstadoRepository estadoRepository;

    @Autowired
    public EstadoService(EstadoRepository estadoRepository) {
        this.estadoRepository = estadoRepository;
    }

    public List<EstadoEntity> getAllEstados() {
        return estadoRepository.findAll();
    }

    public EstadoEntity getEstadoById(Long id) {
        return estadoRepository.findById(id).orElse(null);
    }

    public EstadoEntity createEstado(EstadoEntity estado) {
        return estadoRepository.save(estado);
    }

    public EstadoEntity updateEstado(Long id, EstadoEntity estado) {
        Optional<EstadoEntity> existingEstado = estadoRepository.findById(id);
        if (existingEstado.isPresent()) {
            EstadoEntity updatedEstado = existingEstado.get();
            // Actualizar propiedades
            return estadoRepository.save(updatedEstado);
        } else {
            return null;
        }
    }

    public void deleteEstado(Long id) {
        estadoRepository.deleteById(id);
    }
}


