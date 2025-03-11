package org.example.lab5git.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.lab5git.model.Teacher;
import org.example.lab5git.model.TeacherClass;
import org.example.lab5git.repository.TeacherClassRepository;
import org.example.lab5git.repository.TeacherRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeacherService {
    private final TeacherRepository teacherRepository;
    private final TeacherClassRepository teacherClassRepository;

    public Teacher addTeacher(Teacher teacher) {
        TeacherClass teacherClass = teacher.getTeacherClass();
        if (teacherClass != null) {
            teacherClass = teacherClassRepository.findById(teacherClass.getId())
                    .orElseThrow(() -> new EntityNotFoundException("TeacherClass not found."));

            if (teacherClass.getTeachers().size() >= teacherClass.getTeacherLimit()) {
                throw new IllegalStateException("The teacher class is already at maximum capacity.");
            }
        }
        return teacherRepository.save(teacher);
    }

    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    public void deleteTeacher(Long id) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Teacher with ID " + id + " not found."));
        teacherRepository.delete(teacher);
    }


    public List<Teacher> getTeachersByClassId(Long classId) {
        return teacherRepository.findAllByTeacherClassId(classId);
    }
}

