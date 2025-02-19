package io.github.jchun247.collectables.service.collection;

import io.github.jchun247.collectables.dto.portfolio.CreateCollectionDto;
import io.github.jchun247.collectables.dto.portfolio.CollectionDto;
import io.github.jchun247.collectables.exception.ResourceNotFoundException;
import io.github.jchun247.collectables.model.card.Card;
import io.github.jchun247.collectables.model.card.CardCondition;
import io.github.jchun247.collectables.model.user.UserEntity;
import io.github.jchun247.collectables.model.collection.Collection;
import io.github.jchun247.collectables.model.collection.CollectionCard;
import io.github.jchun247.collectables.model.collection.CollectionValueHistory;
import io.github.jchun247.collectables.repository.card.CardRepository;
import io.github.jchun247.collectables.repository.collection.CollectionCardRepository;
import io.github.jchun247.collectables.repository.collection.CollectionRepository;
import io.github.jchun247.collectables.repository.collection.CollectionValueHistoryRepository;
import io.github.jchun247.collectables.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CollectionServiceImpl implements CollectionService {
    private final CollectionRepository collectionRepository;
    private final CollectionCardRepository collectionCardRepository;
    private final CollectionValueHistoryRepository collectionValueHistoryRepository;
    private final UserRepository userRepository;
    private final CardRepository cardRepository;


    @Override
    @Transactional
    // TODO: change method to return a DTO
    public void addCardToPortfolio(Long portfolioId, Long cardId, CardCondition condition, int quantity) {
        Collection portfolio = collectionRepository.findById(portfolioId)
                .orElseThrow(() -> new ResourceNotFoundException("Portfolio not found with id: " + portfolioId));
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new ResourceNotFoundException("Card not found with id: " + cardId));

        CollectionCard portfolioCard = new CollectionCard();
        portfolioCard.setPortfolio(portfolio);
        portfolioCard.setCard(card);
        portfolioCard.setCondition(condition);
        portfolioCard.setQuantity(quantity);
        collectionCardRepository.save(portfolioCard);

//        userPortfolioRepository.save(portfolio);
    }

    @Override
    public List<CollectionValueHistory> getPortfolioValueHistory(Long portfolioId) {
        return collectionValueHistoryRepository.findAllByPortfolioId(portfolioId);
    }

    @Override
    public void updatePortfolioValue(Collection portfolio) {
        double currentValue = portfolio.calculateCurrentValue();
        CollectionValueHistory valueHistory = new CollectionValueHistory();
        valueHistory.setPortfolio(portfolio);
        valueHistory.setValue(currentValue);
        valueHistory.setTimestamp(LocalDateTime.now());
        collectionValueHistoryRepository.save(valueHistory);
    }

    @Override
    @Scheduled(cron = "0 0 0 * * *") // Runs every day at midnight
    public void updateAllPortfolios() {
        List<Collection> portfolios = collectionRepository.findAll();
        for (Collection portfolio : portfolios) {
            updatePortfolioValue(portfolio);
        }
    }

    @Override
    @Transactional
    public CollectionDto createPortfolio(CreateCollectionDto createCollectionDto) {
        UserEntity user = userRepository.findByAuth0Id(createCollectionDto.getAuth0Id()).orElseThrow(() ->
                new ResourceNotFoundException("User not found with auth0Id: " + createCollectionDto.getAuth0Id()));

        Collection portfolio = Collection.builder()
                .name(createCollectionDto.getName() != null ? createCollectionDto.getName() : "New Portfolio")
                .description(createCollectionDto.getDescription() != null ? createCollectionDto.getDescription() : "")
                .isPublic(createCollectionDto.isPublic())
                .numProducts(0)
                .user(user)
                .build();

        collectionRepository.save(portfolio);
        return CollectionDto.fromEntity(portfolio);
    }
}
