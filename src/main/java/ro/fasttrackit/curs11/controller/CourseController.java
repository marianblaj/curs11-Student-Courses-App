package ro.fasttrackit.curs11.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ro.fasttrackit.curs11.mapper.CourseMapper;
import ro.fasttrackit.curs11.model.CollectionResponse;
import ro.fasttrackit.curs11.model.dto.CourseDto;
import ro.fasttrackit.curs11.model.dto.StudentDto;
import ro.fasttrackit.curs11.model.entity.Course;
import ro.fasttrackit.curs11.model.entity.CourseStudent;
import ro.fasttrackit.curs11.model.filters.CourseFilters;
import ro.fasttrackit.curs11.service.Course.CourseService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;
    private final CourseMapper courseMapper;

    @GetMapping
    CollectionResponse<CourseDto> getAll(CourseFilters courseFilters) {
        return courseService.getAll(courseFilters);
    }

    @GetMapping("/{courseId}")
    CourseDto getCoursetId(@PathVariable String courseId) {
        return courseService.getCourseId(courseId);
    }

    @GetMapping("/{courseId}/students")
    List<StudentDto> getCourseIdStudents(@PathVariable String courseId) {
        return courseService.getCourseIdStudents(courseId);
    }

    @PostMapping
    Course addCourse(@RequestBody Course newCourse) {
        return courseService.addCourse(newCourse);
    }

    @PostMapping("/{courseId}/students")
    CourseStudent addStudentCourseId(@PathVariable String studentId, @PathVariable String courseId) {
        return courseService.addStudentIdCourseId(studentId, courseId);
    }
}
