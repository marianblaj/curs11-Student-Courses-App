package ro.fasttrackit.curs11.mapper;

import org.springframework.stereotype.Component;
import ro.fasttrackit.curs11.model.dto.CourseDto;
import ro.fasttrackit.curs11.model.entity.Course;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CourseMapper implements Mapper<CourseDto, Course> {

    public CourseDto toDto(Course course) {
        if (course == null) {
            return null;
        }
        var target = new CourseDto();
        target.setDiscipline(course.getDiscipline());
        return target;
    }

    public Course toEntity(CourseDto courseDto) {

        if (courseDto == null) {
            return null;
        }
        var target = new Course();
        target.setDiscipline(courseDto.getDiscipline());
        return target;
    }

    public List<CourseDto> toDtoList(List<Course> studentList) {
        return studentList.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
