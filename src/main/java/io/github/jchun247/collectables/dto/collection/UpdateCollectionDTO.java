package io.github.jchun247.collectables.dto.collection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCollectionDTO {
    private String name;
    private String description;
    private boolean isPublic;
    private boolean isFavourite;
}
