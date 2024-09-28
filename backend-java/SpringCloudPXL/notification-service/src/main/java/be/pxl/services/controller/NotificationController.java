package be.pxl.services.controller;

import be.pxl.services.domain.dto.NotificationRequest;
import be.pxl.services.domain.dto.NotificationResponse;
import be.pxl.services.services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<NotificationResponse>> getNotifications() {
        return ResponseEntity.ok(notificationService.getAllNotifications());
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificationResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(notificationService.findNotificationById(id));
    }


    @PostMapping
    public ResponseEntity<Long> addNotification(@RequestBody NotificationRequest notificationRequest) {
        Long id = notificationService.addNotification(notificationRequest);
        return ResponseEntity.created(URI.create("/api/notification/" + id)).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateNotifiationById(@PathVariable Long id, @RequestBody NotificationRequest notificationRequest) {
        notificationService.updateNotificationById(id, notificationRequest);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNotificationById(@PathVariable Long id)  {
        notificationService.deleteNotificationById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
