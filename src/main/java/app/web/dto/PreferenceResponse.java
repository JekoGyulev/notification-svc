package app.web.dto;

import app.model.NotificationType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PreferenceResponse {
    private NotificationType notificationType;
    private String contactInfo;
    private boolean isNotificationEnabled;
}
