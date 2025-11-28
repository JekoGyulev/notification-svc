package app.service;

import app.model.Notification;
import app.model.NotificationPreference;
import app.model.NotificationStatus;
import app.model.NotificationType;
import app.repository.NotificationRepository;
import app.web.dto.CreateNotificationRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationPreferenceService notificationPreferenceService;
    private final MailSender mailSender;

    public NotificationService(NotificationRepository notificationRepository, NotificationPreferenceService notificationPreferenceService, MailSender mailSender) {
        this.notificationRepository = notificationRepository;
        this.notificationPreferenceService = notificationPreferenceService;
        this.mailSender = mailSender;
    }

    public Notification send(CreateNotificationRequest request) {

        NotificationPreference notificationPreference = this.notificationPreferenceService.getByUserId(request.getUserId());

        boolean isEnabled = notificationPreference.isEnabled();

        if (!isEnabled) {
            throw new IllegalStateException("Notification preference is disabled");
        }


        Notification notification = Notification.builder()
                .subject(request.getSubject())
                .userId(request.getUserId())
                .body(request.getBody())
                .createdOn(LocalDateTime.now())
                .type(NotificationType.EMAIL)
                .deleted(false)
                .build();


        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setSubject(request.getSubject());
        mailMessage.setText(request.getBody());
        mailMessage.setTo(notificationPreference.getContactInfo());


        try {
            this.mailSender.send(mailMessage);
            notification.setStatus(NotificationStatus.SUCCEEDED);
        } catch (Exception e ) {
            log.error("Failed email due to : %s".formatted(e.getMessage()));
            notification.setStatus(NotificationStatus.FAILED);
        }

        return this.notificationRepository.save(notification);
    }

    public List<Notification> getHistoryByUserId(UUID userId) {
        return this.notificationRepository.findAllByUserId(userId)
                .stream()
                .filter(notification -> !notification.isDeleted()).toList();
    }
}
