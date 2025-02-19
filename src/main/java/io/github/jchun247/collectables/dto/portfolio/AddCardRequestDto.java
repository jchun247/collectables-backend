package io.github.jchun247.collectables.dto.portfolio;

import io.github.jchun247.collectables.model.card.CardCondition;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddCardRequestDto {
    @NotNull
    private Long portfolioId;
    @NotNull
    private Long cardId;
    @NotNull
    private CardCondition condition;
    @Min(1)
    private int quantity;
}
