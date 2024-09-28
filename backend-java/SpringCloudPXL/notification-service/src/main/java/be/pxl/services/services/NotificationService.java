package be.pxl.services.services;

import be.pxl.services.domain.Notification;
import be.pxl.services.domain.dto.NotificationRequest;
import be.pxl.services.domain.dto.NotificationResponse;
import be.pxl.services.exceptions.NotificationNotFoundException;
import be.pxl.services.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService implements INotificationService {

    private final NotificationRepository notificationRepository;

    private static final Logger LOGGER = LogManager.getLogger(NotificationService.class);

    public NotificationResponse findNotificationById(Long id) {
        Notification notification = notificationRepository.findById(id).orElseThrow(() -> new NotificationNotFoundException("Notification with id " + id + " can't be found"));
        return mapNotificationToDto(notification);
    }


    public List<NotificationResponse> getAllNotifications() {
        return notificationRepository.findAll().stream().map(this::mapNotificationToDto).toList();
    }

    public Long addNotification(NotificationRequest notificationRequest) {
        Notification notification = Notification.builder()
                .receiver(notificationRequest.getReceiver())
                .subject(notificationRequest.getSubject())
                .sender(notificationRequest.getSender())
                .message(notificationRequest.getMessage())
                .build();

        return notificationRepository.save(notification).getId();
    }

    @Override
    public void deleteNotificationById(Long id) {
        Notification notification = notificationRepository.findById(id).orElseThrow(() -> new NotificationNotFoundException("Notification with id " + id + " could not be found"));
        notificationRepository.delete(notification);
    }

    @Override
    public void updateNotificationById(Long id, NotificationRequest notificationRequest) {
        Notification notification = notificationRepository.findById(id).orElseThrow(() -> new NotificationNotFoundException("Notification with id " + id + " could not be found"));
        notification.setReceiver(notificationRequest.getReceiver());
        notification.setSender(notificationRequest.getSender());
        notification.setMessage(notificationRequest.getMessage());
        notification.setSubject(notificationRequest.getSubject());

        notificationRepository.save(notification);
    }

    private NotificationResponse mapNotificationToDto(Notification notification) {
        return NotificationResponse.builder()
                .receiver(notification.getReceiver())
                .sender(notification.getSender())
                .message(notification.getMessage())
                .subject(notification.getSubject())
                .build();
    }
}
