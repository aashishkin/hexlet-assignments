package exercise.model;

import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Table;
import static jakarta.persistence.GenerationType.IDENTITY;

import lombok.*;

// BEGIN
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@EqualsAndHashCode(of = {"title", "price"})
public class Product {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String title;

    private int price;
}

// END
