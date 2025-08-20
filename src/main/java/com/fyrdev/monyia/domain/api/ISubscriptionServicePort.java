package com.fyrdev.monyia.domain.api;

import com.fyrdev.monyia.adapters.driving.http.dto.response.SubscriptionResponse;
import com.fyrdev.monyia.domain.model.Subscription;

import java.util.List;

public interface ISubscriptionServicePort {
    void addNewSubscription(Subscription subscription);
    List<SubscriptionResponse> getSubscriptions();
}
