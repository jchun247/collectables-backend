package io.github.jchun247.collectables.service.card;

import io.github.jchun247.collectables.dto.card.BasicCardDTO;
import io.github.jchun247.collectables.dto.PagedResponse;
import io.github.jchun247.collectables.dto.card.CardDTO;
import io.github.jchun247.collectables.dto.card.CardPriceHistoryDTO;
import io.github.jchun247.collectables.model.card.CardCondition;
import io.github.jchun247.collectables.model.card.CardFinish;
import io.github.jchun247.collectables.model.card.CardGame;
import io.github.jchun247.collectables.model.card.CardRarity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface CardService {
    PagedResponse<BasicCardDTO> getCards(int page, int size, List<CardGame> games,
                                         String setId, CardRarity rarity, CardCondition condition,
                                         String sortOption, BigDecimal minPrice, BigDecimal maxPrice,
                                         String searchQuery, CardFinish finish);
    BasicCardDTO getCardWithBasicData(Long id);
    CardDTO getCardWithAllData(Long id);
    Page<CardPriceHistoryDTO> getCardPriceHistory(
            Long cardId, LocalDateTime startDate,
            LocalDateTime endDate, Pageable pageable);
}
