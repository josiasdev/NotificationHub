package com.cogitolab.notificationhub.domain.port;

import com.cogitolab.notificationhub.domain.model.Notification;

public interface NotificationSenderPort {
    void send(Notification notification);
}
