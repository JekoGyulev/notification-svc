package app.web;


import app.model.Notification;
import app.service.NotificationService;
import app.web.dto.CreateNotificationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {


    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping
    public ResponseEntity<?> sendNotification(@RequestBody CreateNotificationRequest request) {
        Notification notification = this.notificationService.send(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }
}
