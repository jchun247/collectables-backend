package io.github.jchun247.collectables.dto.collection;

import io.github.jchun247.collectables.dto.card.BasicCardDTO;
import io.github.jchun247.collectables.model.card.CardCondition;
import io.github.jchun247.collectables.model.card.CardFinish;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CollectionCardDTO {
    private Long id;
    private Long collectionId;
    private CardCondition condition;
    private CardFinish finish;
    private long quantity;
    private BasicCardDTO card;
    private BigDecimal currentValue;
    private BigDecimal totalCostBasis;
    private BigDecimal unrealizedGain;
    private BigDecimal realizedGain;
}
