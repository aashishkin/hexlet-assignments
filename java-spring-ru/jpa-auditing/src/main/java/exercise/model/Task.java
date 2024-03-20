package exercise.model;

import jakarta.persistence.*;

import static jakarta.persistence.GenerationType.AUTO;
import static jakarta.persistence.GenerationType.IDENTITY;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

// BEGIN
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Task {
    @Id
    @GeneratedValue (strategy = IDENTITY)
    private Long id;

    @LastModifiedDate
    private LocalDate updatedAt;

    @CreatedDate
    private LocalDate createdAt;

    private String title;

    private String description;
}

// END
