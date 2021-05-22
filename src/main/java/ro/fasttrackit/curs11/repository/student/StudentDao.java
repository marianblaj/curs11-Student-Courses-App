package ro.fasttrackit.curs11.repository.student;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import ro.fasttrackit.curs11.model.filters.StudentFilters;
import ro.fasttrackit.curs11.model.entity.Student;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class StudentDao {

    private final MongoTemplate mongo;

    public Page<Student> getAllWithFilters(StudentFilters filters) {

        List<Criteria> criterias = new ArrayList<>();
        Optional.ofNullable(filters.getName())
                .ifPresent(element -> criterias.add(Criteria.where("name").is(element)));
        Optional.ofNullable(filters.getAge())
                .ifPresent(element -> criterias.add(Criteria.where("age").is(Integer.parseInt(element))));
        Optional.ofNullable(filters.getMinAge())
                .ifPresent(element -> criterias.add(Criteria.where("minAge").gt(Integer.parseInt(element))));
        Optional.ofNullable(filters.getMaxAge())
                .ifPresent(element -> criterias.add(Criteria.where("maxAge").lt(Integer.parseInt(element))));
        Optional.ofNullable(filters.getStudentId())
                .ifPresent(element -> criterias.add(Criteria.where("studentId").is(element)));

        Criteria criteria = new Criteria().andOperator(criterias.toArray(new Criteria[0]));
        Pageable pageable = PageRequest.of(0, 10, Sort.by("name"));

        Query query = Query.query(criteria);

        List<Student> students = getAllStudentsWithCriteria(criterias, query);

        return PageableExecutionUtils.getPage(
                students,
                pageable,
                () -> getNumberPages(criterias, criteria));
    }

    List<Student> getAllStudentsWithCriteria(List<Criteria> criterias, Query query) {
        List<Student> students;
        if (criterias.isEmpty()) {
            students = mongo.findAll(Student.class);
        } else {
            students = mongo.find(
                    query,
                    Student.class);
        }
        return students;
    }

    Long getNumberPages(List<Criteria> criterias, Criteria criteria) {
        long pagesNumber;
        if (criterias.isEmpty()) {
            pagesNumber = mongo.findAll(Student.class).size();
        } else {
            pagesNumber = mongo.count(Query.query(criteria), Student.class);
        }
        return pagesNumber;
    }
}
