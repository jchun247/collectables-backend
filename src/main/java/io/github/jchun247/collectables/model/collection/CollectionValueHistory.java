package io.github.jchun247.collectables.model.collection;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "collection_value_history")
@Data
@NoArgsConstructor
public class CollectionValueHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "portfolio_id", nullable = false)
    private Collection portfolio;

    private Double value;
    private LocalDateTime timestamp;
}
