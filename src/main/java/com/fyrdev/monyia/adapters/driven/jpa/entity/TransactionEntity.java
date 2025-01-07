package com.fyrdev.monyia.adapters.driven.jpa.entity;

import com.fyrdev.monyia.domain.model.enums.Periodicity;
import com.fyrdev.monyia.domain.model.enums.TransactionType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "transactions")
public class TransactionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "uuid", unique = true, nullable = false, updatable = false)
    private String uuid;

    @Column( nullable = false)
    private String description;

    @Column( nullable = false)
    private BigDecimal amount;

    @Column( nullable = false)
    private LocalDateTime date;

    @Enumerated(EnumType.STRING)
    @Column( nullable = false)
    private Periodicity periodicity;

    @Enumerated(EnumType.STRING)
    @Column( nullable = false)
    private TransactionType transactionType;

    @ManyToOne
    @JoinColumn(name = "category_entity_id")
    private CategoryEntity categoryEntity;

    @ManyToOne
    @JoinColumn(name = "pocket_entity_id")
    private PocketEntity pocketEntity;
}