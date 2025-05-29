//package io.github.jchun247.collectables.dto.collection;
//
//import io.github.jchun247.collectables.model.card.CardCondition;
//import io.github.jchun247.collectables.model.card.CardFinish;
//import jakarta.validation.constraints.Min;
//import jakarta.validation.constraints.NotNull;
//import lombok.Data;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//
//@Data
//public class DeleteCardFromCollectionDTO {
//    @NotNull
//    private CardCondition condition;
//    @NotNull
//    private CardFinish finish;
//    @Min(1)
//    private int quantity;
//    @NotNull
//    private LocalDate purchaseDate;
//    @NotNull
//    private BigDecimal costBasis;
//}
