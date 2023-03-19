package com.fxdata.notificationservice.service;

import com.fxdata.notificationservice.dto.NotificationData;

public interface NotificationService {

    void sendNotification(NotificationData notificationData);
}

