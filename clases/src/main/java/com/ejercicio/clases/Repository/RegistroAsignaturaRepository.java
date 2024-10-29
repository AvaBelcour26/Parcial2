package com.ejercicio.clases.Repository;

import com.ejercicio.clases.Entity.RegistroAsignatura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistroAsignaturaRepository extends JpaRepository<RegistroAsignatura, Long> {
}
