package org.example.lab5git.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.*;
import org.apache.commons.csv.*;
import org.example.lab5git.model.Teacher;
import org.example.lab5git.service.TeacherService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@RestController
@RequestMapping("/api/teacher")
@RequiredArgsConstructor
public class TeacherController {
    private final TeacherService teacherService;

    @PostMapping
    public ResponseEntity<Teacher> addTeacher(@RequestBody Teacher teacher) {
        return ResponseEntity.status(HttpStatus.CREATED).body(teacherService.addTeacher(teacher));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeacher(@PathVariable Long id) {
        teacherService.deleteTeacher(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/csv")
    public ResponseEntity<Resource> exportTeachersToCsv() throws IOException {
        List<Teacher> teachers = teacherService.getAllTeachers();

        // Generate CSV content
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVPrinter printer = new CSVPrinter(new PrintWriter(out), CSVFormat.DEFAULT.withHeader("ID", "Name", "Surname", "Salary", "Age", "Group"));

        for (Teacher teacher : teachers) {
            printer.printRecord(
                    teacher.getId(),
                    teacher.getName(),
                    teacher.getSurname(),
                    teacher.getSalary(),
                    teacher.getAge(),
                    teacher.getTeacherClass() != null ? teacher.getTeacherClass().getGroupName() : "None"
            );
        }
        printer.flush();

        // Create the file resource
        ByteArrayResource resource = new ByteArrayResource(out.toByteArray());

        // Set response headers
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("text/csv"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=teachers.csv")
                .body(resource);
    }
}
