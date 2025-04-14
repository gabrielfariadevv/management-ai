package com.student.management.controller;

import com.student.management.dto.ExerciseRequest;
import com.student.management.service.ExerciseGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/exercises")
public class ExerciseController {

    private final ExerciseGeneratorService generatorService;

    @Autowired
    public ExerciseController(ExerciseGeneratorService generatorService) {
        this.generatorService = generatorService;
    }

    @PostMapping("/generate")
    public ResponseEntity<List<String>> generate(@RequestBody ExerciseRequest request) {
        List<String> exercises = generatorService
                .generateExercises(request.getSubject(), request.getCount(), request.getTopic());
        return ResponseEntity.ok(exercises);
    }
}
