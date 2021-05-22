package ro.fasttrackit.curs11;

import lombok.SneakyThrows;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import ro.fasttrackit.curs11.model.entity.Student;
import ro.fasttrackit.curs11.repository.student.StudentRepository;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CoursesAppTest {
    @Autowired
    MockMvc mvc;
    @Autowired
    private StudentRepository repository;

    @SneakyThrows
    @Test
    @DisplayName("GET /students")
    void getStudentsTest() {
        repository.saveAll(List.of(
                Student.builder()
                        .name("Alina")
                        .age(24).build(),
                Student.builder()
                        .name("Ghita")
                        .age(21).build()
        ));
        mvc.perform(get("/students"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.[1].name", CoreMatchers.is("Ghita")));
    }

    @AfterEach
    void cleanup() {
        repository.deleteAll();
    }
}
