package ro.fasttrackit.curs11.mapper;

import org.springframework.stereotype.Component;
import ro.fasttrackit.curs11.model.dto.StudentDto;
import ro.fasttrackit.curs11.model.entity.Student;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class StudentMapper implements Mapper<StudentDto, Student> {

    public StudentDto toDto(Student student) {
        if (student == null) {
            return null;
        }
        var target = new StudentDto();
        target.setName(student.getName());
        target.setAge(student.getAge());

        return target;
    }

    public Student toEntity(StudentDto studentDto) {

        if (studentDto == null) {
            return null;
        }
        var target = new Student();
        target.setName(studentDto.getName());
        target.setAge(studentDto.getAge());
        return target;
    }

    public List<StudentDto> toDtoList(List<Student> studentList) {
        return studentList.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
