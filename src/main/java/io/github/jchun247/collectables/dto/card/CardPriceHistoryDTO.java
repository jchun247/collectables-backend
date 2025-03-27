package io.github.jchun247.collectables.dto.card;

import io.github.jchun247.collectables.model.card.CardCondition;
import io.github.jchun247.collectables.model.card.CardFinish;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CardPriceHistoryDTO {
    @Enumerated(EnumType.STRING)
    private CardCondition condition;

    @Enumerated(EnumType.STRING)
    private CardFinish finish;

    private BigDecimal price;
    private LocalDateTime timestamp;
}
