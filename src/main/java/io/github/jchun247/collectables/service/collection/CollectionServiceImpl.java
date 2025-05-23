package io.github.jchun247.collectables.service.collection;

import io.github.jchun247.collectables.dto.collection.*;
import io.github.jchun247.collectables.exception.ResourceNotFoundException;
import io.github.jchun247.collectables.mapper.CollectionMapper;
import io.github.jchun247.collectables.model.card.Card;
import io.github.jchun247.collectables.model.collection.*;
import io.github.jchun247.collectables.model.user.UserEntity;
import io.github.jchun247.collectables.repository.card.CardRepository;
import io.github.jchun247.collectables.repository.collection.CollectionCardRepository;
import io.github.jchun247.collectables.repository.collection.CollectionCardTransactionHistoryRepository;
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
import org.springframework.lang.Nullable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class CollectionServiceImpl implements CollectionService {
    private final CollectionRepository collectionRepository;
    private final CollectionCardRepository collectionCardRepository;
    private final CollectionCardTransactionHistoryRepository collectionCardTransactionHistoryRepository;
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
    public CollectionDTO updateCollectionDetails(Long collectionId, UpdateCollectionDTO updateCollectionDTO) {
        Collection collection = collectionRepository.findById(collectionId)
                .orElseThrow(() -> new ResourceNotFoundException("Collection not found with id: " + collectionId));

        // Update fields
        if (updateCollectionDTO.getName() != null) {
            collection.setName(updateCollectionDTO.getName());
        }
        if (updateCollectionDTO.getDescription() != null) {
            collection.setDescription(updateCollectionDTO.getDescription());
        }
        collection.setPublic(updateCollectionDTO.isPublic());
        collection.setFavourite(updateCollectionDTO.isFavourite());

        Collection updatedCollection = collectionRepository.save(collection);
        return collectionMapper.toCollectionDto(updatedCollection);
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
        // Check if collection and card exist
        Collection collection = collectionRepository.findById(collectionId)
                .orElseThrow(() -> new ResourceNotFoundException("Collection not found with id: " + collectionId));

        Card card = cardRepository.findById(addCardToCollectionDTO.getCardId())
                .orElseThrow(() -> new ResourceNotFoundException("Card not found with id: " + addCardToCollectionDTO.getCardId()));

        CollectionCard collectionCard = collectionCardRepository.findByCollectionIdAndCardIdAndConditionAndFinish(
                    collectionId,
                    addCardToCollectionDTO.getCardId(),
                    addCardToCollectionDTO.getCondition(),
                    addCardToCollectionDTO.getFinish())
                .orElse(null);

        if (collectionCard == null) {
            // If the CollectionCard variant doesn't exist in the collection, create a new one.
            collectionCard = CollectionCard.builder()
                    .collection(collection)
                    .card(card)
                    .condition(addCardToCollectionDTO.getCondition())
                    .finish(addCardToCollectionDTO.getFinish())
                    .quantity(0)
                    .build();
            // Save the newly created CollectionCard to ensure it has an ID before proceeding
            collectionCard = collectionCardRepository.save(collectionCard);
        }

        // Update quantity of card in collection
        collectionCard.setQuantity(collectionCard.getQuantity() + addCardToCollectionDTO.getQuantity());
        CollectionCard savedCollectionCard = collectionCardRepository.save(collectionCard);

        // If collection is a portfolio, transaction history record needs to be created
        if (collection instanceof Portfolio) {
            // Ensure the savedCollectionCard has an ID. This should always be true now.
            if (savedCollectionCard.getId() == null) {
                log.error("Critical error: CollectionCard ID is null after save for cardId: {}. Cannot create transaction history.", addCardToCollectionDTO.getCardId());
                throw new IllegalStateException("Failed to obtain ID for CollectionCard, transaction cannot be logged.");
            }

            CollectionCardTransactionHistory newTransaction = CollectionCardTransactionHistory.builder()
                            .collectionCard(savedCollectionCard)
                            .condition(addCardToCollectionDTO.getCondition())
                            .finish(addCardToCollectionDTO.getFinish())
                            .quantity(addCardToCollectionDTO.getQuantity())
                            .purchaseDate(addCardToCollectionDTO.getPurchaseDate())
                            .costBasis(addCardToCollectionDTO.getCostBasis())
                            .build();

            collectionCardTransactionHistoryRepository.save(newTransaction);
        }

        triggerCollectionUpdate(collection.getId());
        log.info("Added/updated card {} (CollectionCard ID: {}) in collection {} and triggered value update.",
                savedCollectionCard.getCard().getId(), savedCollectionCard.getId(), collection.getId());
        return collectionMapper.toCollectionCardDto(savedCollectionCard);
    }

    @Override
    @Transactional
    @VerifyCollectionAccess
    public void deleteCardFromCollection(Long collectionId, Long collectionCardId, int quantityToRemove) {
        Collection collection = collectionRepository.findById(collectionId)
                .orElseThrow(() -> new ResourceNotFoundException("Collection not found with id: " + collectionId));
        CollectionCard collectionCard = collectionCardRepository.findById(collectionCardId)
                .orElseThrow(() -> new ResourceNotFoundException("Collection card not found with id: " + collectionCardId));

        // Ensure the card belongs to the specified collection
        if (!collectionCard.getCollection().getId().equals(collectionId)) {
            throw new IllegalArgumentException("CollectionCard with id " + collectionCardId +
                    " does not belong to collection with id " + collectionId);
        }

        int newCardQuantity = collectionCard.getQuantity() - quantityToRemove;
        if (newCardQuantity < 0) {
            throw new IllegalArgumentException("Cannot remove more cards (" + quantityToRemove +
                    ") than are in the collection (" + collectionCard.getQuantity() + ")");
        } else if (newCardQuantity == 0) {
            // If the quantity becomes zero, the CollectionCard itself is deleted.
            // If this collection is a Portfolio, its transaction history should also be removed.
            if (collection instanceof Portfolio) {
                // Ensure collectionCard.getId() is not null before attempting to delete history
                if (collectionCard.getId() != null) {
                    collectionCardTransactionHistoryRepository.deleteByCollectionCardId(collectionCard.getId());
                    log.info("Deleted transaction history for CollectionCard ID: {}", collectionCard.getId());
                } else {
                    // This case should ideally not be reached if the collectionCard was properly managed.
                    log.warn("CollectionCard ID is null for collectionCardId {} being deleted. Cannot delete associated transaction history.", collectionCardId);
                }
            }
            collectionCardRepository.delete(collectionCard);
            log.info("Deleted CollectionCard ID: {} from collection ID: {}", collectionCardId, collectionId);
        } else {
            // Otherwise, just update the quantity.
            collectionCard.setQuantity(newCardQuantity);
            collectionCardRepository.save(collectionCard);
            log.info("Updated quantity for CollectionCard ID: {} in collection ID: {}. New quantity: {}",
                    collectionCardId, collectionId, newCardQuantity);
        }

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
    @Transactional
    @VerifyCollectionViewAccess
    public Page<CollectionCardTransactionHistoryDTO> getCollectionCardTransactionHistory(
            Long collectionId,
            Long collectionCardId,
            Pageable pageable) {
        log.debug("Fetching transaction history for CollectionCard ID: {}", collectionCardId);
        Page<CollectionCardTransactionHistory> transactionHistoryPage = collectionCardTransactionHistoryRepository
                .findTransactionHistoriesByCollectionCardId(collectionCardId, pageable);
        return transactionHistoryPage.map(collectionMapper::toCollectionCardTransactionHistoryDto);
    }

    @Override
    @Transactional
    // add verification
    public CollectionCardTransactionHistoryDTO updateTransactionDetails(Long collectionId, Long transactionId, UpdateTransactionDTO updateTransactionDTO) {
        CollectionCardTransactionHistory transactionHistory = collectionCardTransactionHistoryRepository.findById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction history not found with id: " + transactionId));

        if (updateTransactionDTO.getQuantity() > 0) {
            transactionHistory.setQuantity(updateTransactionDTO.getQuantity());
        }
        if (updateTransactionDTO.getCostBasis() != null) {
            transactionHistory.setCostBasis(updateTransactionDTO.getCostBasis());
        }
        if (updateTransactionDTO.getPurchaseDate() != null) {
            transactionHistory.setPurchaseDate(updateTransactionDTO.getPurchaseDate());
        }
        CollectionCardTransactionHistory updatedTransaction = collectionCardTransactionHistoryRepository.save(transactionHistory);
        triggerCollectionUpdate(collectionId);
        return collectionMapper.toCollectionCardTransactionHistoryDto(updatedTransaction);
    }

    @Override
    @Transactional
    // add verification
    public void deleteTransaction(Long collectionId, Long transactionId) {
        CollectionCardTransactionHistory collectionCardTransactionHistory = collectionCardTransactionHistoryRepository.findById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Collection card transaction not found with id: " + transactionId));
        collectionCardTransactionHistoryRepository.delete(collectionCardTransactionHistory);
        triggerCollectionUpdate(collectionId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CollectionDTO> getCollectionsByUserId(String targetUserAuth0Id, @Nullable CollectionType collectionType, Pageable pageable) {
        String requestingUserAuth0Id = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            requestingUserAuth0Id = authentication.getName();
        }

        Class<? extends Collection> entityClassFilter = null; // Use the base class Collection
        if (collectionType != null) {
            entityClassFilter = switch (collectionType) {
                case PORTFOLIO -> Portfolio.class;
                case LIST -> CollectionList.class;
            };
        }

        Page<Collection> collectionsPage = collectionRepository.findVisibleCollectionsByUserId(
                targetUserAuth0Id,
                requestingUserAuth0Id,
                entityClassFilter,
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
