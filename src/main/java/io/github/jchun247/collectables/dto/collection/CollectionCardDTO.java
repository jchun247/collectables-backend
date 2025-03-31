package io.github.jchun247.collectables.dto.collection;

import io.github.jchun247.collectables.model.card.CardCondition;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CollectionCardDTO {
    private Long id;
    private Long cardId;
    private Long collectionId;
    private CardCondition condition;
    private int quantity;
    // Denormalized data to avoid additional queries
    private String cardName;
    private String collectionName;
}
