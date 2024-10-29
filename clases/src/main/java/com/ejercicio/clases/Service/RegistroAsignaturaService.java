package com.ejercicio.clases.Service;

import com.ejercicio.clases.Entity.RegistroAsignatura;
import com.ejercicio.clases.Repository.RegistroAsignaturaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RegistroAsignaturaService {

    private final RegistroAsignaturaRepository registroAsignaturaRepository;

    @Autowired
    public RegistroAsignaturaService(RegistroAsignaturaRepository registroAsignaturaRepository) {
        this.registroAsignaturaRepository = registroAsignaturaRepository;
    }

    public List<RegistroAsignatura> getAllRegistroAsignaturas() {
        return registroAsignaturaRepository.findAll();
    }

    public RegistroAsignatura getRegistroAsignaturaById(Long id) {
        return registroAsignaturaRepository.findById(id).orElse(null);
    }

    public RegistroAsignatura createRegistroAsignatura(RegistroAsignatura registroAsignatura) {
        return registroAsignaturaRepository.save(registroAsignatura);
    }

    public RegistroAsignatura updateRegistroAsignatura(Long id, RegistroAsignatura registroAsignatura) {
        Optional<RegistroAsignatura> existingRegistro = registroAsignaturaRepository.findById(id);
        if (existingRegistro.isPresent()) {
            RegistroAsignatura updatedRegistro = existingRegistro.get();

            return registroAsignaturaRepository.save(updatedRegistro);
        } else {
            return null;
        }
    }

    public void deleteRegistroAsignatura(Long id) {
        registroAsignaturaRepository.deleteById(id);
    }
}
