package com.fyrdev.monyia.adapters.driving.http.mapper;

import com.fyrdev.monyia.adapters.driving.http.dto.request.SubscriptionRequest;
import com.fyrdev.monyia.adapters.driving.http.dto.response.BudgetResponse;
import com.fyrdev.monyia.adapters.driving.http.dto.response.SubscriptionResponse;
import com.fyrdev.monyia.domain.model.Budget;
import com.fyrdev.monyia.domain.model.Subscription;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ISubscriptionMapper {
    @Mapping(target = "frequency", source = "periodicity")
    Subscription toSubscription(SubscriptionRequest subscriptionRequest);

    SubscriptionResponse toSubscriptionResponse(Subscription subscription);
    List<SubscriptionResponse> toSubscriptionResponseList(List<Subscription> subscriptions);
}
