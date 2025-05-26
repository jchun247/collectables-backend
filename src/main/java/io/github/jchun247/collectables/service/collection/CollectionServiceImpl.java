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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .user(user)
                .listType(createCollectionListDTO.getListType())
                .build();

        collectionRepository.save(collectionList);
        // Set numProducts and currentValue to zero for new collection lists
        CollectionListDTO dto = collectionMapper.toCollectionListDto(collectionList);
        dto.setNumProducts(0);
        dto.setCurrentValue(BigDecimal.ZERO);
        return dto;
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
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .user(user)
                .build();

        collectionRepository.save(portfolio);
        // Set numProducts, currentValue, total cost basis to zero for new portfolios
        PortfolioDTO dto = collectionMapper.toPortfolioDto(portfolio);
        dto.setNumProducts(0);
        dto.setCurrentValue(BigDecimal.ZERO);
        dto.setTotalCostBasis(BigDecimal.ZERO);
        return dto;
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

        return populateCollectionDto(updatedCollection);
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

        // Create collectionCard if it doesn't exist
        CollectionCard collectionCard = collectionCardRepository.findByCollectionIdAndCardIdAndConditionAndFinish(
                    collectionId,
                    addCardToCollectionDTO.getCardId(),
                    addCardToCollectionDTO.getCondition(),
                    addCardToCollectionDTO.getFinish())
                .orElseGet(() -> {
                    CollectionCard newCollectionCard = CollectionCard.builder()
                            .collection(collection)
                            .card(card)
                            .condition(addCardToCollectionDTO.getCondition())
                            .finish(addCardToCollectionDTO.getFinish())
                            .build();
                    return collectionCardRepository.save(newCollectionCard);
                });

        // Add a new transaction history record and save it
        CollectionCardTransactionHistory newTransaction = CollectionCardTransactionHistory.builder()
                .collectionCard(collectionCard)
                .condition(addCardToCollectionDTO.getCondition())
                .finish(addCardToCollectionDTO.getFinish())
                .quantity(addCardToCollectionDTO.getQuantity())
                .purchaseDate(addCardToCollectionDTO.getPurchaseDate())
                .costBasis(addCardToCollectionDTO.getCostBasis() != null ? addCardToCollectionDTO.getCostBasis() : BigDecimal.ZERO)
                .build();

        collectionCardTransactionHistoryRepository.save(newTransaction);
        log.info("Added/updated card {} (CollectionCard ID: {}) to collection {}.",
                collectionCard.getCard().getId(), collectionCard.getId(), collection.getId());

        CollectionCardDTO dto = collectionMapper.toCollectionCardDto(collectionCard);
        dto.setQuantity(collectionRepository.calculateCollectionSize(collectionId));
        return dto;
    }

    @Override
    @Transactional
    @VerifyCollectionAccess
    public void deleteCardFromCollection(Long collectionId, Long collectionCardId, DeleteCardFromCollectionDTO deleteCardFromCollectionDTO) {
        if (deleteCardFromCollectionDTO.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity to remove must be positive.");
        }
        int quantityToRemove = deleteCardFromCollectionDTO.getQuantity();

        // Check if collection and collectionCard exist
        Collection collection = collectionRepository.findById(collectionId)
                .orElseThrow(() -> new ResourceNotFoundException("Collection not found with id: " + collectionId));
        CollectionCard collectionCard = collectionCardRepository.findById(collectionCardId)
                .orElseThrow(() -> new ResourceNotFoundException("Collection card not found with id: " + collectionCardId));

        // Ensure the card belongs to the specified collection
        if (!collectionCard.getCollection().getId().equals(collectionId)) {
            throw new IllegalArgumentException("CollectionCard with id " + collectionCardId +
                    " does not belong to collection with id " + collectionId);
        }

        int currentQuantity = collectionCardTransactionHistoryRepository.sumQuantityByCollectionCardId(collectionCardId);

        if (quantityToRemove > currentQuantity) {
            throw new IllegalArgumentException("Cannot remove more cards (" + quantityToRemove +
                    ") than are in the collection (" + currentQuantity + ")");
        }

        if (quantityToRemove == currentQuantity) {
            // If the final quantity will be zero, remove the card and all its history.
            collectionCardRepository.delete(collectionCard);
            log.info("Deleted CollectionCard ID: {} and its history from collection ID: {} as quantity became zero.",
                    collectionCardId, collectionId);
        } else {
            // If there's remaining quantity, record the removal as a negative transaction.
            CollectionCardTransactionHistory removalTransaction = CollectionCardTransactionHistory.builder()
                    .collectionCard(collectionCard)
                    .condition(collectionCard.getCondition())
                    .finish(collectionCard.getFinish())
                    .quantity(-quantityToRemove)
                    .purchaseDate(LocalDate.now())
                    .costBasis(deleteCardFromCollectionDTO.getCostBasis())
                    .build();

            collectionCardTransactionHistoryRepository.save(removalTransaction);
            log.info("Recorded removal of {} item(s) for CollectionCard ID: {}. New calculated quantity: {}",
                    quantityToRemove, collectionCardId, (currentQuantity - quantityToRemove));
        }
    }

    @Override
    @Transactional(readOnly = true)
    @VerifyCollectionViewAccess
    public CollectionDTO getCollectionDetails(Long collectionId) {
        log.debug("Fetching details for collection ID: {}", collectionId);
        Collection collection = collectionRepository.findById(collectionId)
                .orElseThrow(() -> new ResourceNotFoundException("Collection not found with id: " + collectionId));

        return populateCollectionDto(collection);
    }

    @Override
    @Transactional(readOnly = true)
    @VerifyCollectionViewAccess
    public Page<CollectionCardDTO> getCollectionCards(Long collectionId, Pageable pageable) {
        log.debug("Fetching cards for collection ID: {}", collectionId);

        Page<CollectionCard> collectionCardsPage = collectionCardRepository.findPageByCollectionId(collectionId, pageable);

        if (!collectionCardsPage.hasContent()) {
            return Page.empty(pageable);
        }

        List<Long> collectionCardIds = collectionCardsPage.stream().map(CollectionCard::getId).toList();
        List<Long> cardIds = collectionCardsPage.stream().map(cc -> cc.getCard().getId()).toList();

        Map<Long, Integer> quantityMap = collectionCardTransactionHistoryRepository.sumQuantitiesByCollectionCardIds(collectionCardIds)
                .stream()
                .collect(Collectors.toMap(
                        CollectionCardQuantity::collectionCardId,
                        stats -> stats.totalQuantity().intValue()
                ));

        Map<Long, Card> cardDetailsMap = cardRepository.findCardsWithDetailsByIds(cardIds)
                .stream()
                .collect(Collectors.toMap(Card::getId, card -> card));

        return collectionCardsPage.map(collectionCard -> {
            // Replace the potentially lazy Card object with our fully loaded one.
            collectionCard.setCard(cardDetailsMap.get(collectionCard.getCard().getId()));

            // The mapper can now access all details (prices, images) without triggering lazy loads.
            CollectionCardDTO dto = collectionMapper.toCollectionCardDto(collectionCard);

            // Set the correct quantity from our map.
            dto.setQuantity(quantityMap.getOrDefault(collectionCard.getId(), 0));
            return dto;
        });
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
    @VerifyCollectionAccess
    public CollectionCardTransactionHistoryDTO updateTransactionDetails(Long collectionId, Long transactionId, UpdateTransactionDTO updateTransactionDTO) {
        CollectionCardTransactionHistory transactionHistory = collectionCardTransactionHistoryRepository.findById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction history not found with id: " + transactionId));
        if (!transactionHistory.getCollectionCard().getCollection().getId().equals(collectionId)) {
            throw new IllegalArgumentException("Transaction " + transactionId + " does not belong to collection " + collectionId);
        }

        int newQuantity = updateTransactionDTO.getQuantity();

        if (newQuantity == 0) {
            throw new IllegalArgumentException("Updating quantity to zero is not allowed. Please use the delete transaction endpoint instead.");
        }

        // Prevent changing the transaction type (buy vs. sell)
        // This check fails if one quantity is positive and the other is negative.
        if ((long) transactionHistory.getQuantity() * newQuantity < 0) {
            throw new IllegalArgumentException("Cannot change the transaction from a purchase to a sale (or vice-versa). Please create a new transaction to record a sale.");
        }

        transactionHistory.setQuantity(newQuantity);

        if (updateTransactionDTO.getCostBasis() != null) {
            transactionHistory.setCostBasis(updateTransactionDTO.getCostBasis());
        }
        if (updateTransactionDTO.getPurchaseDate() != null) {
            transactionHistory.setPurchaseDate(updateTransactionDTO.getPurchaseDate());
        }
        CollectionCardTransactionHistory updatedTransaction = collectionCardTransactionHistoryRepository.save(transactionHistory);
        return collectionMapper.toCollectionCardTransactionHistoryDto(updatedTransaction);
    }

    @Override
    @Transactional
    @VerifyCollectionAccess
    public void deleteTransaction(Long collectionId, Long transactionId) {
        CollectionCardTransactionHistory collectionCardTransactionHistory = collectionCardTransactionHistoryRepository.findById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Collection card transaction not found with id: " + transactionId));

        CollectionCard parentCollectionCard = collectionCardTransactionHistory.getCollectionCard();

        if (!parentCollectionCard.getCollection().getId().equals(collectionId)) {
            throw new IllegalArgumentException("Transaction " + transactionId + " does not belong to collection " + collectionId);
        }

        collectionCardTransactionHistoryRepository.delete(collectionCardTransactionHistory);
        log.info("Deleted transaction ID: {}", transactionId);

        long remainingCount = collectionCardTransactionHistoryRepository.countByCollectionCardId(parentCollectionCard.getId());
        log.debug("Remaining transactions for CollectionCard ID {}: {}", parentCollectionCard.getId(), remainingCount);

        if (remainingCount == 0) {
            collectionCardRepository.delete(parentCollectionCard);
            log.info("Deleted CollectionCard ID: {} as it has no remaining transactions.", parentCollectionCard.getId());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CollectionDTO> getCollectionsByUserId(String targetUserAuth0Id, @Nullable CollectionType collectionType, Pageable pageable) {
        String requestingUserAuth0Id = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            requestingUserAuth0Id = authentication.getName();
        }

        Class<? extends Collection> entityClassFilter = null;
        if (collectionType != null) {
            entityClassFilter = switch (collectionType) {
                case PORTFOLIO -> Portfolio.class;
                case LIST -> CollectionList.class;
            };
        }

        // 1. Fetch the page of entities
        Page<Collection> collectionsPage = collectionRepository.findVisibleCollectionsByUserId(
                targetUserAuth0Id,
                requestingUserAuth0Id,
                entityClassFilter,
                pageable);

        if (!collectionsPage.hasContent()) {
            return Page.empty(pageable);
        }

        // 2. Get IDs and fetch all stats in one batch query
        List<Long> collectionIds = collectionsPage.stream()
                .map(Collection::getId)
                .toList();

        Map<Long, CollectionStats> statsMap = collectionRepository.getBulkStatsForCollections(collectionIds).stream()
                .collect(Collectors.toMap(CollectionStats::collectionId, stats -> stats));

        // 3. Map to DTOs, combining entities with their fetched stats
        return collectionsPage.map(collection -> {
            // Use the helper method you created earlier, but provide it with stats
            return populateCollectionDtoWithStats(collection, statsMap.getOrDefault(collection.getId(),
                    new CollectionStats(collection.getId(), 0L, BigDecimal.ZERO, BigDecimal.ZERO)));
        });

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
//                    Collection updatedCollection = triggerCollectionUpdate(collection.getId()); // Assuming trigger works per item transactionally
                    updatedCount++;

                    if (collection instanceof Portfolio portfolio) {
                        PortfolioValueHistory historyEntry = new PortfolioValueHistory();
                        historyEntry.setPortfolio(portfolio);
                        historyEntry.setValue(collectionRepository.calculateCollectionValue(collection.getId()));
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


    /* Helper Methods */
    private UserEntity findUserByAuth0IdOrThrow(String auth0Id) {
        return userRepository.findByAuth0Id(auth0Id).orElseThrow(() ->
                new ResourceNotFoundException("User not found with auth0Id: " + auth0Id));
    }

    private CollectionDTO populateCollectionDto(Collection collection) {
        Long collectionId = collection.getId();

        // 1. Calculate the dynamic values
        int numProducts = collectionRepository.calculateCollectionSize(collectionId);
        BigDecimal currentValue = collectionRepository.calculateCollectionValue(collectionId);

        // 2. Use the mapper to handle polymorphism and map base fields
        CollectionDTO dto;
        if (collection instanceof Portfolio) {
            PortfolioDTO portfolioDTO = collectionMapper.toPortfolioDto((Portfolio) collection);
//            dto = collectionMapper.toPortfolioDto((Portfolio) collection);
            portfolioDTO.setTotalCostBasis(collectionRepository.calculateCollectionTotalCostBasis(collectionId));
            dto = portfolioDTO;
        } else { // Handles CollectionList and any other non-portfolio types
            dto = collectionMapper.toCollectionListDto((CollectionList) collection);
        }

        // 3. Set the calculated values on the DTO
        dto.setNumProducts(numProducts);
        dto.setCurrentValue(currentValue);

        return dto;
    }

    private CollectionDTO populateCollectionDtoWithStats(Collection collection, CollectionStats stats) {
        CollectionDTO dto;
        if (collection instanceof Portfolio) {
            dto = collectionMapper.toPortfolioDto((Portfolio) collection);
            ((PortfolioDTO) dto).setTotalCostBasis(stats.totalCostBasis());
        } else {
            dto = collectionMapper.toCollectionListDto((CollectionList) collection);
        }
        dto.setNumProducts((int) stats.numProducts());
        dto.setCurrentValue(stats.currentValue());
        // The mapper correctly sets the collectionType via the abstract getter
        return dto;
    }

}
