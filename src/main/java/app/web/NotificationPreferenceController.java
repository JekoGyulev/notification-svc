package app.web;

import app.model.NotificationPreference;
import app.service.NotificationPreferenceService;
import app.web.dto.PreferenceRequest;
import app.web.dto.PreferenceResponse;
import app.web.mapper.DtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notification-preferences")
public class NotificationPreferenceController {

    private final NotificationPreferenceService notificationPreferenceService;

    @Autowired
    public NotificationPreferenceController(NotificationPreferenceService notificationPreferenceService) {
        this.notificationPreferenceService = notificationPreferenceService;
    }

    @PostMapping
    public ResponseEntity<PreferenceResponse> upsertPreference(@RequestBody PreferenceRequest preferenceRequest) {

        NotificationPreference notificationPreference = this.notificationPreferenceService.upsert(preferenceRequest);

        PreferenceResponse response = DtoMapper.from(notificationPreference);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
