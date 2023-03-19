package com.fxdata.fxratesmonitor.notify;


import com.fxdata.fxratesmonitor.dto.NotificationDTO;

public interface NotificationService {

    void sendNotification(NotificationDTO notificationDTO);

    void sendNotificationWithBody(String body);
}
