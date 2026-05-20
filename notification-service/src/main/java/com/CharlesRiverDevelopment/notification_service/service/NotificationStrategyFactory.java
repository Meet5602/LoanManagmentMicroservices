package com.CharlesRiverDevelopment.notification_service.service;

import com.CharlesRiverDevelopment.notification_service.NotificationStrategies.NotificationStrategy;
import com.CharlesRiverDevelopment.notification_service.models.NotificationChannel;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class NotificationStrategyFactory {
    private Map<NotificationChannel, NotificationStrategy> strategies;

    public NotificationStrategyFactory(List<NotificationStrategy> strategies) {
        this.strategies = strategies.stream().
                collect(Collectors.toMap(NotificationStrategy::notificationChannel, Function.identity()));
    }

    public NotificationStrategy getStrategy(NotificationChannel channel) {
        NotificationStrategy strategy = strategies.get(channel);
        if (strategy == null) {
            throw new IllegalArgumentException("No strategy found for channel: " + channel);
        }
        return strategy;
    }
}
