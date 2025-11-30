package app.service;

import app.model.NotificationPreference;
import app.model.NotificationType;
import app.repository.NotificationPreferenceRepository;
import app.web.dto.PreferenceRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class NotificationPreferenceService {

    private final NotificationPreferenceRepository preferenceRepository;

    public NotificationPreferenceService(NotificationPreferenceRepository preferenceRepository) {
        this.preferenceRepository = preferenceRepository;
    }

    public NotificationPreference upsert(PreferenceRequest preferenceRequest) {

        Optional<NotificationPreference> preferenceOptional =
                this.preferenceRepository.findByUserId(preferenceRequest.getUserId());


        if (preferenceOptional.isPresent()) {
            NotificationPreference notificationPreference = preferenceOptional.get();

            notificationPreference.setEnabled(preferenceRequest.isNotificationEnabled());
            notificationPreference.setContactInfo(preferenceRequest.getContactInfo());
            notificationPreference.setUpdatedOn(LocalDateTime.now());

            return this.preferenceRepository.save(notificationPreference);
        }

        NotificationPreference notificationPreference = NotificationPreference.builder()
                    .userId(preferenceRequest.getUserId())
                    .contactInfo(preferenceRequest.getContactInfo())
                    .createdOn(LocalDateTime.now())
                    .updatedOn(LocalDateTime.now())
                    .enabled(preferenceRequest.isNotificationEnabled())
                    .type(NotificationType.EMAIL)
                    .build();


        return this.preferenceRepository.save(notificationPreference);
    }

    public NotificationPreference getByUserId(UUID userId) {
        return this.preferenceRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Preference for this user was not found"));
    }
}
