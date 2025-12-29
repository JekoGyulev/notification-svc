package app.web;


import app.model.Notification;
import app.service.NotificationService;
import app.web.dto.CreateNotificationRequest;
import app.web.dto.NotificationResponse;
import app.web.mapper.DtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {


    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }


    @GetMapping
    public ResponseEntity<List<NotificationResponse>> getNotificationHistory(@RequestParam("user_id") UUID userId) {

        List<Notification> notifications = this.notificationService.getHistoryByUserId(userId);

        List<NotificationResponse> notificationResponses = notifications
                .stream()
                .map(DtoMapper::from)
                .toList();

        return ResponseEntity.status(HttpStatus.OK)
                .body(notificationResponses);
    }

    @PostMapping
    public ResponseEntity<NotificationResponse> sendNotification(@RequestBody CreateNotificationRequest request) {
        Notification notification = this.notificationService.send(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(DtoMapper.from(notification));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteNotification(@RequestParam("userId") UUID userId) {

        this.notificationService.deleteAll(userId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }





}
