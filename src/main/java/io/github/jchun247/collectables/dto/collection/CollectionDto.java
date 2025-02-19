package io.github.jchun247.collectables.dto.collection;

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

    public static CollectionDto fromEntity(Collection collection) {
        return CollectionDto.builder()
                .id(collection.getId())
                .name(collection.getName())
                .description(collection.getDescription())
                .isPublic(collection.isPublic())
                .build();
    }
}
