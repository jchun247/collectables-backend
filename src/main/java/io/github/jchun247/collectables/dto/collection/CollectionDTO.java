package io.github.jchun247.collectables.dto.collection;

import io.github.jchun247.collectables.model.collection.CollectionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
