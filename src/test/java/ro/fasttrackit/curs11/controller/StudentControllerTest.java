package ro.fasttrackit.curs11.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ro.fasttrackit.curs11.Curs11HomeworkApplication;
import ro.fasttrackit.curs11.mapper.CourseMapper;
import ro.fasttrackit.curs11.model.CollectionResponse;
import ro.fasttrackit.curs11.model.PageInfo;
import ro.fasttrackit.curs11.model.StudentCourse;
import ro.fasttrackit.curs11.model.dto.StudentDto;
import ro.fasttrackit.curs11.model.entity.Student;
import ro.fasttrackit.curs11.model.filters.StudentFilters;
import ro.fasttrackit.curs11.service.Course.CourseService;
import ro.fasttrackit.curs11.service.student.StudentService;

import java.util.List;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@WebMvcTest
@ContextConfiguration(classes = {Curs11HomeworkApplication.class, StudentControllerTest.TestBeans.class})
public class StudentControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private StudentService studentService;

    @Test
    @DisplayName("GET /students")
    void getAllTest() throws Exception {
        var collRespons = CollectionResponse.<StudentDto>builder()
                .content(List.of(StudentDto.builder()
                                .name("Alina")
                                .age(22).build(),
                        StudentDto.builder()
                                .name("Sandu")
                                .age(19).build()))
                .pageInfo(PageInfo.builder()
                        .totalPages(1)
                        .totalElements(1)
                        .crtPage(1)
                        .pageSize(1)
                        .build()).build();

        doReturn(collRespons).when(studentService).getAll(StudentFilters.builder().build());
        mvc.perform(MockMvcRequestBuilders.get("/students"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[0].name", CoreMatchers.is("Alina")));
    }

    @Test
    @DisplayName("GET /students/{studentId}")
    void getStudentIdTest() throws Exception {
        StudentDto student = StudentDto.builder()
                .name("Alina")
                .age(22).build();
        doReturn(student).when(studentService).getStudentId("studentId");
        mvc.perform(MockMvcRequestBuilders.get("/students/studentId"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").isString())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is("Alina")));
    }

    @Test
    @DisplayName("GET /{studentId}/courses")
    void getStudentIdCourses() throws Exception {
        List<StudentCourse> coursesList = List.of(
                StudentCourse.builder()
                        .discipline("Math")
                        .age(22)
                        .name("Alina")
                        .build(),
                StudentCourse.builder()
                        .discipline("")
                        .age(20)
                        .name("")
                        .build());
        doReturn(coursesList).when(studentService).getStudentIdCourses("studentId");
        mvc.perform(MockMvcRequestBuilders.get("/students/studentId/courses"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].discipline", CoreMatchers.is("Math")));
    }

    @Test
    @DisplayName("POST /students")
    void postStudent() throws Exception {
        Student student = Student.builder()
                .name("Alina").build();
        doReturn(student).when(studentService).addStudent(student);

        mvc.perform(MockMvcRequestBuilders.post("/students")
                .content(asJsonString(student))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").isString())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is("Alina")));
    }

    public static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Configuration
    static class TestBeans {

        @Bean
        StudentService studentService() {
            return mock(StudentService.class);
        }

        @Bean
        StudentDto studentDto() {
            return mock(StudentDto.class);
        }

        @Bean
        CollectionResponse<StudentDto> collectionResponse() {
            return mock(CollectionResponse.class);
        }

        @Bean
        CourseService courseService() {
            return mock(CourseService.class);
        }

        @Bean
        CourseMapper courseMapper() {
            return mock(CourseMapper.class);
        }
    }
}
