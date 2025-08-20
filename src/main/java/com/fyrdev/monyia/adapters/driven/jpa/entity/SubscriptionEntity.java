package com.fyrdev.monyia.adapters.driven.jpa.entity;

import com.fyrdev.monyia.domain.model.enums.Periodicity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "subscriptions")
public class SubscriptionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private String name;
    private String urlImage;
    @Column(nullable = false)
    private Double amount;
    @Column(nullable = false)
    private LocalDate dueDate;
    @Enumerated
    @Column(nullable = false)
    private Periodicity frequency;

    @ManyToOne
    @JoinColumn(name = "user_entity_id")
    private UserEntity userEntity;

}