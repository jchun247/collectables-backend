package io.github.jchun247.collectables.dto.collection;

import io.github.jchun247.collectables.model.card.CardCondition;
import io.github.jchun247.collectables.model.card.CardFinish;
import io.github.jchun247.collectables.model.collection.TransactionType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CreateTransactionDTO {
    @NotNull(message = "Condition cannot be null")
    private CardCondition condition;

    @NotNull(message = "Finish cannot be null")
    private CardFinish finish;

    @NotNull(message = "Transaction type cannot be null")
    private TransactionType transactionType;

    @Positive(message = "Quantity must be a positive number")
    private int quantity;

    @NotNull(message = "Purchase date cannot be null")
    private LocalDate purchaseDate;

    @NotNull(message = "Cost basis cannot be null")
    private BigDecimal costBasis;
}
