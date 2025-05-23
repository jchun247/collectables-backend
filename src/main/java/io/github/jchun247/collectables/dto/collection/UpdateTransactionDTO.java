package io.github.jchun247.collectables.dto.collection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTransactionDTO {
    private int quantity;
    private LocalDate purchaseDate;
    private BigDecimal costBasis;
}
