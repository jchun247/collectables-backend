package io.github.jchun247.collectables.model.collection;

import io.github.jchun247.collectables.model.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "collections")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Collection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private UserEntity user;

    private String name;
}
