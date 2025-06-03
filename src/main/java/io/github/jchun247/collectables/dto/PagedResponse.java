package io.github.jchun247.collectables.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class PagedResponse<T> {

    private List<T> items;
    private int currentPage;
    private long totalItems;
    private int totalPages;
    private int size;
    private int numberOfElements;
    private boolean first;
    private boolean last;
    private SortInfo sort;

    public PagedResponse(Page<T> page) {
        this.items = page.getContent();
        this.currentPage = page.getNumber();
        this.totalItems = page.getTotalElements();
        this.totalPages = page.getTotalPages();
        this.size = page.getSize();
        this.numberOfElements = page.getNumberOfElements();
        this.first = page.isFirst();
        this.last = page.isLast();
        this.sort = new SortInfo(page.getSort());
    }

    @Data
    @NoArgsConstructor
    public static class SortInfo {
        private boolean sorted;
        private boolean unsorted;
        private boolean empty;
        private List<OrderInfo> orders; // Represent Sort.Order nicely

        public SortInfo(Sort sort) {
            this.sorted = sort.isSorted();
            this.unsorted = sort.isUnsorted();
            this.empty = sort.isEmpty();
            // Map each Sort.Order to our simpler OrderInfo DTO
            this.orders = sort.stream()
                    .map(OrderInfo::new)
                    .collect(Collectors.toList());
        }
    }

    @Data
    @NoArgsConstructor
    public static class OrderInfo {
        private String property;
        private Sort.Direction direction;
        private Sort.NullHandling nullHandling;
        private boolean ignoreCase;

        public OrderInfo(Sort.Order order) {
            this.property = order.getProperty();
            this.direction = order.getDirection();
            this.nullHandling = order.getNullHandling();
            this.ignoreCase = order.isIgnoreCase();
        }
    }
}
