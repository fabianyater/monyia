package com.fyrdev.monyia.domain.model;

import com.fyrdev.monyia.domain.model.enums.Periodicity;

import java.time.LocalDate;

public class Subscription {
    private Long id;
    private String name;
    private String urlImage;
    private Double amount;
    private LocalDate dueDate;
    private Periodicity frequency;
    private Long userId;

    public Subscription() {
    }

    public Subscription(Long id, String name, String urlImage, Double amount, LocalDate dueDate, Periodicity frequency, Long userId) {
        this.id = id;
        this.name = name;
        this.urlImage = urlImage;
        this.amount = amount;
        this.dueDate = dueDate;
        this.frequency = frequency;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public Periodicity getFrequency() {
        return frequency;
    }

    public void setFrequency(Periodicity frequency) {
        this.frequency = frequency;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
