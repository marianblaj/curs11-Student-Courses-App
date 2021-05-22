package ro.fasttrackit.curs11.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import ro.fasttrackit.curs11.model.entity.Course;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
public class CourseDaoTest {
    @Autowired
    private MongoTemplate mongo;

    @Test
    @DisplayName("WHEN  getAllWithFilters is called THEN all courses with filters is returned")
    void getAllWithFilters() {
        mongo.insertAll(List.of(
                Course.builder()
                        .discipline("Math")
                        .description("hard").build(),
                Course.builder()
                        .discipline("history").build()
        ));

        List<Criteria> criterias = List.of(Criteria.where("discipline").is("history"));
        Criteria criteria = new Criteria().andOperator(criterias.toArray(new Criteria[0]));
        Query query = Query.query(criteria);

        List<Course> result = mongo.find(query, Course.class);

        assertThat(result).isNotEmpty();
        assertThat(result.get(0)).extracting("discipline", "description")
                .containsExactly("history", null);
    }
}
