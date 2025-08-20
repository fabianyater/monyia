package com.fyrdev.monyia.adapters.driven.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "categories")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "emoji")
    private List<String> emojis;

    private String defaultEmoji;

    @ManyToOne
    @JoinColumn(name = "user_entity_id")
    private UserEntity userEntity;

    @ManyToMany
    @JoinTable(name = "categories_userEntities",
            joinColumns = @JoinColumn(name = "categoryEntity_id"),
            inverseJoinColumns = @JoinColumn(name = "userEntities_id"))
    private Set<UserEntity> userEntities = new LinkedHashSet<>();

}