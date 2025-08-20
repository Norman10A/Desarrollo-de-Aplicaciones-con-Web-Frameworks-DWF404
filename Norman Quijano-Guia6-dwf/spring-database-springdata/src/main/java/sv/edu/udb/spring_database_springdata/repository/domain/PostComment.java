package sv.edu.udb.spring_database_springdata.repository.domain;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String comment;

    private LocalDate commentedAt;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;
}