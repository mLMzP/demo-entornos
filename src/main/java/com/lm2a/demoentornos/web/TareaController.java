package com.lm2a.demoentornos.web;

import com.lm2a.demoentornos.data.TareaRepository;
import com.lm2a.demoentornos.entities.Tarea;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tareas")
public class TareaController {

    private static final Logger log = LoggerFactory.getLogger(TareaController.class);

    @Autowired
    private TareaRepository repository;

    @GetMapping
    public List<Tarea> listar() {
        return repository.findAll();
    }

    @PostMapping
    public Tarea crear(@RequestBody Tarea tarea) {
        log.info("Creando tarea: {}", tarea);
        return repository.save(tarea);
    }
}