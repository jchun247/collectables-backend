package io.github.jchun247.collectables.service.card;

import io.github.jchun247.collectables.dto.card.CardDto;
import io.github.jchun247.collectables.dto.PagedResponse;
import io.github.jchun247.collectables.exception.ResourceNotFoundException;
import io.github.jchun247.collectables.model.card.*;
import io.github.jchun247.collectables.repository.card.CardRepository;
import io.github.jchun247.collectables.repository.card.CardSetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService{

    private final CardRepository cardRepository;
    private final CardSetRepository cardSetRepository;

    @Override
    @Transactional
    public CardDto createCard(CreateCardRequest cardRequest) {
        // Fetch the cardSet entity from the code
        // TODO: handle case where game in set code table does not match game in card request
        CardSet cardSet = cardSetRepository.findByCode(cardRequest.getSetCode()).orElseThrow(() -> new ResourceNotFoundException("Invalid set code: " + cardRequest.getSetCode()));

        // set card attributes from request
        Card newCard = new Card();
        newCard.setName(cardRequest.getName());
        newCard.setGame(cardRequest.getGame());
        newCard.setSet(cardSet);
        newCard.setSetNumber(cardRequest.getSetNumber());
        newCard.setRarity(cardRequest.getRarity());
        newCard.setImageUrl(cardRequest.getImageUrl());

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
    @Transactional(readOnly = true)
    public PagedResponse<CardDto> getCards(int page, int size, List<CardGame> games,
                                           String setCode, CardRarity rarity, CardCondition condition,
                                           String sortOption, Double minPrice, Double maxPrice, String query) {

//        List<Sort.Order> orders = createSortOrders(sort);
        Sort sort = switch (sortOption) {
            case "name" -> Sort.by(Sort.Direction.ASC, "c.name");
            case "name-desc" -> Sort.by(Sort.Direction.DESC, "c.name");
            case "price-asc" -> Sort.by(Sort.Direction.ASC, "p.price");
            case "price-desc" -> Sort.by(Sort.Direction.DESC, "p.price");
            default -> Sort.unsorted();
        };

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Card> cardPage = cardRepository.findByFilters(
                games, setCode, rarity, condition, query,
                minPrice == null ? 0.0 : minPrice,
                maxPrice == null ? Double.MAX_VALUE : maxPrice,
                pageable
        );

        List<CardDto> cardDtos = cardPage.getContent().stream()
                .map(CardDto::new).toList();

        return new PagedResponse<>(cardDtos, cardPage);

    }

    /* Used for multiple sort orders, may use in the future */
//    private List<Sort.Order> createSortOrders(String[] sort) {
//        List<Sort.Order> orders = new ArrayList<>();
//
//        if (sort[0].contains(",")) {
//            for (String sortOrder : sort) {
//                String[] _sort = sortOrder.split(",");
//                orders.add(new Sort.Order(getSortDirection(_sort[1]), _sort[0]));
//            }
//        } else {
//            orders.add(new Sort.Order(getSortDirection(sort[1]), sort[0]));
//        }
//
//        return orders;
//    }

//    private Sort.Direction getSortDirection(String direction) {
//        return direction.equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
//    }

    @Override
    public Card getCardById(Long id) {
        return cardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Card not found with id: " + id));
    }
}
