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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService{

    private final CardRepository cardRepository;
    private final CardMapper cardMapper;

//    @Override
//    @Transactional(readOnly = true)
//    public PagedResponse<CardDto> getCards(int page, int size, List<CardGame> games,
//                                           String setCode, CardRarity rarity, CardCondition condition,
//                                           String sortOption, BigDecimal minPrice, BigDecimal maxPrice, String searchQuery) {
//
//        Sort sort = switch (sortOption) {
//            case "name" -> Sort.by(Sort.Direction.ASC, "c.name");
//            case "name-desc" -> Sort.by(Sort.Direction.DESC, "c.name");
//            case "price-asc" -> Sort.by(Sort.Direction.ASC, "p.price");
//            case "price-desc" -> Sort.by(Sort.Direction.DESC, "p.price");
//            default -> Sort.unsorted();
//        };
//
//        Pageable pageable = PageRequest.of(page, size, sort);

//        Page<Card> cardPage = cardRepository.findByFilters(
//                games, setCode, rarity, condition, searchQuery,
//                minPrice == null ? 0.0 : minPrice,
//                maxPrice == null ? Double.MAX_VALUE : maxPrice,
//                pageable
//        );

//        List<CardDto> cardDTOs = cardPage.getContent().stream()
//                .map(CardDto::fromEntity).toList();
//
//        return new PagedResponse<>(cardDTOs, cardPage);
//
//        return null;
//
//    }

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
