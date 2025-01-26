package io.github.jchun247.collectables.service;

import io.github.jchun247.collectables.dto.CardDto;
import io.github.jchun247.collectables.dto.PagedResponse;
import io.github.jchun247.collectables.exception.ResourceNotFoundException;
import io.github.jchun247.collectables.model.*;
import io.github.jchun247.collectables.repository.CardRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService{

    private final CardRepository cardRepository;

    @Override
    @Transactional
    public CardDto createCard(CreateCardRequest cardRequest) {
        // set card attributes from request
        Card newCard = new Card();
        newCard.setName(cardRequest.getName());
        newCard.setGame(cardRequest.getGame());
        newCard.setSet(cardRequest.getSet());
        newCard.setSetNumber(cardRequest.getSetNumber());
        newCard.setRarity(cardRequest.getRarity());

        for (CreateCardPriceRequest priceRequest : cardRequest.getPrices()) {
            CardPrice cardPrice = new CardPrice();
            cardPrice.setCondition(priceRequest.getCondition());
            cardPrice.setPrice(priceRequest.getPrice());
            newCard.addPrice(cardPrice);
        }

        Card savedCard = cardRepository.save(newCard);
        return new CardDto(savedCard);
    }

    @Override
    public PagedResponse<CardDto> getCards(int page, int size, String[] sort, String game,
                                           Long set, CardRarity rarity, CardCondition condition,
                                           Double minPrice, Double maxPrice) {

        List<Sort.Order> orders = createSortOrders(sort);
        Pageable pageable = PageRequest.of(page, size, Sort.by(orders));

        Page<Card> cardPage = cardRepository.findByFilters(
                game, set, rarity, condition,
                minPrice == null ? 0.0 : minPrice,
                maxPrice == null ? Double.MAX_VALUE : maxPrice,
                pageable
        );

        List<CardDto> cardDtos = cardPage.getContent().stream()
                .map(CardDto::new).toList();

        return new PagedResponse<>(cardDtos, cardPage);

    }

    private List<Sort.Order> createSortOrders(String[] sort) {
        List<Sort.Order> orders = new ArrayList<>();

        if (sort[0].contains(",")) {
            for (String sortOrder : sort) {
                String[] _sort = sortOrder.split(",");
                orders.add(new Sort.Order(getSortDirection(_sort[1]), _sort[0]));
            }
        } else {
            orders.add(new Sort.Order(getSortDirection(sort[1]), sort[0]));
        }

        return orders;
    }

    private Sort.Direction getSortDirection(String direction) {
        return direction.equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
    }

    @Override
    public Card getCardById(Long id) {
        return cardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Card not found with id: " + id));
    }
}
