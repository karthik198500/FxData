package com.fxdata.fxeventsmonitor.notify;

import com.fxdata.fxeventsmonitor.dto.NotificationDTO;

public interface NotificationService {

    void sendNotification(NotificationDTO notificationDTO);
}
