package io.github.jchun247.collectables.service;

import io.github.jchun247.collectables.model.Card;

import java.util.List;

public interface CardService {
    Card createCard(Card card);
    List<Card> getAllCards();
    Card getCardById(Long id);
}
