package ro.fasttrackit.curs11.model;

import lombok.*;

@Data
@Builder
public class StudentCourse {
    private String name;
    private int age;
    private String discipline;
    private int grade;
}
