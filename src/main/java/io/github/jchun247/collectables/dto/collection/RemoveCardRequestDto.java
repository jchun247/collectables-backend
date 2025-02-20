package io.github.jchun247.collectables.dto.collection;

import io.github.jchun247.collectables.model.card.CardCondition;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Valid
public class RemoveCardRequestDto {
    @NotNull(message = "Card condition is required")
    private CardCondition condition;

    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;
}
