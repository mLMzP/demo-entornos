package com.lm2a.demoentornos.data;

import com.lm2a.demoentornos.entities.Tarea;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TareaRepository extends JpaRepository<Tarea, Long> { }