package io.github.jchun247.collectables.dto.collection;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class DeleteCardFromCollectionDTO {
    @Min(1)
    private int quantity;
}
