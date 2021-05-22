package ro.fasttrackit.curs11.repository.student;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ro.fasttrackit.curs11.model.entity.Student;

@Repository
public interface StudentRepository extends MongoRepository<Student, String> {

}
