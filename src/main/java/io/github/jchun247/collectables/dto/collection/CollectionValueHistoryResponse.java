package io.github.jchun247.collectables.dto.collection;

import io.github.jchun247.collectables.model.collection.CollectionValueHistory;
import lombok.Data;

import java.util.List;

@Data
public class CollectionValueHistoryResponse {
    private List<CollectionValueHistory> history;
    private int totalRecords;

    public CollectionValueHistoryResponse(List<CollectionValueHistory> history) {
        this.history = history;
        this.totalRecords = history.size();
    }
}
