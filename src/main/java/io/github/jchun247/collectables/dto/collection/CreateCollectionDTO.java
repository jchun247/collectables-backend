package io.github.jchun247.collectables.dto.collection;

import io.github.jchun247.collectables.model.collection.CollectionType;
import lombok.Data;

@Data
public class CreateCollectionDTO {
    private String auth0Id;
    private String name;
    private String description;
    private CollectionType type;
    private boolean isPublic;
}
