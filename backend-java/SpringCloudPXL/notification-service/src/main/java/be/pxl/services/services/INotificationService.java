package be.pxl.services.services;

import be.pxl.services.domain.Notification;
import be.pxl.services.domain.dto.NotificationRequest;
import be.pxl.services.domain.dto.NotificationResponse;

import java.util.List;

public interface INotificationService {

    NotificationResponse findNotificationById(Long id);
    List<NotificationResponse> getAllNotifications();
    Long addNotification(NotificationRequest notificationRequest);

    void deleteNotificationById(Long id);

    void updateNotificationById(Long id, NotificationRequest notificationRequest);

    void sendMessage(NotificationRequest notificationRequest);
}
