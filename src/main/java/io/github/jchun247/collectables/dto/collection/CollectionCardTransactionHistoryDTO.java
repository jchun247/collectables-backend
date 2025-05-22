package io.github.jchun247.collectables.dto.collection;

import io.github.jchun247.collectables.model.card.CardCondition;
import io.github.jchun247.collectables.model.card.CardFinish;
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
public class CollectionCardTransactionHistoryDTO {
    private Long id;
    private CardCondition condition;
    private CardFinish finish;
    private int quantity;
    private LocalDate purchaseDate;
    private BigDecimal costBasis;
}
