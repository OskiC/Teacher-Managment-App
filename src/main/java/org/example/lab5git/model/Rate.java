package org.example.lab5git.model;


import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private TeacherClass teacherClass;

    private Integer rating;
    private LocalDate date;

    @Column(nullable = false)
    private String comment;


    // Parameterized constructor
    public Rate(Integer rating, String comment, LocalDate date, TeacherClass teacherClass) {
        this.rating = rating;
        this.comment = comment;
        this.date = date;
        this.teacherClass = teacherClass;
    }
}