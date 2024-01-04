package dias.photo_app.service;

import dias.photo_app.shared.DayStatus;
import dias.photo_app.shared.dto.ChallengeDayDto;

import java.util.List;

public interface ChallengeDayService {
    List<ChallengeDayDto> getAllChallengeDays(String userId, String challengeId);

    ChallengeDayDto updateChallengeDayStatus(String challengeDayId, DayStatus newStatus);

    ChallengeDayDto getChallengeDayById(String challengeDayId);
}
