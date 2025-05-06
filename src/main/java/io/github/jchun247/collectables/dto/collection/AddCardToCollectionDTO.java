package io.github.jchun247.collectables.dto.collection;

import io.github.jchun247.collectables.model.card.CardCondition;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class AddCardToCollectionDTO {
    @NotNull
    private Long cardId;
    @NotNull
    private CardCondition condition;
    @Min(1)
    private int quantity;
    private LocalDate purchaseDate;
    private BigDecimal costBasis;
}

