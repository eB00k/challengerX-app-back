package dias.photo_app.service;

import dias.photo_app.shared.dto.ChallengeDto;

import java.util.List;

public interface ChallengeService {
    ChallengeDto createChallenge(String userId, ChallengeDto challengeDto);

    List<ChallengeDto> getChallenges(String userId);

    ChallengeDto getChallengeById(String userId, String challengeId);

    ChallengeDto updateChallenge(String userId, String challengeId, ChallengeDto challengeDto);

    void deleteChallenge(String userId, String challengeId);
}
