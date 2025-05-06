package io.github.jchun247.collectables.model.collection;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("LIST")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SuperBuilder
public class CollectionList extends Collection {
    @Enumerated(EnumType.STRING)
    private CollectionListType listType;

    @Override
    public CollectionType getCollectionType() {
        return CollectionType.LIST;
    }
}