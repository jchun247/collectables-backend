package io.github.jchun247.collectables.model.card;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="sets")
@Data
public class CardSet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String code;

    private String name;
}
