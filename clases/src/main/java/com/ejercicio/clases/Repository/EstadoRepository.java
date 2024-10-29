package com.ejercicio.clases.Repository;

import com.ejercicio.clases.Entity.EstadoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstadoRepository extends JpaRepository<EstadoEntity, Long> {
    EstadoEntity findByNombre(String nombre);
}
