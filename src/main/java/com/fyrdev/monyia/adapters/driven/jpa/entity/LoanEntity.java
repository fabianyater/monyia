package com.fyrdev.monyia.adapters.driven.jpa.entity;

import com.fyrdev.monyia.domain.model.enums.LoanType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "loans")
public class LoanEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private double balance;

    @Column(nullable = false)
    private String loanParty;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "loan_type", nullable = false)
    private LoanType loanType;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @ManyToOne
    @JoinColumn(name = "pocket_entity_id")
    private PocketEntity pocketEntity;

}