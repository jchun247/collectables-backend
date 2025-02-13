package io.github.jchun247.collectables.model.user;

import io.github.jchun247.collectables.model.card.Card;
import io.github.jchun247.collectables.model.card.CardPrice;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "user_portfolio")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPortfolio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private int numProducts;

    @OneToMany(mappedBy = "portfolio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserPortfolioValueHistory> valueHistory;

    @OneToMany
    @JoinColumn(name = "portfolio_id")
    private List<Card> cards;

    //TODO: flatmap flattens all these prices into a single stream, need to only take values from the condition of the card being in the portfolio
    public double calculateCurrentValue() {
        return cards.stream()
                .flatMap(card -> card.getPrices().stream())
                .mapToDouble(CardPrice::getPrice)
                .sum();
    }
}
