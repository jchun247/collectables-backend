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
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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

        // Create the Sort object from the request parameter
        String effectiveSortOption = (sortOption == null || sortOption.isBlank()) ? "name-asc" : sortOption;
        String[] sortParts = effectiveSortOption.split("-");
        Sort.Direction direction = "desc".equalsIgnoreCase(sortParts[1]) ? Sort.Direction.DESC : Sort.Direction.ASC;

        Sort sort = Sort.by(direction, sortParts[0]);
        Pageable pageable = PageRequest.of(page, size, sort);

        // Build the criteria object
        CardSearchCriteria criteria = CardSearchCriteria.builder()
                .games(games)
                .setId(setId)
                .rarity(rarity)
                .condition(condition == null ? CardCondition.NEAR_MINT : condition)
                .finish(finish)
                .query(searchQuery)
                .minPrice(minPrice == null ? BigDecimal.ZERO : minPrice)
                .maxPrice(maxPrice == null ? MAX_PRICE : maxPrice)
                .build();

        // Execute the SINGLE dynamic ID query
        Page<Long> idPage = cardRepository.findCardIdsByCriteria(criteria, pageable);
        List<Long> cardIds = idPage.getContent();

        if (cardIds.isEmpty()) {
            return new PagedResponse<>(Page.empty(pageable));
        }

        // Execute the SECOND query to get full data for the page of IDs
        List<Card> unsortedCards = cardRepository.findFullCardsByIds(cardIds);

        // 5. Perform an efficient in-memory sort to match the ID order.
        Map<Long, Card> cardMap = unsortedCards.stream()
                .collect(Collectors.toMap(Card::getId, Function.identity()));

        List<Card> cards = cardIds.stream()
                .map(cardMap::get)
                .collect(Collectors.toList());

        // Build the final Page and return the response
        Page<Card> cardPage = new PageImpl<>(cards, pageable, idPage.getTotalElements());
        Page<BasicCardDTO> dtoPage = cardPage.map(cardMapper::toBasicDTO);
        return new PagedResponse<>(dtoPage);
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
