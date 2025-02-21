package io.github.jchun247.collectables.dto.collection;

import io.github.jchun247.collectables.model.collection.Collection;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class CollectionDto {
    private Long id;
    private String name;
    private String description;
    private boolean isPublic;
    private int numProducts;
    private BigDecimal currentValue;

    public static CollectionDto fromEntity(Collection collection) {
        return CollectionDto.builder()
                .id(collection.getId())
                .name(collection.getName())
                .description(collection.getDescription())
                .isPublic(collection.isPublic())
                .numProducts(collection.getNumProducts())
                .currentValue(collection.calculateCurrentValue())
                .build();
    }
}
