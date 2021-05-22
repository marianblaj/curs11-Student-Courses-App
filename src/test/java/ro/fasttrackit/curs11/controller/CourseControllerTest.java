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
import ro.fasttrackit.curs11.model.dto.CourseDto;
import ro.fasttrackit.curs11.model.dto.StudentDto;
import ro.fasttrackit.curs11.model.entity.Course;
import ro.fasttrackit.curs11.model.filters.CourseFilters;
import ro.fasttrackit.curs11.service.Course.CourseService;
import ro.fasttrackit.curs11.service.student.StudentService;

import java.util.List;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@WebMvcTest
@ContextConfiguration(classes = {Curs11HomeworkApplication.class, StudentControllerTest.TestBeans.class})
public class CourseControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private CourseService courseService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /courses")
    void getAllTest() throws Exception {
        var collectionResponseCourseDto = CollectionResponse.<CourseDto>builder()
                .content(List.of(CourseDto.builder()
                                .discipline("Math").build(),
                        CourseDto.builder()
                                .discipline("history").build()))
                .pageInfo(PageInfo.builder()
                        .totalPages(1)
                        .totalElements(1)
                        .crtPage(1)
                        .pageSize(1)
                        .build()).build();

        doReturn(collectionResponseCourseDto).when(courseService).getAll(CourseFilters.builder().build());
        mvc.perform(MockMvcRequestBuilders.get("/courses"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[0].discipline", CoreMatchers.is("Math")));

    }

    @Test
    @DisplayName("GET /{courseId}")
    void getCourseIdTest() throws Exception {
        CourseDto course = CourseDto.builder()
                .discipline("Math")
                .build();
        doReturn(course).when(courseService).getCourseId("courseId");
        mvc.perform(MockMvcRequestBuilders.get("/courses/courseId"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.discipline").isString())
                .andExpect(MockMvcResultMatchers.jsonPath("$.discipline", CoreMatchers.is("Math")));
    }

    @Test
    @DisplayName("GET /{courseId}/students")
    void getCourseIdStudents() throws Exception {
        List<StudentDto> studentsList = List.of(
                StudentDto.builder()
                        .name("Alina")
                        .age(22)
                        .build(),
                StudentDto.builder()
                        .name("Gigi")
                        .age(20)
                        .build());
        doReturn(studentsList).when(courseService).getCourseIdStudents("courseId");
        mvc.perform(MockMvcRequestBuilders.get("/courses/courseId/students"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", CoreMatchers.is("Alina")));
    }

    @Test
    @DisplayName("POST /courses")
    void postCourse() throws Exception {
        Course course = Course.builder()
                .discipline("Math").build();
        doReturn(course).when(courseService).addCourse(course);

        mvc.perform(MockMvcRequestBuilders.post("/courses")
                .content(asJsonString(course))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.discipline").isString())
                .andExpect(MockMvcResultMatchers.jsonPath("$.discipline", CoreMatchers.is("Math")));
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

