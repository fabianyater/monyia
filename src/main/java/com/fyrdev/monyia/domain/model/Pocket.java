package com.fyrdev.monyia.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Pocket {
    private Long id;
    private UUID uuid;
    private String name;
    private BigDecimal balance;
    private LocalDateTime date;
    private Long userId;

    public Pocket() {
    }

    public Pocket(Long id, UUID uuid, String name, BigDecimal balance, LocalDateTime date, Long userId) {
        this.id = id;
        this.uuid = uuid;
        this.name = name;
        this.balance = balance;
        this.date = date;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
