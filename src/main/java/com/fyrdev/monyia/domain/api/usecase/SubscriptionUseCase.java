package com.fyrdev.monyia.domain.api.usecase;

import com.fyrdev.monyia.adapters.driving.http.dto.response.SubscriptionResponse;
import com.fyrdev.monyia.domain.api.ISubscriptionServicePort;
import com.fyrdev.monyia.domain.model.Subscription;
import com.fyrdev.monyia.domain.spi.IAuthenticationPort;
import com.fyrdev.monyia.domain.spi.ISubscriptionPersistencePort;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class SubscriptionUseCase implements ISubscriptionServicePort {
    private final ISubscriptionPersistencePort subscriptionPersistencePort;
    private final IAuthenticationPort authenticationPort;

    public SubscriptionUseCase(ISubscriptionPersistencePort subscriptionPersistencePort,
                               IAuthenticationPort authenticationPort) {
        this.subscriptionPersistencePort = subscriptionPersistencePort;
        this.authenticationPort = authenticationPort;
    }

    @Override
    public void addNewSubscription(Subscription subscription) {
        Long userId = authenticationPort.getAuthenticatedUserId();

        subscription.setUserId(userId);

        subscriptionPersistencePort.addNewSubscription(subscription);
    }

    @Override
    public List<SubscriptionResponse> getSubscriptions() {
        Long userId = authenticationPort.getAuthenticatedUserId();

        var subscriptions = subscriptionPersistencePort.getSubscriptions(userId);

        return subscriptions
                .stream()
                .map(subscription -> SubscriptionResponse.builder()
                        .id(subscription.getId())
                        .name(subscription.getName())
                        .amount(subscription.getAmount())
                        .periodicity(subscription.getFrequency())
                        .dueDate(subscription.getDueDate())
                        .daysLeft(String.valueOf(ChronoUnit.DAYS.between(LocalDate.now(), subscription.getDueDate())))
                        .build())
                .toList();
    }
}
