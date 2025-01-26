package io.github.jchun247.collectables.dto;

import io.github.jchun247.collectables.model.card.Card;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class PagedResponse<T> {

    private List<T> items;
    private int currentPage;
    private long totalItems;
    private int totalPages;

    public PagedResponse(List<T> items, Page<Card> page) {
        this.items = items;
        this.currentPage = page.getNumber();
        this.totalItems = page.getTotalElements();
        this.totalPages = page.getTotalPages();
    }
}
