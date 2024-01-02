package dias.photo_app.ui.controller;

import dias.photo_app.exceptions.ChallengeServiceExceptions;
import dias.photo_app.service.ChallengeService;
import dias.photo_app.shared.dto.ChallengeDto;
import dias.photo_app.ui.model.request.ChallengeDetailsRequestModel;
import dias.photo_app.ui.model.response.ChallengeRest;
import dias.photo_app.ui.model.response.OperationStatusModel;
import dias.photo_app.ui.model.response.RequestOperationName;
import dias.photo_app.ui.model.response.RequestOperationStatus;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class ChallengeController {
    @Autowired
    ChallengeService challengeService;

    @Autowired
    ModelMapper modelMapper;

    // POST: http://localhost:4000/challenger-app/users/{userId}/challenges + payload
    @PostMapping(path = "/{userId}/challenges")
    public ChallengeRest createChallenge(@PathVariable String userId, @RequestBody ChallengeDetailsRequestModel challengeDetails) {
        // Validate challengeDetails...

        ChallengeDto challengeDto = modelMapper.map(challengeDetails, ChallengeDto.class);
        ChallengeDto createdChallenge = challengeService.createChallenge(userId, challengeDto);

        return modelMapper.map(createdChallenge, ChallengeRest.class);
    }

    // GET: http://localhost:4000/challenger-app/users/{userId}/challenges
    @GetMapping(path = "/{userId}/challenges")
    public List<ChallengeRest> getChallenges(@PathVariable String userId) {
        List<ChallengeRest> returnValue = new ArrayList<>();

        List<ChallengeDto> challenges = challengeService.getChallenges(userId);

        for(ChallengeDto challenge : challenges) {
            returnValue.add(modelMapper.map(challenge, ChallengeRest.class));
        }

        return returnValue;
    }

    // GET: http://localhost:4000/challenger-app/users/{userId}/challenges/{challengeId}
    @GetMapping(path = "/{userId}/challenges/{challengeId}")
    public ChallengeRest getChallengeById(@PathVariable String userId, @PathVariable String challengeId) {
        ChallengeDto challenge = challengeService.getChallengeById(userId, challengeId);
        if (challenge == null) {
            return null;
        }

        return modelMapper.map(challenge, ChallengeRest.class);
    }

    @PutMapping(path = "/{userId}/challenges/{challengeId}")
    public ChallengeRest updateChallenge(
            @PathVariable String userId,
            @PathVariable String challengeId,
            @RequestBody ChallengeDetailsRequestModel challengeDetails) {

        ChallengeDto challengeDto = modelMapper.map(challengeDetails, ChallengeDto.class);

        ChallengeDto updatedChallenge = challengeService.updateChallenge(userId, challengeId, challengeDto);

        return modelMapper.map(updatedChallenge, ChallengeRest.class);
    }

    @DeleteMapping(path = "/{userId}/challenges/{challengeId}")
    public OperationStatusModel deleteChallenge(@PathVariable String userId, @PathVariable String challengeId) {
        OperationStatusModel returnValue = new OperationStatusModel();

        try {
            challengeService.deleteChallenge(userId, challengeId);
            returnValue.setOperationName(RequestOperationName.DELETE.name());
            returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
        } catch (ChallengeServiceExceptions e) {
            returnValue.setOperationName(RequestOperationName.DELETE.name());
            returnValue.setOperationResult(RequestOperationStatus.ERROR.name());
        }

        return returnValue;
    }

}
