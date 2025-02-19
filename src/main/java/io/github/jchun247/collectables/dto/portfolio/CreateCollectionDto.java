package io.github.jchun247.collectables.dto.portfolio;

import lombok.Data;

@Data
public class CreateCollectionDto {
    private String auth0Id;
    private String name;
    private String description;
    private boolean isPublic;
}
