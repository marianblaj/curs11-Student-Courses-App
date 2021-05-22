package ro.fasttrackit.curs11.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import ro.fasttrackit.curs11.model.entity.Student;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
public class StudentDaoTest {
    @Autowired
    private MongoTemplate mongo;

    @Test
    @DisplayName("WHEN  getAllWithFilters is called THEN all students with filters is returned")
    void getAllWithFilters() {
        mongo.insertAll(List.of(
                Student.builder()
                        .name("Alina")
                        .age(22).build(),
                Student.builder()
                        .name("Andrei")
                        .age(28).build()
        ));

        List<Criteria> criterias = List.of(Criteria.where("name").is("Alina"), Criteria.where("age").is(22));
        Criteria criteria = new Criteria().andOperator(criterias.toArray(new Criteria[0]));
        Query query = Query.query(criteria);

        List<Student> result = mongo.find(query, Student.class);

        assertThat(result).isNotEmpty();
        assertThat(result.get(0)).extracting("name", "age")
                .containsExactly("Alina", 22);
    }
}
