package org.example.lab5git;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.example.lab5git.model.Teacher;
import org.example.lab5git.model.TeacherClass;
import org.example.lab5git.repository.RateRepository;
import org.example.lab5git.repository.TeacherClassRepository;
import org.example.lab5git.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static com.jayway.jsonpath.internal.path.PathCompiler.fail;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
public class Lab5gitApplicationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private TeacherClassRepository teacherClassRepository;
    @Autowired
    private RateRepository rateRepository;




    @Test
    void testAddTeacher() {
        Teacher teacher = new Teacher("John", "Doe", 3000.0, 30, null);
        ResponseEntity<Teacher> response = restTemplate.postForEntity("/api/teacher", teacher, Teacher.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("John");
    }

    @Test
    void testDeleteTeacher() {
        Teacher teacher = teacherRepository.save(new Teacher("Jane", "Smith", 3500.0, 32, null));

        ResponseEntity<Void> response = restTemplate.exchange("/api/teacher/" + teacher.getId(),
                HttpMethod.DELETE, null, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(teacherRepository.findById(teacher.getId())).isEmpty();
    }

    @Test
    void testExportTeachersToCsv() {
        teacherRepository.save(new Teacher("Jan", "Ek", 3000.0, 30, null));

        ResponseEntity<String> response = restTemplate.getForEntity("/api/teacher/csv", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(response.getHeaders().getContentType().toString()).isEqualTo("text/csv");

        assertThat(response.getBody()).contains("Jan,Ek");
    }


    @Test
    void testGetAllGroups() {
        // Create a unique group to test
        TeacherClass teacherClass = new TeacherClass("Matma_Test_" + UUID.randomUUID(), 5);
        teacherClassRepository.save(teacherClass);

        // Fetch all groups
        ResponseEntity<TeacherClass[]> response = restTemplate.getForEntity("/api/group", TeacherClass[].class);

        // Validate response status
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        // Validate response body
        TeacherClass[] body = response.getBody();
        assertThat(body).isNotNull().isNotEmpty();
        assertThat(body).extracting(TeacherClass::getName).contains(teacherClass.getName());
    }





    @Test
    void testAddGroup() {
        TeacherClass group = new TeacherClass("Sci", 10);

        ResponseEntity<TeacherClass> response = restTemplate.postForEntity("/api/group", group, TeacherClass.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getGroupName()).isEqualTo("Sci");
    }

    @Test
    void testDeleteGroup() {
        TeacherClass group = teacherClassRepository.save(new TeacherClass("His", 8));

        ResponseEntity<Void> response = restTemplate.exchange("/api/group/" + group.getId(),
                HttpMethod.DELETE, null, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(teacherClassRepository.findById(group.getId())).isEmpty();
    }

    @Test
    void testGetTeachersByGroup() {
        TeacherClass group = teacherClassRepository.save(new TeacherClass("Ang", 4));
        teacherRepository.save(new Teacher("Alicja", "Czarow", 2800.0, 29, group));

        ResponseEntity<Teacher[]> response = restTemplate.getForEntity("/api/group/" + group.getId() + "/teacher", Teacher[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
        assertThat(response.getBody()[0].getName()).isEqualTo("Alicja");
    }

    @Test
    void testGetGroupFillPercentage() {
        TeacherClass group = teacherClassRepository.save(new TeacherClass("Biology", 3));
        teacherRepository.save(new Teacher("Bob", "Williams", 2900.0, 31, group));

        ResponseEntity<String> response = restTemplate.getForEntity("/api/group/" + group.getId() + "/fill", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("33.33");
    }

    @Test
    void testAddRating() {
        TeacherClass group = teacherClassRepository.save(new TeacherClass("Geography", 7));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String ratingPayload = """
                {
                    "teacherClass": { "id": %d },
                    "rating": 5,
                    "comment": "Excellent group!"
                }
                """.formatted(group.getId());

        HttpEntity<String> request = new HttpEntity<>(ratingPayload, headers);
        ResponseEntity<Void> response = restTemplate.postForEntity("/api/rating", request, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }
}
