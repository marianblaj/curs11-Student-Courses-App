package ro.fasttrackit.curs11.loader;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ro.fasttrackit.curs11.model.entity.Student;
import ro.fasttrackit.curs11.repository.student.StudentRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    private final StudentRepository studentRepository;

    @Override
    public void run(String... args) {
        studentRepository.saveAll(List.of(
                Student.builder()
                        .name("Alina")
                        .age(22)
                        .build(),
                Student.builder()
                        .name("Sandu")
                        .age(19)
                        .build(),
                Student.builder()
                        .name("Ion")
                        .age(23)
                        .build(),
                Student.builder()
                        .name("Daniel")
                        .age(21)
                        .build()
        ));
    }
}
