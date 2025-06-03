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
import org.springframework.data.domain.PageImpl;
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
import java.math.RoundingMode;
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

        CreateTransactionDTO createTransactionDTO = new CreateTransactionDTO();
        createTransactionDTO.setCondition(addCardToCollectionDTO.getCondition());
        createTransactionDTO.setFinish(addCardToCollectionDTO.getFinish());
        createTransactionDTO.setTransactionType(TransactionType.BUY);
        createTransactionDTO.setQuantity(addCardToCollectionDTO.getQuantity());
        createTransactionDTO.setPurchaseDate(addCardToCollectionDTO.getPurchaseDate() != null ? addCardToCollectionDTO.getPurchaseDate() : LocalDate.now());
        createTransactionDTO.setCostBasis(addCardToCollectionDTO.getCostBasis() != null ? addCardToCollectionDTO.getCostBasis() : BigDecimal.ZERO);

        addTransaction(collectionId, collectionCard.getId(), createTransactionDTO);

        log.info("Added/updated card {} (CollectionCard ID: {}) to collection {}.",
                collectionCard.getCard().getId(), collectionCard.getId(), collection.getId());

        int newQuantity = collectionCardTransactionHistoryRepository.sumQuantityByCollectionCardId(collectionCard.getId());
        CollectionCardDTO dto = collectionMapper.toCollectionCardDto(collectionCard);
        dto.setQuantity(newQuantity);
        return dto;
    }

    @Override
    @Transactional
    @VerifyCollectionAccess
    public void deleteCardFromCollection(Long collectionId, Long collectionCardId) {
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

        // Delete all transactions for this CollectionCard
        List<CollectionCardTransactionHistory> transactions = collectionCardTransactionHistoryRepository.findAllByCollectionCardId(collectionCardId);
        if (transactions.isEmpty()) {
            log.warn("No transactions found for CollectionCard ID: {}. Nothing to delete.", collectionCardId);
            return;
        }

        // Delete the CollectionCard itself if it has no remaining transactions
        collectionCardTransactionHistoryRepository.deleteAll(transactions);
        log.info("Deleted all transactions for CollectionCard ID: {} in Collection ID: {}", collectionCardId, collectionId);
        collectionCardRepository.delete(collectionCard);
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
    public CollectionCardDTO getCollectionCardDetails(Long collectionId, Long collectionCardId) {
        log.debug("Fetching details for CollectionCard ID: {} in Collection ID: {}", collectionCardId, collectionId);
        CollectionCard collectionCard = collectionCardRepository.findById(collectionCardId)
                .orElseThrow(() -> new ResourceNotFoundException("Collection card not found with id: " + collectionCardId));

        // Ensure the card belongs to the specified collection
        if (!collectionCard.getCollection().getId().equals(collectionId)) {
            throw new IllegalArgumentException("CollectionCard with id " + collectionCardId +
                    " does not belong to collection with id " + collectionId);
        }

        // Fetch stats for this specific card
        CollectionCardStats stats = collectionCardTransactionHistoryRepository
                .getBulkStatsForCollectionCards(List.of(collectionCardId))
                .stream()
                .map(row -> new CollectionCardStats(
                        (Long) row[0],                                  // collectionCardId
                        ((Number) row[1]).longValue(),                  // totalQuantity
                        (BigDecimal) row[2],                            // currentValue
                        (BigDecimal) row[3],                            // totalCostOfBuys
                        (BigDecimal) row[4],                            // totalProceedsFromSells
                        (BigDecimal) row[5]                             // totalRealizedGain
                ))
                .findFirst()
                .orElse(null); // Return null if no stats are found

        // Use the new helper method to populate and return the DTO
        return populateCollectionCardDtoWithStats(collectionCard, stats);
    }

    @Override
    @Transactional(readOnly = true)
    @VerifyCollectionViewAccess
    public Page<CollectionCardDTO> getCollectionCards(Long collectionId, @Nullable String cardName, Pageable pageable) {
        log.debug("Fetching cards for collection ID: {}", collectionId);

        // Get a page of sorted CollectionCardIDs and their calculated total stack values
        Page<Object[]> sortedIdAndTotalStackValuePage = collectionCardRepository.findCollectionCardIdsAndTotalStackValueSorted(collectionId, cardName, pageable);

        if (!sortedIdAndTotalStackValuePage.hasContent()) {
            return Page.empty(pageable);
        }

        // Extract the sorted list of IDs and their values into maps
        List<Long> sortedCollectionCardIds = sortedIdAndTotalStackValuePage.getContent().stream()
                .map(row -> (Long) row[0]) // First element is collection card ID
                .toList();

        Map<Long, BigDecimal> totalStackValueMap = sortedIdAndTotalStackValuePage.getContent().stream()
                .collect(Collectors.toMap(
                        row -> (Long) row[0],      // collectionCardId
                        row -> (BigDecimal) row[1] // calculatedTotalStackValue
                ));

        // Fetch the full collection card entities for these IDs
        Map<Long, CollectionCard> collectionCardMap = collectionCardRepository.findAllByIdsEagerly(sortedCollectionCardIds).stream()
                .collect(Collectors.toMap(CollectionCard::getId, cc -> cc));

        // Re-order them according to sortedCollectionCardIds to maintain the sort
        List<CollectionCard> sortedCollectionCards = sortedCollectionCardIds.stream()
                .map(collectionCardMap::get)
                .filter(java.util.Objects::nonNull) // Filter out any nulls if an ID wasn't found (shouldn't happen)
                .toList();

        // Should not happen if sortedIdAndTotalStackValuePage had content
        if (sortedCollectionCards.isEmpty()) {
            return Page.empty(pageable);
        }

        // Fetch other bulk stats and card details using sortedCollectionCardIDs
        List<Long> cardEntityIds = sortedCollectionCards.stream().map(cc -> cc.getCard().getId()).toList();

        // Fetch raw results and manually map them into CollectionCardStats, then collect to a map
        List<Object[]> results = collectionCardTransactionHistoryRepository.getBulkStatsForCollectionCards(sortedCollectionCardIds);
        Map<Long, CollectionCardStats> statsMap = results.stream()
                .map(row -> new CollectionCardStats(
                        (Long) row[0],                                  // collectionCardId
                        ((Number) row[1]).longValue(),                  // totalQuantity
                        (BigDecimal) row[2],                            // currentValue
                        (BigDecimal) row[3],                            // totalCostOfBuys
                        (BigDecimal) row[4],                            // totalProceedsFromSells
                        (BigDecimal) row[5]                             // totalRealizedGain
                ))
                .collect(Collectors.toMap(CollectionCardStats::collectionCardId, stats -> stats));

        Map<Long, Card> cardDetailsMap = cardRepository.findCardsWithDetailsByIds(cardEntityIds)
                .stream()
                .collect(Collectors.toMap(Card::getId, card -> card));

        List<CollectionCardDTO> collectionCardDTOs = sortedCollectionCards.stream()
                .map(collectionCard -> {
                    // Ensure the Card entity within CollectionCard is the fully loaded one
                    Card detailedCard = cardDetailsMap.get(collectionCard.getCard().getId());
                    if (detailedCard != null) {
                        collectionCard.setCard(detailedCard);
                    }

                    CollectionCardDTO dto = collectionMapper.toCollectionCardDto(collectionCard);
                    CollectionCardStats stats = statsMap.get(collectionCard.getId());
                    // Get the pre-calculated total stack value for this card (this is what we sorted by)
                    BigDecimal preCalculatedTotalStackValue = totalStackValueMap.get(collectionCard.getId());

                    if (stats != null) {
                        // Calculate financial values based on the building blocks from CollectionCardStats
                        BigDecimal costOfGoodsSold = stats.totalProceedsFromSells().subtract(stats.totalRealizedGain());
                        BigDecimal costBasisOfRemaining = stats.totalCostOfBuys().subtract(costOfGoodsSold);
                        BigDecimal unrealizedGain = (preCalculatedTotalStackValue != null ? preCalculatedTotalStackValue : BigDecimal.ZERO).subtract(costBasisOfRemaining);

                        dto.setQuantity((int) stats.totalQuantity());
                        // The 'currentValue' field in CollectionCardDTO should represent the total stack value.
                        dto.setCurrentValue(preCalculatedTotalStackValue != null ? preCalculatedTotalStackValue : BigDecimal.ZERO);
                        dto.setTotalCostBasis(costBasisOfRemaining);
                        dto.setRealizedGain(stats.totalRealizedGain());
                        dto.setUnrealizedGain(unrealizedGain);
                    } else {
                        // Default values if no stats (e.g., card exists but has no transactions yet)
                        dto.setQuantity(0);
                        dto.setCurrentValue(preCalculatedTotalStackValue != null ? preCalculatedTotalStackValue : BigDecimal.ZERO);
                        dto.setTotalCostBasis(BigDecimal.ZERO);
                        dto.setUnrealizedGain(BigDecimal.ZERO);
                        dto.setRealizedGain(BigDecimal.ZERO);
                    }
                    return dto;
                }).toList();

        // Return a new Page object with the sorted DTOs and original pagination info from the ID-sorted query.
        return new PageImpl<>(collectionCardDTOs, pageable, sortedIdAndTotalStackValuePage.getTotalElements());
    }

    @Override
    @Transactional(readOnly = true)
    @VerifyCollectionViewAccess
    public Page<PortfolioValueHistoryDTO> getPortfolioValueHistory(
            Long collectionId, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        log.debug("Fetching value history for portfolio (collection) ID: {}", collectionId);
        Page<PortfolioValueHistory> valueHistoryPage = portfolioValueHistoryRepository
                .findByPortfolioIdAndTimestampBetween(collectionId, startDate, endDate, pageable);
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
    public CollectionCardTransactionHistoryDTO addTransaction(Long collectionId, Long collectionCardId, CreateTransactionDTO createTransactionDTO) {
        Collection collection = collectionRepository.findById(collectionId)
                .orElseThrow(() -> new ResourceNotFoundException("Collection not found with id: " + collectionId));

        CollectionCard collectionCard = collectionCardRepository.findById(collectionCardId)
                .orElseThrow(() -> new ResourceNotFoundException("Collection card not found with id: " + collectionCardId));

        // Ensure the card belongs to the specified collection
        if (!collectionCard.getCollection().getId().equals(collectionId)) {
            throw new IllegalArgumentException("CollectionCard with id " + collectionCardId +
                    " does not belong to collection with id " + collectionId);
        }

        // Create a new transaction history record
        CollectionCardTransactionHistory.CollectionCardTransactionHistoryBuilder transactionHistoryBuilder = CollectionCardTransactionHistory.builder()
                .collectionCard(collectionCard)
                .condition(createTransactionDTO.getCondition())
                .finish(createTransactionDTO.getFinish())
                .transactionType(createTransactionDTO.getTransactionType())
                .quantity(createTransactionDTO.getQuantity())
                .purchaseDate(createTransactionDTO.getPurchaseDate() != null ? createTransactionDTO.getPurchaseDate() : LocalDate.now())
                .costBasis(createTransactionDTO.getCostBasis() != null ? createTransactionDTO.getCostBasis() : BigDecimal.ZERO);

        if (createTransactionDTO.getTransactionType() == TransactionType.SELL) {
            // For a SELL, 'costBasis' from the DTO represents the sale price per item.
            BigDecimal salePricePerItem = createTransactionDTO.getCostBasis();

            // Calculate the average cost of the items being sold.
            AverageCostInfo avgCostInfo = collectionCardTransactionHistoryRepository
                    .getAverageCostForBuys(collectionCardId);

            if (avgCostInfo == null || avgCostInfo.totalBuyQuantity() <= 0) {
                // Cannot sell something with no purchase history, treat as zero-cost basis.
                avgCostInfo = new AverageCostInfo(BigDecimal.ZERO, 0L);
            }

            BigDecimal averageCostPerItem = avgCostInfo.getAverageCost();

            // Realized Gain for this specific transaction = (Sale Price - Average Cost) * Quantity Sold
            BigDecimal realizedGainForThisTransaction = (salePricePerItem.subtract(averageCostPerItem))
                    .multiply(new BigDecimal(createTransactionDTO.getQuantity()));

            // Set the calculated gain on the transaction to be saved.
            transactionHistoryBuilder.realizedGain(realizedGainForThisTransaction);
            log.info("Calculated realized gain of {} for sale of {} items of card {}.",
                    realizedGainForThisTransaction, createTransactionDTO.getQuantity(), collectionCardId);
        }

        CollectionCardTransactionHistory savedTransaction = collectionCardTransactionHistoryRepository.save(transactionHistoryBuilder.build());
        log.info("Added transaction for CollectionCard ID: {} in Collection ID: {}", collectionCardId, collectionId);

        return collectionMapper.toCollectionCardTransactionHistoryDto(savedTransaction);
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

        Map<Long, PortfolioStatBuildingBlocks> statsMap = collectionRepository.getBulkStatBuildingBlocks(collectionIds).stream()
                .collect(Collectors.toMap(PortfolioStatBuildingBlocks::collectionId, stats -> stats));

        // 3. Map to DTOs, combining entities with their fetched stats
        return collectionsPage.map(collection -> {
            // Use the helper method you created earlier, but provide it with stats
            return populateCollectionDtoWithStats(collection, statsMap.getOrDefault(collection.getId(),
                    new PortfolioStatBuildingBlocks(collection.getId(), 0L, BigDecimal.ZERO,
                            BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO)));
        });

    }

    @Override
    @Scheduled(cron = "0 0 0 * * *") // Runs every day at midnight
    @Transactional
    public void updateAllCollections() {
        int pageNumber = 0;
        int pageSize = 100; // Adjust batch size
        Page<Collection> collectionPage;
        int historyCreatedCount = 0;
        int errorCount = 0;

        log.info("Starting scheduled job to create daily portfolio value history snapshots.");

        do {
            // Fetch one page of collections
            collectionPage = collectionRepository.findAllCollectionsPaged(PageRequest.of(pageNumber, pageSize));
            log.info("Processing page {} with {} collections.", pageNumber, collectionPage.getNumberOfElements());

            if (!collectionPage.hasContent()) {
                break; // Exit if the page is empty
            }

            // Get all collection IDs from the current page
            List<Long> collectionIdsOnPage = collectionPage.stream()
                    .map(Collection::getId)
                    .toList();

            // Fetch all stats for the entire page in a SINGLE batch query to avoid N+1 issues
            Map<Long, PortfolioStatBuildingBlocks> statsMap = collectionRepository.getBulkStatBuildingBlocks(collectionIdsOnPage)
                    .stream()
                    .collect(Collectors.toMap(PortfolioStatBuildingBlocks::collectionId, stats -> stats));

            // Process each collection using the pre-fetched stats from the map
            for (Collection collection : collectionPage.getContent()) {
                // We only create value history for Portfolios
                if (!(collection instanceof Portfolio portfolio)) {
                    continue;
                }

                try {
                    // Look up the stats for the current portfolio from our map
                    PortfolioStatBuildingBlocks currentStats = statsMap.get(collection.getId());

                    // If a portfolio has stats, create a history entry
                    if (currentStats != null) {
                        PortfolioValueHistory historyEntry = new PortfolioValueHistory();
                        historyEntry.setPortfolio(portfolio);
                        // Use the 'currentValue' from the pre-fetched stats object
                        historyEntry.setValue(currentStats.currentValue());
                        // Use the current time to mark when the snapshot was taken
                        historyEntry.setTimestamp(LocalDateTime.now());

                        portfolioValueHistoryRepository.save(historyEntry);
                        historyCreatedCount++;
                    }
                } catch (Exception e) {
                    errorCount++;
                    log.error("Failed to create value history for portfolio {} during scheduled job: {}", collection.getId(), e.getMessage(), e);
                }
            }
            pageNumber++;
        } while (collectionPage.hasNext());

        log.info("Finished scheduled portfolio value history update. New history entries created: {}, Errors: {}", historyCreatedCount, errorCount);
    }

    /* Helper Methods */
    private UserEntity findUserByAuth0IdOrThrow(String auth0Id) {
        return userRepository.findByAuth0Id(auth0Id).orElseThrow(() ->
                new ResourceNotFoundException("User not found with auth0Id: " + auth0Id));
    }

    private CollectionDTO populateCollectionDto(Collection collection) {
        Long collectionId = collection.getId();

        Map<Long, PortfolioStatBuildingBlocks> statsMap = collectionRepository
                .getBulkStatBuildingBlocks(List.of(collectionId)).stream()
                .collect(Collectors.toMap(PortfolioStatBuildingBlocks::collectionId, stats -> stats));

        PortfolioStatBuildingBlocks stats = statsMap.getOrDefault(collectionId,
                new PortfolioStatBuildingBlocks(collectionId, 0L,
                        BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO));

        return populateCollectionDtoWithStats(collection, stats);
    }

    private CollectionDTO populateCollectionDtoWithStats(Collection collection, PortfolioStatBuildingBlocks stats) {
        // For CollectionLists, the logic is simple.
        if (collection instanceof CollectionList) {
            CollectionListDTO dto = collectionMapper.toCollectionListDto((CollectionList) collection);
            dto.setNumProducts((int) stats.numProducts());
            dto.setCurrentValue(stats.currentValue());
            return dto;
        }

        // --- Logic for Portfolios ---
        PortfolioDTO dto = collectionMapper.toPortfolioDto((Portfolio) collection);

        // Get the raw building blocks from the stats object.
        BigDecimal currentValue = stats.currentValue();
        BigDecimal totalCostOfBuys = stats.totalCostOfBuys();
        BigDecimal totalProceedsFromSells = stats.totalProceedsFromSells();
        BigDecimal totalRealizedGain = stats.totalRealizedGain();

        // --- Perform Final Calculations in Java ---

        // 1. Cost Basis of Remaining Items = (Total Cost of Buys) - (Cost of Goods Sold)
        //    where Cost of Goods Sold = (Total Sale Proceeds) - (Total Realized Gain)
        BigDecimal costOfGoodsSold = totalProceedsFromSells.subtract(totalRealizedGain);
        BigDecimal totalCostBasis = totalCostOfBuys.subtract(costOfGoodsSold);

        // 2. Unrealized Gain = (Current Market Value) - (Cost Basis of Remaining Items)
        BigDecimal unrealizedGain = currentValue.subtract(totalCostBasis);

        // 3. Total Return = (Unrealized Gain) + (Realized Gain)
        BigDecimal totalReturn = unrealizedGain.add(totalRealizedGain);

        // 4. Lifetime ROI = (Total Return / Total Cost Of All Buys) * 100
        BigDecimal lifetimeROI = BigDecimal.ZERO;
        // Avoid division by zero if no purchases were ever made.
        if (totalCostOfBuys.compareTo(BigDecimal.ZERO) > 0) {
            lifetimeROI = totalReturn.divide(totalCostOfBuys, 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100"));
        }

        // --- Populate the DTO ---
        dto.setNumProducts((int) stats.numProducts());
        dto.setCurrentValue(currentValue);
        dto.setTotalCostBasis(totalCostBasis);
        dto.setUnrealizedGain(unrealizedGain);
        dto.setRealizedGain(totalRealizedGain);
        dto.setTotalReturn(totalReturn);
        dto.setLifetimeROI(lifetimeROI);

        return dto;
    }

    private CollectionCardDTO populateCollectionCardDtoWithStats(CollectionCard collectionCard, @Nullable CollectionCardStats stats) {
        // First, create the basic DTO from the entity
        CollectionCardDTO dto = collectionMapper.toCollectionCardDto(collectionCard);

        if (stats != null) {
            // Calculate final values from the stat building blocks
            BigDecimal costOfGoodsSold = stats.totalProceedsFromSells().subtract(stats.totalRealizedGain());
            BigDecimal costBasisOfRemaining = stats.totalCostOfBuys().subtract(costOfGoodsSold);
            BigDecimal unrealizedGain = stats.currentValue().subtract(costBasisOfRemaining);

            // Set all calculated values on the DTO
            dto.setQuantity(stats.totalQuantity());
            dto.setCurrentValue(stats.currentValue());
            dto.setTotalCostBasis(costBasisOfRemaining);
            dto.setRealizedGain(stats.totalRealizedGain());
            dto.setUnrealizedGain(unrealizedGain);
        } else {
            // Default values for a card with no transaction history
            dto.setQuantity(0);
            dto.setCurrentValue(BigDecimal.ZERO);
            dto.setTotalCostBasis(BigDecimal.ZERO);
            dto.setUnrealizedGain(BigDecimal.ZERO);
            dto.setRealizedGain(BigDecimal.ZERO);
        }
        return dto;
    }
}
