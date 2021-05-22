package ro.fasttrackit.curs11.repository.courseStudent;

import org.springframework.data.mongodb.repository.MongoRepository;
import ro.fasttrackit.curs11.model.entity.CourseStudent;

import java.util.List;

public interface CourseStudentRepository extends MongoRepository<CourseStudent, String> {
    List<CourseStudent> findAllByStudentId(String studentId);
    List<CourseStudent> findAllByCourseId(String courseId);
}
