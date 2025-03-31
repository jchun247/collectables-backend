package io.github.jchun247.collectables.dto.collection;

import io.github.jchun247.collectables.model.card.CardCondition;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddCardRequestDTO {
    @NotNull
    private Long collectionId;
    @NotNull
    private Long cardId;
    @NotNull
    private CardCondition condition;
    @Min(1)
    private int quantity;
}
