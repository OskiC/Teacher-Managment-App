package org.example.lab5git.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String surname;
    private Double salary;
    private Integer age;

    @ManyToOne
    @JsonBackReference
    private TeacherClass teacherClass;


    // Parameterized constructor
    public Teacher(String name, String surname, Double salary, Integer age, TeacherClass teacherClass) {
        this.name = name;
        this.surname = surname;
        this.salary = salary;
        this.age = age;
        this.teacherClass = teacherClass;
    }
}

