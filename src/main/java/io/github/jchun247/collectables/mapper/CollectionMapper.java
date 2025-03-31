package io.github.jchun247.collectables.mapper;

import io.github.jchun247.collectables.dto.collection.CollectionCardDTO;
import io.github.jchun247.collectables.dto.collection.CollectionDTO;
import io.github.jchun247.collectables.dto.collection.CollectionValueHistoryDTO;
import io.github.jchun247.collectables.model.collection.Collection;
import io.github.jchun247.collectables.model.collection.CollectionCard;
import io.github.jchun247.collectables.model.collection.CollectionValueHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CollectionMapper {
    @Mapping(source = "valueHistory", target = "valueHistory", qualifiedByName = "mapValueHistory")
    CollectionDTO toCollectionDto(Collection collection);
    CollectionCardDTO toCollectionCardDto(CollectionCard collectionCard);

    @Named("mapValueHistory")
    default Set<CollectionValueHistoryDTO> mapValueHistory(Set<CollectionValueHistory> valueHistory) {
        if (valueHistory == null) {
            return Collections.emptySet();
        }
        return valueHistory.stream()
                .map(this::toCollectionValueHistoryDto)
                .collect(Collectors.toSet());
    }

    CollectionValueHistoryDTO toCollectionValueHistoryDto(CollectionValueHistory collectionValueHistoryDTO);

}
