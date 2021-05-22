package ro.fasttrackit.curs11.service.student;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import ro.fasttrackit.curs11.exception.ResourceNotFoundException;
import ro.fasttrackit.curs11.mapper.StudentMapper;
import ro.fasttrackit.curs11.model.CollectionResponse;
import ro.fasttrackit.curs11.model.PageInfo;
import ro.fasttrackit.curs11.model.StudentCourse;
import ro.fasttrackit.curs11.model.dto.StudentDto;
import ro.fasttrackit.curs11.model.entity.CourseStudent;
import ro.fasttrackit.curs11.model.entity.Student;
import ro.fasttrackit.curs11.model.filters.StudentFilters;
import ro.fasttrackit.curs11.repository.course.CourseRepository;
import ro.fasttrackit.curs11.repository.courseStudent.CourseStudentRepository;
import ro.fasttrackit.curs11.repository.student.StudentDao;
import ro.fasttrackit.curs11.repository.student.StudentRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Builder
@RequiredArgsConstructor
public class StudentService {
    private final StudentDao studentDao;
    private final StudentRepository repo;
    private final StudentValidator studentValidator;
    private final CourseStudentRepository courseStudentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final StudentMapper studentMapper;


    public CollectionResponse<StudentDto> getAll(StudentFilters studentFilters) {

        Page<Student> page = studentDao.getAllWithFilters(studentFilters);

        return CollectionResponse.<StudentDto>builder()
                .content(studentMapper.toDtoList(page.getContent()))
                .pageInfo(PageInfo.builder()
                        .totalPages(page.getTotalPages())
                        .totalElements(page.getNumberOfElements())
                        .crtPage(page.getNumber())
                        .pageSize(page.getSize())
                        .build())
                .build();

    }

    public Student addStudent(Student newStudent) {
        return repo.save(newStudent);
    }

    public StudentDto getStudentId(String studentId) {
        studentValidator.validateExistsOrThrow(studentId);
        Student dbStudent = repo.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Couldn`t find student with id " + studentId));
        return StudentDto.builder()
                .name(dbStudent.getName())
                .age(dbStudent.getAge()).build();
    }

    public List<StudentCourse> getStudentIdCourses(String studentId) {
        studentValidator.validateExistsOrThrow(studentId);

        List<CourseStudent> dbCourseStudent = courseStudentRepository.findAllByStudentId(studentId);
        return dbCourseStudent.stream()
                .map(element -> {
                    Student dbStudent = studentRepository.findById(studentId).orElseThrow();
                    return StudentCourse.builder()
                            .name(dbStudent.getName())
                            .age(dbStudent.getAge())
                            .discipline(courseRepository
                                    .findById(element.getCourseId())
                                    .orElseThrow().getDiscipline())
                            .build();
                })
                .collect(Collectors.toList());
    }
}

