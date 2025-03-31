package io.github.jchun247.collectables.mapper;

import io.github.jchun247.collectables.dto.collection.CollectionCardDTO;
import io.github.jchun247.collectables.dto.collection.CollectionDTO;
import io.github.jchun247.collectables.model.collection.Collection;
import io.github.jchun247.collectables.model.collection.CollectionCard;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CollectionMapper {
    CollectionDTO toCollectionDto(Collection collection);
    CollectionCardDTO toCollectionCardDto(CollectionCard collectionCard);
}
