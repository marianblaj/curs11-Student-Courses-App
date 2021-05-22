package ro.fasttrackit.curs11.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ro.fasttrackit.curs11.model.CollectionResponse;
import ro.fasttrackit.curs11.model.StudentCourse;
import ro.fasttrackit.curs11.model.dto.StudentDto;
import ro.fasttrackit.curs11.model.entity.Student;
import ro.fasttrackit.curs11.model.filters.StudentFilters;
import ro.fasttrackit.curs11.service.student.StudentService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    @GetMapping
    CollectionResponse<StudentDto> getAll(StudentFilters studentFilters) {
        return studentService.getAll(studentFilters);
    }

    @GetMapping("/{studentId}")
    StudentDto getStudentId(@PathVariable String studentId) {
        return studentService.getStudentId(studentId);
    }

    @GetMapping("/{studentId}/courses")
    List<StudentCourse> getStudentIdCourses(@PathVariable String studentId) {
        return studentService.getStudentIdCourses(studentId);
    }

    @PostMapping
    Student addStudent(@RequestBody Student newStudent) {
        return studentService.addStudent(newStudent);
    }
}