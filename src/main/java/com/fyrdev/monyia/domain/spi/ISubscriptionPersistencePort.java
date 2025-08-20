package com.fyrdev.monyia.domain.spi;

import com.fyrdev.monyia.domain.model.Subscription;

import java.util.List;

public interface ISubscriptionPersistencePort {
    void addNewSubscription(Subscription subscription);
    List<Subscription> getSubscriptions(Long userId);
}
