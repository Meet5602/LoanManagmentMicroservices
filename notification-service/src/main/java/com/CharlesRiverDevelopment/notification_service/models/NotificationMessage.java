package com.CharlesRiverDevelopment.notification_service.models;

// Channel-agnostic message built from any incoming event
public record NotificationMessage(Long userId,
                                  String email,
                                  String subject,
                                  String body) {}
