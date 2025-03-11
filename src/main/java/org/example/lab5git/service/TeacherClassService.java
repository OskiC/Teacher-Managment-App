package org.example.lab5git.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.lab5git.model.TeacherClass;
import org.example.lab5git.repository.TeacherClassRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeacherClassService {
    private final TeacherClassRepository teacherClassRepository;

    public TeacherClass addTeacherClass(TeacherClass teacherClass) {
        return teacherClassRepository.save(teacherClass);
    }

    public List<TeacherClass> getAllTeacherClasses() {
        return teacherClassRepository.findAll();
    }

    public void deleteTeacherClass(Long id) {
        TeacherClass teacherClass = teacherClassRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("TeacherClass with ID " + id + " not found."));
        teacherClassRepository.delete(teacherClass);
    }

    public TeacherClass getTeacherClassById(Long id) {
        return teacherClassRepository.findById(id).orElse(null);
    }
}
