package com.ejercicio.clases.Repository;

import com.ejercicio.clases.Entity.ClaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClaseRepository extends JpaRepository<ClaseEntity, Long> {
}
