package com.fyrdev.monyia.adapters.driven.jpa.entity;

import com.fyrdev.monyia.domain.model.Category;
import com.fyrdev.monyia.domain.model.enums.Periodicity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "budgets")
public class BudgetEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private BigDecimal balance;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

    @Enumerated
    @Column(nullable = false)
    private Periodicity periodicity;

    @Column(nullable = false)
    private Integer dayOfTheMonth;

    @Column(nullable = false)
    private String emoji;

    @ManyToOne
    @JoinColumn(name = "category_entity_id")
    private CategoryEntity categoryEntity;

    @ManyToOne
    @JoinColumn(name = "user_entity_id")
    private UserEntity userEntity;

}