package dias.photo_app.service;

import dias.photo_app.shared.dto.ChallengeDto;

public interface ChallengeService {
    ChallengeDto createChallenge(String userId, ChallengeDto challengeDto);
}
