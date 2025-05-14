package io.github.jchun247.collectables.service.collection;

import io.github.jchun247.collectables.dto.collection.*;
import io.github.jchun247.collectables.exception.ResourceNotFoundException;
import io.github.jchun247.collectables.mapper.CollectionMapper;
import io.github.jchun247.collectables.model.card.Card;
import io.github.jchun247.collectables.model.collection.*;
import io.github.jchun247.collectables.model.user.UserEntity;
import io.github.jchun247.collectables.repository.card.CardRepository;
import io.github.jchun247.collectables.repository.collection.CollectionCardRepository;
import io.github.jchun247.collectables.repository.collection.CollectionRepository;
import io.github.jchun247.collectables.repository.collection.PortfolioValueHistoryRepository;
import io.github.jchun247.collectables.repository.user.UserRepository;
import io.github.jchun247.collectables.security.VerifyCollectionAccess;
import io.github.jchun247.collectables.security.VerifyCollectionViewAccess;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class CollectionServiceImpl implements CollectionService {
    private final CollectionRepository collectionRepository;
    private final CollectionCardRepository collectionCardRepository;
    private final PortfolioValueHistoryRepository portfolioValueHistoryRepository;
    private final UserRepository userRepository;
    private final CardRepository cardRepository;
    private final CollectionMapper collectionMapper;

    @Override
    @Transactional
    public CollectionListDTO createCollectionList(CreateCollectionListDTO createCollectionListDTO) {
        UserEntity user = findUserByAuth0IdOrThrow(createCollectionListDTO.getAuth0Id());

        CollectionList collectionList = CollectionList.builder()
                .name(createCollectionListDTO.getName() != null ? createCollectionListDTO.getName() : "New Collection")
                .description(createCollectionListDTO.getDescription() != null ? createCollectionListDTO.getDescription() : "")
                .isPublic(createCollectionListDTO.isPublic())
                .isFavourite(false)
                .numProducts(0)
                .currentValue(BigDecimal.ZERO)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .user(user)
                .listType(createCollectionListDTO.getListType())
                .build();

        collectionRepository.save(collectionList);
        return collectionMapper.toCollectionListDto(collectionList);
    }

    @Override
    @Transactional
    public PortfolioDTO createPortfolio(CreatePortfolioDTO createPortfolioDto) {
        UserEntity user = findUserByAuth0IdOrThrow(createPortfolioDto.getAuth0Id());

        Portfolio portfolio = Portfolio.builder()
                .name(createPortfolioDto.getName() != null ? createPortfolioDto.getName() : "New Collection")
                .description(createPortfolioDto.getDescription() != null ? createPortfolioDto.getDescription() : "")
                .isPublic(createPortfolioDto.isPublic())
                .isFavourite(false)
                .numProducts(0)
                .currentValue(BigDecimal.ZERO)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .user(user)
                .totalCostBasis(createPortfolioDto.getTotalCostBasis())
                .build();

        collectionRepository.save(portfolio);
        return collectionMapper.toPortfolioDto(portfolio);
    }

    @Override
    @Transactional
    @VerifyCollectionAccess
    public void deleteCollection(Long collectionId) {
        Collection collection = collectionRepository.findById(collectionId)
                .orElseThrow(() -> new ResourceNotFoundException("Collection not found with id: " + collectionId));
        collectionRepository.delete(collection);
    }

    @Override
    @Transactional
    @VerifyCollectionAccess
    public CollectionCardDTO addCardToCollection(Long collectionId, AddCardToCollectionDTO addCardToCollectionDTO) {
        Collection collection = collectionRepository.findById(collectionId)
                .orElseThrow(() -> new ResourceNotFoundException("Collection not found with id: " + collectionId));

        CollectionCard collectionCard = collectionCardRepository.findByCollectionIdAndCardIdAndConditionAndCostBasisAndPurchaseDate(
                    collectionId,
                    addCardToCollectionDTO.getCardId(),
                    addCardToCollectionDTO.getCondition(),
                    addCardToCollectionDTO.getCostBasis(),
                    addCardToCollectionDTO.getPurchaseDate())
                .orElseGet(() -> {
                    Card card = cardRepository.findById(addCardToCollectionDTO.getCardId())
                            .orElseThrow(() -> new ResourceNotFoundException("Card not found with id: " + addCardToCollectionDTO.getCardId()));

                    return CollectionCard.builder()
                        .collection(collection)
                        .card(card)
                        .condition(addCardToCollectionDTO.getCondition())
                        .costBasis(addCardToCollectionDTO.getCostBasis())
                        .purchaseDate(addCardToCollectionDTO.getPurchaseDate())
                        .quantity(0)
                        .build();
                });

        // Update quantity
        collectionCard.setQuantity(collectionCard.getQuantity() + addCardToCollectionDTO.getQuantity());

        collectionRepository.save(collection);
        CollectionCard savedCard = collectionCardRepository.save(collectionCard);

        triggerCollectionUpdate(collection.getId());
        log.info("Added/updated card {} to collection {} and triggered value update.", savedCard.getCard().getId(), collection.getId());
        return collectionMapper.toCollectionCardDto(savedCard);
    }

    @Override
    @Transactional
    @VerifyCollectionAccess
    public void deleteCardFromCollection(Long collectionId, Long collectionCardId, int quantity) {
        Collection collection = collectionRepository.findById(collectionId)
                .orElseThrow(() -> new ResourceNotFoundException("Collection not found with id: " + collectionId));
        CollectionCard collectionCard = collectionCardRepository.findById(collectionCardId)
                .orElseThrow(() -> new ResourceNotFoundException("Collection card not found with id: " + collectionCardId));
        int newCardQuantity = collectionCard.getQuantity() - quantity;
        if (newCardQuantity < 0) {
            throw new IllegalArgumentException("Cannot remove more cards than are in the collection");
        } else if (newCardQuantity == 0) {
            collectionCardRepository.delete(collectionCard);
        } else {
            collectionCard.setQuantity(newCardQuantity);
            collectionCardRepository.save(collectionCard);
        }

        collectionRepository.save(collection);
        triggerCollectionUpdate(collection.getId());
    }

    @Override
    @Transactional(readOnly = true)
    @VerifyCollectionViewAccess
    public CollectionDTO getCollectionDetails(Long collectionId) {
        log.debug("Fetching details for collection ID: {}", collectionId);
        Collection collection = collectionRepository.findById(collectionId)
                .orElseThrow(() -> new ResourceNotFoundException("Collection not found with id: " + collectionId));

        // Type check and specific mappings
        if (collection instanceof Portfolio) {
            return collectionMapper.toPortfolioDto((Portfolio) collection);
        } else if (collection instanceof CollectionList) {
            return collectionMapper.toCollectionListDto((CollectionList) collection);
        } else {
            // Should ideally not reach here if the data is consistent
            log.error("Unknown collection type found for ID: {}", collectionId);
            throw new IllegalArgumentException("Unknown collection type found for ID: " + collectionId);
        }
    }

    @Override
    @Transactional(readOnly = true)
    @VerifyCollectionViewAccess
    public Page<CollectionCardDTO> getCollectionCards(Long collectionId, Pageable pageable) {
        log.debug("Fetching cards for collection ID: {}", collectionId);
        Page<CollectionCard> collectionCardsPage = collectionCardRepository.findDetailedByCollectionId(collectionId, pageable);
        return collectionCardsPage.map(collectionMapper::toCollectionCardDto);
    }

    @Override
    @Transactional(readOnly = true)
    @VerifyCollectionViewAccess
    public Page<PortfolioValueHistoryDTO> getPortfolioValueHistory(Long collectionId, Pageable pageable) {
        log.debug("Fetching value history for portfolio (collection) ID: {}", collectionId);
        Page<PortfolioValueHistory> valueHistoryPage = portfolioValueHistoryRepository.findAllByPortfolioId(collectionId, pageable);
        return valueHistoryPage.map(collectionMapper::toPortfolioValueHistoryDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CollectionDTO> getCollectionsByUserId(String targetUserAuth0Id, Pageable pageable) {
        String requestingUserAuth0Id = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            requestingUserAuth0Id = authentication.getName();
        }
        Page<Collection> collectionsPage = collectionRepository.findVisibleCollectionsByUserId(
                targetUserAuth0Id,
                requestingUserAuth0Id,
                pageable);
        return collectionsPage.map(collectionMapper::toCollectionDto);
    }

    @Transactional
    protected Collection triggerCollectionUpdate(Long collectionId) {
        Collection collection = collectionRepository.findById(collectionId)
                .orElseThrow(() -> new ResourceNotFoundException("Collection not found with id: " + collectionId + " during update trigger."));

        // Calculate common values using repository methods
        BigDecimal newCollectionValue = collectionRepository.calculateCollectionValue(collectionId);
        int newNumProducts = collectionRepository.calculateCollectionSize(collectionId);

        // Update common fields
        collection.setCurrentValue(newCollectionValue);
        collection.setNumProducts(newNumProducts);
        collection.setUpdatedAt(LocalDateTime.now());

        // Handle portfolio specific updates
        if (collection instanceof Portfolio portfolio) {
            BigDecimal newTotalCostBasis = collectionRepository.calculateCollectionTotalCostBasis(collectionId);
            portfolio.setTotalCostBasis(newTotalCostBasis);
            log.info("Updated Portfolio-specific fields for ID: {}. New Cost Basis: {}", collectionId, newTotalCostBasis);
        } else {
            log.info("Updated common fields for CollectionList ID: {}", collectionId);
        }

        Collection savedCollection = collectionRepository.save(collection);
        log.info("Triggered value update complete for Collection ID: {}. New Value: {}, NumProducts: {}",
                collectionId, newCollectionValue, newNumProducts);
        return savedCollection;
    }

    @Override
    @Scheduled(cron = "0 0 0 * * *") // Runs every day at midnight
    @Transactional
    public void updateAllCollections() {
        int pageNumber = 0;
        int pageSize = 100; // Adjust batch size
        Page<Collection> collectionPage;
        int updatedCount = 0;
        int historyCreatedCount = 0;
        int errorCount = 0;

        do {
            collectionPage = collectionRepository.findAllCollectionsPaged(PageRequest.of(pageNumber, pageSize));
            log.info("Processing page {} with {} collections.", pageNumber, collectionPage.getNumberOfElements());

            for (Collection collection : collectionPage.getContent()) {
                try {
                    // NOTE: If using pages, triggerCollectionValueUpdate might need adjustments
                    // if it relies on specific transactional boundaries not spanning the whole page.
                    // It might be safer to recalculate/update directly here or call trigger method carefully.
                    Collection updatedCollection = triggerCollectionUpdate(collection.getId()); // Assuming trigger works per item transactionally
                    updatedCount++;

                    if (updatedCollection instanceof Portfolio portfolio) {
                        PortfolioValueHistory historyEntry = new PortfolioValueHistory();
                        historyEntry.setPortfolio(portfolio);
                        historyEntry.setValue(portfolio.getCurrentValue());
                        historyEntry.setTimestamp(portfolio.getUpdatedAt());
                        portfolioValueHistoryRepository.save(historyEntry);
                        historyCreatedCount++;
                    }
                } catch (Exception e) {
                    errorCount++;
                    log.error("Failed to update collection {} during scheduled job (page {}): {}", collection.getId(), pageNumber, e.getMessage(), e);
                }
            }
            pageNumber++;
        } while (collectionPage.hasNext());
        log.info("Finished scheduled collection value update. Collections processed: {}, History entries created: {}, Errors: {}", updatedCount, historyCreatedCount, errorCount);
    }

    private UserEntity findUserByAuth0IdOrThrow(String auth0Id) {
        return userRepository.findByAuth0Id(auth0Id).orElseThrow(() ->
                new ResourceNotFoundException("User not found with auth0Id: " + auth0Id));
    }
}
