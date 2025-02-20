package io.github.jchun247.collectables.dto.collection;

import io.github.jchun247.collectables.model.card.CardCondition;
import io.github.jchun247.collectables.model.collection.CollectionCard;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CollectionCardDto {
    private Long id;
    private Long cardId;
    private Long collectionId;
    private CardCondition condition;
    private int quantity;
    // Optionally, you could include some denormalized data for convenience:
    private String cardName;
    private String collectionName;

    public static CollectionCardDto fromEntity(CollectionCard collectionCard) {
        return CollectionCardDto.builder()
                .id(collectionCard.getId())
                .cardId(collectionCard.getCard().getId())
                .collectionId(collectionCard.getCollection().getId())
                .condition(collectionCard.getCondition())
                .quantity(collectionCard.getQuantity())
                // Optional denormalized fields:
                .cardName(collectionCard.getCard().getName())
                .collectionName(collectionCard.getCollection().getName())
                .build();
    }
}
