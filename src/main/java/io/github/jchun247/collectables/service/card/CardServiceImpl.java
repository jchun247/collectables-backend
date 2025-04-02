package io.github.jchun247.collectables.service.card;

import io.github.jchun247.collectables.dto.card.*;
import io.github.jchun247.collectables.dto.PagedResponse;
import io.github.jchun247.collectables.exception.ResourceNotFoundException;
import io.github.jchun247.collectables.mapper.CardMapper;
import io.github.jchun247.collectables.model.card.*;
import io.github.jchun247.collectables.repository.card.CardRepository;
import jakarta.persistence.Basic;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService{

    private final CardRepository cardRepository;
    private final CardMapper cardMapper;

    private static final BigDecimal MAX_PRICE = new BigDecimal("9999999.99");

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<BasicCardDTO> getCards(int page, int size, List<CardGame> games,
                                           String setId, CardRarity rarity, CardCondition condition,
                                           String sortOption, BigDecimal minPrice, BigDecimal maxPrice,
                                                String searchQuery, CardFinish finish) {

        Sort sort = switch (sortOption) {
            case "name" -> Sort.by(Sort.Direction.ASC, "c.name");
            case "name-desc" -> Sort.by(Sort.Direction.DESC, "c.name");
            case "price-asc" -> Sort.by(Sort.Direction.ASC, "p.price");
            case "price-desc" -> Sort.by(Sort.Direction.DESC, "p.price");
            default -> Sort.unsorted();
        };

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Long> cardIdPage = cardRepository.findCardIdsByFilters(
                games, setId, rarity,
                condition == null ? CardCondition.NEAR_MINT : condition,
                finish,
                searchQuery,
                minPrice == null ? BigDecimal.ZERO : minPrice,
                maxPrice == null ? MAX_PRICE : maxPrice,
                pageable
        );

        List<Long> cardIds = cardIdPage.getContent();

        if (cardIds.isEmpty()) {
            return new PagedResponse<>(Collections.emptyList(), Page.empty());
        }

        List<Card> cards = cardRepository.findCardsByIds(cardIds);

        List<BasicCardDTO> basicCardDTOs = cards.stream()
                .map(cardMapper::toBasicDTO)
                .toList();

        Page<Card> cardPage = new PageImpl<>(
                cards,
                pageable,
                cardIdPage.getTotalElements()
        );

        return new PagedResponse<>(basicCardDTOs, cardPage);
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
}
