package io.github.jchun247.collectables.dto.collection;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Set;

@Data
public class CollectionDTO {
    private Long id;
    private String name;
    private String description;
    private boolean isPublic;
    private boolean isFavourite;
    private int numProducts;
    private BigDecimal currentValue;
    private Set<CollectionValueHistoryDTO> valueHistory;
}
