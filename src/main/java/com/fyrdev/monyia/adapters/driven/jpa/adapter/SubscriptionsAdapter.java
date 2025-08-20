package com.fyrdev.monyia.adapters.driven.jpa.adapter;

import com.fyrdev.monyia.adapters.driven.jpa.mapper.ISubscriptionEntityMapper;
import com.fyrdev.monyia.adapters.driven.jpa.repository.ISubscriptionRepository;
import com.fyrdev.monyia.domain.model.Subscription;
import com.fyrdev.monyia.domain.spi.ISubscriptionPersistencePort;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class SubscriptionsAdapter implements ISubscriptionPersistencePort {
    private final ISubscriptionRepository subscriptionRepository;
    private final ISubscriptionEntityMapper subscriptionEntityMapper;

    @Override
    public void addNewSubscription(Subscription subscription) {
        subscriptionRepository.save(subscriptionEntityMapper.toSubscriptionEntity(subscription));
    }

    @Override
    public List<Subscription> getSubscriptions(Long userId) {
        return subscriptionRepository.findByUserEntity_Id(userId)
                .stream()
                .map(subscriptionEntityMapper::toSubscription)
                .toList();
    }
}
