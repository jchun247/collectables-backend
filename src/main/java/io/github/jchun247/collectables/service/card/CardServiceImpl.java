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

        String sortField;
        Sort.Direction direction = Sort.Direction.ASC;
        boolean sortByPrice = false;

        if (sortOption != null && sortOption.endsWith("-desc")) {
            direction = Sort.Direction.DESC;
        }

        if (sortOption != null && sortOption.startsWith("price")) {
            sortField = "p.price";
            sortByPrice = true;
        } else if (sortOption != null && sortOption.startsWith("rarity")) {
            sortField = "rarity";
        } else {
            sortField = "name"; // Default
        }

        Sort sort = Sort.by(direction, sortField);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Card> cardPage;

        // Call the appropriate repository method based on the sort criteria
        if (sortByPrice) {
            cardPage = cardRepository.findAndPaginateSortedByPrice(
                    games, setId, rarity,
                    condition == null ? CardCondition.NEAR_MINT : condition,
                    finish, searchQuery,
                    minPrice == null ? BigDecimal.ZERO : minPrice,
                    maxPrice == null ? MAX_PRICE : maxPrice,
                    pageable
            );
        } else {
            cardPage = cardRepository.findAndPaginate(
                    games, setId, rarity,
                    condition == null ? CardCondition.NEAR_MINT : condition,
                    finish, searchQuery,
                    minPrice == null ? BigDecimal.ZERO : minPrice,
                    maxPrice == null ? MAX_PRICE : maxPrice,
                    pageable
            );
        }

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
