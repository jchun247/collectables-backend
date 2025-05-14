package io.github.jchun247.collectables.dto.collection;

import io.github.jchun247.collectables.model.collection.CollectionType;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class CollectionDTO {
    private Long id;
    private String name;
    private String description;
    private CollectionType collectionType;
    private boolean isPublic;
    private boolean isFavourite;
    private int numProducts;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private BigDecimal currentValue;
}
