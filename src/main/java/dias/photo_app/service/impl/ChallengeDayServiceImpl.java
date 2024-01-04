package dias.photo_app.service.impl;

import dias.photo_app.exceptions.ChallengeServiceExceptions;
import dias.photo_app.io.ChallengeDayRepository;
import dias.photo_app.io.ChallengeRepository;
import dias.photo_app.io.UserRepository;
import dias.photo_app.io.entity.ChallengeDayEntity;
import dias.photo_app.io.entity.ChallengeEntity;
import dias.photo_app.io.entity.UserEntity;
import dias.photo_app.service.ChallengeDayService;
import dias.photo_app.shared.DayStatus;
import dias.photo_app.shared.dto.ChallengeDayDto;
import dias.photo_app.shared.dto.ChallengeDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChallengeDayServiceImpl implements ChallengeDayService {
    @Autowired
    ChallengeDayRepository challengeDayRepository;

    @Autowired
    ChallengeRepository challengeRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public List<ChallengeDayDto> getAllChallengeDays(String userId, String challengeId) {

        UserEntity userEntity = userRepository.findByUserId(userId);

        if (userEntity == null) {
            throw new UsernameNotFoundException(userId);
        }

        Optional<ChallengeEntity> optionalChallenge = userEntity.getChallenges()
                .stream()
                .filter(challenge -> challenge.getChallengeId().equals(challengeId))
                .findFirst();

        if (!optionalChallenge.isPresent()) {
            throw new ChallengeServiceExceptions("Challenge with ID " + challengeId + " not found for the user.");
        }

        ChallengeEntity challengeEntity = optionalChallenge.get();

        List<ChallengeDayDto> challengeDays = new ArrayList<>();
        for (ChallengeDayEntity challengeDay : challengeDayRepository.findByChallengeId(challengeEntity.getId())) {
            ChallengeDayDto map = modelMapper.map(challengeDay, ChallengeDayDto.class);
            challengeDays.add(map);
        }

        return challengeDays;
    }

    @Override
    public ChallengeDayDto updateChallengeDayStatus(String challengeDayId, DayStatus newStatus) {
        Optional<ChallengeDayEntity> optionalChallengeDay = challengeDayRepository.findByChallengeDayId(challengeDayId);

        if (optionalChallengeDay.isPresent()) {
            ChallengeDayEntity challengeDayEntity = optionalChallengeDay.get();

            // Update the status of the challenge day
            challengeDayEntity.setDayStatus(newStatus);
            challengeDayRepository.save(challengeDayEntity);

            // Return the updated ChallengeDayDto
            return modelMapper.map(challengeDayEntity, ChallengeDayDto.class);
        } else {
            throw new ChallengeServiceExceptions("Challenge day with ID " + challengeDayId + " not found.");
        }
    }

    @Override
    public ChallengeDayDto getChallengeDayById(String challengeDayId) {
        Optional<ChallengeDayEntity> optionalChallengeDay = challengeDayRepository.findByChallengeDayId(challengeDayId);

        if (optionalChallengeDay.isPresent()) {
            ChallengeDayEntity challengeDayEntity = optionalChallengeDay.get();
            return modelMapper.map(challengeDayEntity, ChallengeDayDto.class);
        } else {
            throw new ChallengeServiceExceptions("Challenge day with ID " + challengeDayId + " not found.");
        }
    }
}