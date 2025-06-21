package io.github.jchun247.collectables.repository.card;

import io.github.jchun247.collectables.dto.card.CardSearchCriteria;
import io.github.jchun247.collectables.model.card.Card;
import io.github.jchun247.collectables.model.card.CardPrice;
import io.github.jchun247.collectables.model.card.CardRarity;
import io.github.jchun247.collectables.model.card.CardSet;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CardRepositoryImpl implements CardRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<Long> findCardIdsByCriteria(CardSearchCriteria criteria, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        // Query for fetching the IDs
        CriteriaQuery<Long> idQuery = cb.createQuery(Long.class);
        Root<Card> card = idQuery.from(Card.class);
        Join<Card, CardSet> set = card.join("set");
        Join<Card, CardPrice> prices = card.join("prices");

        // Build WHERE clause
        idQuery.where(buildPredicates(cb, card, set, prices, criteria));

        // Handle Sorting and Grouping
        applySortingAndGrouping(cb, idQuery, card, prices, pageable.getSort());

        idQuery.select(card.get("id"));

        // Execute ID query with pagination
        TypedQuery<Long> typedQuery = entityManager.createQuery(idQuery);
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());
        List<Long> ids = typedQuery.getResultList();

        // Execute Count query for total elements
        long total = executeCountQuery(cb, criteria);

        return new PageImpl<>(ids, pageable, total);
    }

    private Predicate[] buildPredicates(CriteriaBuilder cb, Root<Card> card, Join<Card, CardSet> set, Join<Card, CardPrice> prices, CardSearchCriteria criteria) {
        List<Predicate> predicates = new ArrayList<>();

        if (criteria.getGames() != null && !criteria.getGames().isEmpty()) {
            predicates.add(card.get("game").in(criteria.getGames()));
        }
        if (criteria.getSetId() != null) {
            predicates.add(cb.equal(set.get("id"), criteria.getSetId()));
        }
        if (criteria.getRarity() != null) {
            predicates.add(cb.equal(card.get("rarity"), criteria.getRarity()));
        }
        if (criteria.getCondition() != null) {
            predicates.add(cb.equal(prices.get("condition"), criteria.getCondition()));
        }
        if (criteria.getFinish() != null) {
            predicates.add(cb.equal(prices.get("finish"), criteria.getFinish()));
        }
        if (criteria.getMinPrice() != null && criteria.getMaxPrice() != null) {
            predicates.add(cb.between(prices.get("price"), criteria.getMinPrice(), criteria.getMaxPrice()));
        }
        if (criteria.getQuery() != null && !criteria.getQuery().isBlank()) {
            String likePattern = "%" + criteria.getQuery().toLowerCase() + "%";
            predicates.add(cb.or(
                    cb.like(cb.lower(card.get("name")), likePattern),
                    cb.like(cb.lower(set.get("id")), likePattern),
                    cb.like(cb.lower(card.get("setNumber")), likePattern)
            ));
        }

        return predicates.toArray(new Predicate[0]);
    }

    private void applySortingAndGrouping(CriteriaBuilder cb, CriteriaQuery<Long> query, Root<Card> card, Join<Card, CardPrice> prices, Sort sort) {
        Sort.Order sortOrder = sort.stream().findFirst().orElse(Sort.Order.by("name"));
        String property = sortOrder.getProperty();

        List<Order> orders = new ArrayList<>();

        if ("price".equals(property)) {
            query.groupBy(card.get("id"));
            if (sortOrder.isAscending()) {
                orders.add(cb.asc(cb.min(prices.get("price"))));
            } else {
                orders.add(cb.desc(cb.min(prices.get("price"))));
            }
        } else if ("rarity".equals(property)) {
            CriteriaBuilder.Case<Integer> caseExpression = cb.selectCase();

            for (CardRarity rarity : CardRarity.values()) {
                caseExpression = caseExpression.when(cb.equal(card.get("rarity"), rarity), rarity.getSortOrder());
            }

            // The final expression uses a large number for any fallback.
            Expression<Integer> rarityOrderExpression = caseExpression.otherwise(9999);

            // The sorting expression must be included in the GROUP BY clause
            query.groupBy(card.get("id"), rarityOrderExpression);

            if (sortOrder.isAscending()) {
                orders.add(cb.asc(rarityOrderExpression));
            } else {
                orders.add(cb.desc(rarityOrderExpression));
            }
        } else { // Default to name or any other direct property
            query.groupBy(card.get("id"), card.get(property));
            if (sortOrder.isAscending()) {
                orders.add(cb.asc(card.get(property)));
            } else {
                orders.add(cb.desc(card.get(property)));
            }
        }

        // Add a secondary, stable sort by ID
        orders.add(cb.asc(card.get("id")));
        query.orderBy(orders);
    }

    private long executeCountQuery(CriteriaBuilder cb, CardSearchCriteria criteria) {
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Card> card = countQuery.from(Card.class);
        Join<Card, CardSet> set = card.join("set");
        Join<Card, CardPrice> prices = card.join("prices");

        countQuery.select(cb.countDistinct(card.get("id")));
        countQuery.where(buildPredicates(cb, card, set, prices, criteria));

        return entityManager.createQuery(countQuery).getSingleResult();
    }
}
