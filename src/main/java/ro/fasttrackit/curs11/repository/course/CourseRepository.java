package ro.fasttrackit.curs11.repository.course;

import org.springframework.data.mongodb.repository.MongoRepository;
import ro.fasttrackit.curs11.model.entity.Course;

public interface CourseRepository extends MongoRepository<Course, String> {
}
