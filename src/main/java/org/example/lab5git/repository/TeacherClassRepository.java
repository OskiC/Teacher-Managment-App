package org.example.lab5git.repository;

import org.example.lab5git.model.TeacherClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherClassRepository extends JpaRepository<TeacherClass, Long> {}
