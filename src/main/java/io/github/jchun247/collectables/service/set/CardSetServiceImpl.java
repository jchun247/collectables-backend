package io.github.jchun247.collectables.service.set;

import io.github.jchun247.collectables.model.card.CardSeries;
import io.github.jchun247.collectables.model.card.CardSet;
import io.github.jchun247.collectables.repository.card.CardSetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CardSetServiceImpl implements CardSetService{

    private final CardSetRepository cardSetRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CardSet> getCardSetsBySeries(CardSeries series) {
        return cardSetRepository.findAllBySeriesWithCollections(series);
    }

    @Override
    public List<CardSeries> getAllCardSeries() {
        return cardSetRepository.findAllSeries();
    }
}
