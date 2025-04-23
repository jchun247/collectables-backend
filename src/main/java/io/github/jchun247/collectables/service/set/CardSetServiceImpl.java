package io.github.jchun247.collectables.service.set;

import io.github.jchun247.collectables.dto.card.BasicCardSetDTO;
import io.github.jchun247.collectables.mapper.CardMapper;
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
    private final CardMapper cardMapper;

    @Override
    @Transactional(readOnly = true)
    public List<BasicCardSetDTO> getCardSetsBySeries(CardSeries series) {
        return cardSetRepository.findAllBySeriesWithCollections(series)
                .stream()
                .map(cardMapper::toBasicCardSetDTO)
                .toList();
    }

    @Override
    public List<CardSeries> getAllCardSeries() {
        return cardSetRepository.findAllSeries();
    }
}
