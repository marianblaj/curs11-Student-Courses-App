package ro.fasttrackit.curs11.model.filters;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
public class CourseFilters {
    String studentId;
}
