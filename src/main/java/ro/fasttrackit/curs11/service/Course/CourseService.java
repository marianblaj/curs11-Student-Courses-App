package ro.fasttrackit.curs11.service.Course;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import ro.fasttrackit.curs11.exception.ResourceNotFoundException;
import ro.fasttrackit.curs11.mapper.CourseMapper;
import ro.fasttrackit.curs11.mapper.StudentMapper;
import ro.fasttrackit.curs11.model.CollectionResponse;
import ro.fasttrackit.curs11.model.PageInfo;
import ro.fasttrackit.curs11.model.dto.CourseDto;
import ro.fasttrackit.curs11.model.dto.StudentDto;
import ro.fasttrackit.curs11.model.entity.Course;
import ro.fasttrackit.curs11.model.entity.CourseStudent;
import ro.fasttrackit.curs11.model.entity.Student;
import ro.fasttrackit.curs11.model.StudentCourse;
import ro.fasttrackit.curs11.model.filters.CourseFilters;
import ro.fasttrackit.curs11.repository.course.CourseDao;
import ro.fasttrackit.curs11.repository.course.CourseRepository;
import ro.fasttrackit.curs11.repository.courseStudent.CourseStudentRepository;
import ro.fasttrackit.curs11.repository.student.StudentRepository;
import ro.fasttrackit.curs11.service.student.StudentValidator;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CourseService {
    private final CourseDao courseDao;
    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;
    private final CourseValidator courseValidator;
    private final StudentValidator studentvalidator;
    private final StudentMapper studentMapper;
    private final CourseStudentRepository courseStudentRepository;
    private final CourseMapper courseMapper;

    public CollectionResponse<CourseDto> getAll(CourseFilters courseFilters) {
        Page<Course> page = courseDao.getAllWithFilters(courseFilters);

        return CollectionResponse.<CourseDto>builder()
                .content(courseMapper.toDtoList(page.getContent()))
                .pageInfo(PageInfo.builder()
                        .totalPages(page.getTotalPages())
                        .totalElements(page.getNumberOfElements())
                        .crtPage(page.getNumber())
                        .pageSize(page.getSize())
                        .build())
                .build();
    }

    public Course addCourse(Course newCourse) {
        return courseRepository.save(newCourse);
    }

    public CourseDto getCourseId(String courseId) {
        courseValidator.validateExistsOrThrow(courseId);
        Course dbCourse = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Couldn`t find course with id " + courseId));
        return new CourseDto(dbCourse.getDiscipline());
    }


    public List<StudentDto> getCourseIdStudents(String courseId) {
        courseValidator.validateExistsOrThrow(courseId);

        List<CourseStudent> studentsId = courseStudentRepository.findAllByCourseId(courseId);
        List<Student> students = studentsId.stream()
                .map(element -> studentRepository.findById(element.getStudentId()).orElseThrow())
                .collect(Collectors.toList());

        return studentMapper.toDtoList(students);
    }

    public CourseStudent addStudentIdCourseId(String studentId, String courseId) {
        studentvalidator.validateExistsOrThrow(studentId);
        courseValidator.validateExistsOrThrow(courseId);

        return courseStudentRepository.save(
                CourseStudent.builder()
                        .courseId(courseId)
                        .studentId(studentId)
                        .build());

    }
}
