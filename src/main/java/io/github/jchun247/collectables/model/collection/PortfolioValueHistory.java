package io.github.jchun247.collectables.model.collection;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name="portfolio_value_history")
@Data
@NoArgsConstructor
public class PortfolioValueHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "portfolio_id", nullable = false)
    private Portfolio portfolio;

    private BigDecimal value;
    private LocalDateTime timestamp;
}
