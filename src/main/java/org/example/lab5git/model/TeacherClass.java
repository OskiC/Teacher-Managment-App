package org.example.lab5git.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TeacherClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String groupName;
    private Integer teacherLimit;

    @OneToMany(mappedBy = "teacherClass", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Teacher> teachers = new ArrayList<>();

    // Parameterized constructor
    public TeacherClass(String groupName, Integer teacherLimit) {
        this.groupName = groupName;
        this.teacherLimit = teacherLimit;
    }

    public String getName() {
        return groupName;
    }
}

