package ro.fasttrackit.curs11.repository.course;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import ro.fasttrackit.curs11.model.entity.Course;
import ro.fasttrackit.curs11.model.filters.CourseFilters;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CourseDao {

    private final MongoTemplate mongo;

    public Page<Course> getAllWithFilters(CourseFilters filters) {

        List<Criteria> criterias = new ArrayList<>();
        Optional.ofNullable(filters.getStudentId())
                .ifPresent(element -> criterias.add(Criteria.where("studentId").is(element)));

        Criteria criteria = new Criteria().andOperator(criterias.toArray(new Criteria[0]));
        Pageable pageable = PageRequest.of(0, 10, Sort.by("studentId"));

        Query query = Query.query(criteria);

        List<Course> courses = getAllCoursesWithCriteria(criterias, query);

        return PageableExecutionUtils.getPage(
                courses,
                pageable,
                () -> getNumberPages(criterias, criteria));
    }

    List<Course> getAllCoursesWithCriteria(List<Criteria> criterias, Query query) {
        List<Course> courses;
        if (criterias.isEmpty()) {
            courses = mongo.findAll(Course.class);
        } else {
            courses = mongo.find(
                    query,
                    Course.class);
        }
        return courses;
    }

    Long getNumberPages(List<Criteria> criterias, Criteria criteria) {
        long pagesNumber;
        if (criterias.isEmpty()) {
            pagesNumber = mongo.findAll(Course.class).size();
        } else {
            pagesNumber = mongo.count(Query.query(criteria), Course.class);
        }
        return pagesNumber;
    }

}
