package io.github.jchun247.collectables.dto.collection;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class CollectionDto {
    private Long id;
    private String name;
    private String description;
    private boolean isPublic;
    private boolean isFavourite;
    private int numProducts;
    private BigDecimal currentValue;
}
