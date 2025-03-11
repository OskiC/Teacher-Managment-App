package org.example.lab5git.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.*;
import org.example.lab5git.model.Teacher;
import org.example.lab5git.model.TeacherClass;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.example.lab5git.service.*;

import java.util.List;

@RestController
@RequestMapping("/api/group")
@RequiredArgsConstructor
public class TeacherClassController {
    private final TeacherClassService teacherClassService;
    private final TeacherService teacherService;

    @PostMapping
    public ResponseEntity<TeacherClass> addTeacherClass(@RequestBody TeacherClass teacherClass) {
        return ResponseEntity.status(HttpStatus.CREATED).body(teacherClassService.addTeacherClass(teacherClass));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTeacherClassById(@PathVariable Long id) {
        TeacherClass teacherClass = teacherClassService.getTeacherClassById(id);
        if (teacherClass == null) {
            return new ResponseEntity<>("TeacherClass with ID " + id + " not found.", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(teacherClass, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<TeacherClass>> getAllTeacherClasses() {
        List<TeacherClass> teacherClasses = teacherClassService.getAllTeacherClasses();
        return ResponseEntity.ok(teacherClasses);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTeacherClass(@PathVariable Long id) {
        try {
            teacherClassService.deleteTeacherClass(id);
            return new ResponseEntity<>("TeacherClass with ID " + id + " deleted successfully.", HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}/teacher")
    public ResponseEntity<List<Teacher>> getTeachersByClassId(@PathVariable Long id) {
        return ResponseEntity.ok(teacherService.getTeachersByClassId(id));
    }

    @GetMapping("/{id}/fill")
    public ResponseEntity<Double> getFillPercentage(@PathVariable Long id) {
        TeacherClass teacherClass = teacherClassService.getTeacherClassById(id);

        if (teacherClass == null) {
            throw new EntityNotFoundException("TeacherClass not found for ID: " + id);
        }

        int teacherCount = teacherService.getTeachersByClassId(id).size();
        int teacherLimit = teacherClass.getTeacherLimit();

        if (teacherLimit == 0) {
            throw new IllegalStateException("Teacher limit for the group cannot be zero.");
        }

        double fillPercentage = (double) teacherCount / teacherLimit * 100;
        fillPercentage = Math.round(fillPercentage*100.0)/100.0;

        return ResponseEntity.ok(fillPercentage);
    }
}
