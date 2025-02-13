package io.github.jchun247.collectables.model.user;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_portfolio_value_history")
@Data
@NoArgsConstructor
public class UserPortfolioValueHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "portfolio_id", nullable = false)
    private UserPortfolio portfolio;

    private Double value;
    private LocalDateTime timestamp;
}
