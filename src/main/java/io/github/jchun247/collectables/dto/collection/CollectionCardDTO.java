package io.github.jchun247.collectables.dto.collection;

import io.github.jchun247.collectables.dto.card.BasicCardDTO;
import io.github.jchun247.collectables.model.card.CardCondition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CollectionCardDTO {
    private Long id;
    private Long collectionId;
    private CardCondition condition;
    private int quantity;
    private LocalDate purchaseDate;
    private BigDecimal costBasis;
    private BasicCardDTO card;
}
