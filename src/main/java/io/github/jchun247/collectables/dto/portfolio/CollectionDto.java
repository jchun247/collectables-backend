package io.github.jchun247.collectables.dto.portfolio;

import io.github.jchun247.collectables.model.collection.Collection;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CollectionDto {
    private Long id;
    private String name;
    private String description;
    private boolean isPublic;
    private int quantity;

    public static CollectionDto fromEntity(Collection portfolio) {
        return CollectionDto.builder()
                .id(portfolio.getId())
                .name(portfolio.getName())
                .description(portfolio.getDescription())
                .isPublic(portfolio.isPublic())
                .build();
    }
}
