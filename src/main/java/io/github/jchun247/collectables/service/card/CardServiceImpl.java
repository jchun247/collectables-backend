package io.github.jchun247.collectables.service.card;

import io.github.jchun247.collectables.dto.card.*;
import io.github.jchun247.collectables.dto.PagedResponse;
import io.github.jchun247.collectables.exception.ResourceNotFoundException;
import io.github.jchun247.collectables.mapper.CardMapper;
import io.github.jchun247.collectables.model.card.*;
import io.github.jchun247.collectables.repository.card.CardPriceHistoryRepository;
import io.github.jchun247.collectables.repository.card.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService{

    private final CardRepository cardRepository;
    private final CardPriceHistoryRepository cardPriceHistoryRepository;
    private final CardMapper cardMapper;

    private static final BigDecimal MAX_PRICE = new BigDecimal("9999999.99");

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<BasicCardDTO> getCards(int page, int size, List<CardGame> games,
                                           String setId, CardRarity rarity, CardCondition condition,
                                           String sortOption, BigDecimal minPrice, BigDecimal maxPrice,
                                                String searchQuery, final CardFinish finish) {

        List<Long> allMatchingIds = cardRepository.findMatchingCardIds(
                games, setId, rarity,
                condition == null ? CardCondition.NEAR_MINT : condition,
                finish, searchQuery,
                minPrice == null ? BigDecimal.ZERO : minPrice,
                maxPrice == null ? MAX_PRICE : maxPrice
        );

        if (allMatchingIds.isEmpty()) {
            return new PagedResponse<>(Page.empty());
        }

        // Count total matches for pagination
        long totalElements = cardRepository.countMatchingCardIds(
                games, setId, rarity,
                condition == null ? CardCondition.NEAR_MINT : condition,
                finish, searchQuery,
                minPrice == null ? BigDecimal.ZERO : minPrice,
                maxPrice == null ? MAX_PRICE : maxPrice
        );

        // Apply sorting and pagination
        List<Card> sortedCards;
        if (sortOption != null && (sortOption.equals("price-asc") || sortOption.equals("price-desc"))) {
            Sort priceSort = Sort.by(sortOption.equals("price-asc") ?
                    Sort.Direction.ASC : Sort.Direction.DESC, "p.price");
            sortedCards = cardRepository.findCardsByIdsSortedByPrice(
                    allMatchingIds,
                    condition == null ? CardCondition.NEAR_MINT : condition,
                    finish,
                    priceSort
            );
        } else {
            Sort nameSort = Sort.by("name-desc".equals(sortOption) ?
                    Sort.Direction.DESC : Sort.Direction.ASC, "name");
            sortedCards = cardRepository.findCardsByIdsSortedByName(allMatchingIds, nameSort);
        }

        // Apply pagination manually (after sorting)
        int fromIndex = page * size;
        int toIndex = Math.min(fromIndex + size, sortedCards.size());

        // Ensure fromIndex is within bounds
        if (fromIndex >= sortedCards.size()) {
            return new PagedResponse<>(Page.empty());
        }

        List<Card> pageOfCards = sortedCards.subList(fromIndex, toIndex);

        List<BasicCardDTO> basicCardDTOs = pageOfCards.stream()
                .map(cardMapper::toBasicDTO)
                .toList();

        Pageable pageable = PageRequest.of(page, size);
        Page<BasicCardDTO> pageOfBasicCardDTOs = new PageImpl<>(basicCardDTOs, pageable, totalElements);
        return new PagedResponse<>(pageOfBasicCardDTOs);
    }

    @Override
    @Transactional(readOnly = true)
    public BasicCardDTO getCardWithBasicData(Long id) {
        return cardMapper.toBasicDTO(cardRepository.findWithBasicDataById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Card not found with id: " + id)));
    }

    @Override
    @Transactional(readOnly = true)
    public CardDTO getCardWithAllData(Long id) {
        return cardMapper.toCardDTO(cardRepository.findWithAllDataById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Card not found with id: " + id)));
    }

    @Override
    public Page<CardPriceHistoryDTO> getCardPriceHistory(
            Long cardId,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Pageable pageable) {
        Page<CardPriceHistory> priceHistoryPage = cardPriceHistoryRepository.findByCardIdAndTimestampBetween(
                cardId, startDate, endDate, pageable);
        return priceHistoryPage.map(cardMapper::toCardPriceHistoryDTO);
    }
}
