package io.github.jchun247.collectables.dto.card;

import io.github.jchun247.collectables.model.card.CardCondition;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Valid
public class CreateCardPriceRequestDto {
    @NotNull(message = "Card condition is required")
    private CardCondition condition;
    @Min(value = 0, message = "Price must be at least 0")
    private BigDecimal price;
}
