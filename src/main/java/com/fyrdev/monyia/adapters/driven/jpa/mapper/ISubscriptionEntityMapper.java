package com.fyrdev.monyia.adapters.driven.jpa.mapper;

import com.fyrdev.monyia.adapters.driven.jpa.entity.SubscriptionEntity;
import com.fyrdev.monyia.domain.model.Subscription;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ISubscriptionEntityMapper {
    @Mapping(target = "userEntity.id", source = "userId")
    SubscriptionEntity toSubscriptionEntity(Subscription subscription);

    @Mapping(target = "userId", source = "userEntity.id")
    Subscription toSubscription(SubscriptionEntity subscriptionEntity);
}
