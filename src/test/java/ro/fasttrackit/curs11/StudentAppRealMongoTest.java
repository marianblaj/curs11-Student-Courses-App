package ro.fasttrackit.curs11;

import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import ro.fasttrackit.curs11.model.entity.Student;
import ro.fasttrackit.curs11.repository.student.StudentRepository;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@ContextConfiguration(initializers = StudentAppRealMongoTest.Initializer.class)
public class StudentAppRealMongoTest {

    @Container
    private static MongoDBContainer mongoDb = new MongoDBContainer(DockerImageName.parse("mongo:latest"));

    static {
        mongoDb.start();
    }

    @Autowired
    private MockMvc mvc;
    @Autowired
    private StudentRepository repository;

    @SneakyThrows
    @Test
    @DisplayName("GET /students")
    void getAllCountriesTest() {
        repository.saveAll(List.of(
                Student.builder()
                        .name("Alina")
                        .age(20).build(),
                Student.builder()
                        .name("Sandu")
                        .age(23).build()
        ));

        mvc.perform(get("/students"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.content.[0].name", is("Alina")));
    }

    @AfterEach
    void cleanup() {
        repository.deleteAll();
    }

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    String.format("spring.data.mongodb.uri: %s", mongoDb.getReplicaSetUrl())
            ).applyTo(configurableApplicationContext);
        }
    }
}