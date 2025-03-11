package org.example.lab5git.controller;

import lombok.RequiredArgsConstructor;
import org.example.lab5git.model.Rate;
import org.example.lab5git.repository.TeacherClassRepository;
import org.example.lab5git.service.RateService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/rating")
@RequiredArgsConstructor
public class RateController {
    private final RateService rateService;
    private final TeacherClassRepository teacherClassRepository;

    // Add a new rating
    @PostMapping
    public ResponseEntity<?> addRating(@RequestBody Rate rate) {
        // Check if the teacherClass exists
        if (rate.getTeacherClass() == null || !teacherClassRepository.existsById(rate.getTeacherClass().getId())) {
            return new ResponseEntity<>("No group found with the given ID.", HttpStatus.NOT_FOUND);
        }

        // Auto-set the date if not provided
        if (rate.getDate() == null) {
            rate.setDate(LocalDate.now());
        }

        // Save the rating
        Rate savedRate = rateService.addRate(rate);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRate);
    }

    // Get all ratings
    @GetMapping
    public ResponseEntity<List<Rate>> getAllRates() {
        List<Rate> rates = rateService.getAllRates();
        return ResponseEntity.ok(rates);
    }

}
