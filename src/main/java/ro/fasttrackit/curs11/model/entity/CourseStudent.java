package ro.fasttrackit.curs11.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Document(collection = "CourseStudent")
public class CourseStudent {

    @Id
    String id;

    private final String studentId;
    private final String courseId;
    private final int grade;
}
