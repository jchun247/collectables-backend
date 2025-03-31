package io.github.jchun247.collectables.dto.collection;

import io.github.jchun247.collectables.model.collection.CollectionValueHistory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class CollectionValueHistoryDTO {
    private Long id;
    private BigDecimal value;
    private LocalDateTime timestamp;

    public static CollectionValueHistoryDTO fromEntity(CollectionValueHistory history) {
        return CollectionValueHistoryDTO.builder()
                .id(history.getId())
                .value(history.getValue())
                .timestamp(history.getTimestamp())
                .build();
    }
}
