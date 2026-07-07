package com.CharlesRiverDevlopement.events;

import com.CharlesRiverDevlopement.DTOs.EventType;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@Builder
public class NotificationEvent<T> {

    private UUID eventId;

    private EventType type;

    private T payload;

}