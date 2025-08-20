package sv.edu.udb.spring_database_springdata.repository.domain;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    @Id
    private Long id;
    private String title;
    private LocalDate postDate;
}