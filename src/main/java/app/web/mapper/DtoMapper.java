package app.web.mapper;

import app.model.NotificationPreference;
import app.web.dto.PreferenceResponse;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DtoMapper {

    public static PreferenceResponse from(NotificationPreference notificationPreference) {
        return PreferenceResponse.builder()
                .contactInfo(notificationPreference.getContactInfo())
                .isNotificationEnabled(notificationPreference.isEnabled())
                .notificationType(notificationPreference.getType())
                .build();
    }
}
