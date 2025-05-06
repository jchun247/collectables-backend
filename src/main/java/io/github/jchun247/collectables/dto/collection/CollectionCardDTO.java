package io.github.jchun247.collectables.dto.collection;

import io.github.jchun247.collectables.model.card.CardCondition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
public class CollectionCardDTO {
    private Long id;
    private Long cardId;
    private Long collectionId;
    private CardCondition condition;
    private int quantity;
    private LocalDate purchaseDate;
    private BigDecimal costBasis;
}
