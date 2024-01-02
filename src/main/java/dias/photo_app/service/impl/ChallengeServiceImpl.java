package dias.photo_app.service.impl;

import dias.photo_app.exceptions.ChallengeServiceExceptions;
import dias.photo_app.io.ChallengeRepository;
import dias.photo_app.io.UserRepository;
import dias.photo_app.io.entity.ChallengeEntity;
import dias.photo_app.io.entity.UserEntity;
import dias.photo_app.service.ChallengeService;
import dias.photo_app.shared.Utils;
import dias.photo_app.shared.dto.ChallengeDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ChallengeServiceImpl implements ChallengeService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ChallengeRepository challengeRepository;
    @Autowired
    Utils utils;
    @Autowired
    ModelMapper modelMapper;
    @Override
    public ChallengeDto createChallenge(String userId, ChallengeDto challengeDto) {
        UserEntity userEntity = userRepository.findByUserId(userId);
        if(userEntity == null) {
            throw new UsernameNotFoundException(userId);
        }

        ChallengeEntity challengeEntity = modelMapper.map(challengeDto, ChallengeEntity.class);
        challengeEntity.setUserDetails(userEntity);
        challengeEntity.setChallengeId(utils.generateChallengeId(30));
        ChallengeEntity savedChallenge = challengeRepository.save(challengeEntity);

        return modelMapper.map(savedChallenge, ChallengeDto.class);
    }

    @Override
    public List<ChallengeDto> getChallenges(String userId) {
        List<ChallengeDto> returnValue = new ArrayList<>();

        UserEntity userEntity = userRepository.findByUserId(userId);

        if(userEntity == null) {
            throw new UsernameNotFoundException(userId);
        }

        List<ChallengeEntity> challenges = challengeRepository.findAllByUserDetails(userEntity);
        for(ChallengeEntity challenge : challenges) {
            returnValue.add(modelMapper.map(challenge, ChallengeDto.class));
        }

        return returnValue;
    }

    @Override
    public ChallengeDto getChallengeById(String userId, String challengeId) {
        UserEntity userEntity = userRepository.findByUserId(userId);

        if (userEntity == null) {
            throw new UsernameNotFoundException(userId);
        }

        Optional<ChallengeEntity> optionalChallenge = userEntity.getChallenges()
                .stream()
                .filter(challenge -> challenge.getChallengeId().equals(challengeId))
                .findFirst();

        return optionalChallenge.map(challenge -> modelMapper.map(challenge, ChallengeDto.class)).orElse(null);
    }

    @Override
    public ChallengeDto updateChallenge(String userId, String challengeId, ChallengeDto challengeDto) {
        UserEntity userEntity = userRepository.findByUserId(userId);

        if(userEntity == null) {
            throw new UsernameNotFoundException(userId);
        }

        Optional<ChallengeEntity> optionalChallenge = userEntity.getChallenges()
                .stream()
                .filter(challenge -> challenge.getChallengeId().equals(challengeId))
                .findFirst();

        if (!optionalChallenge.isPresent()) {
            throw new ChallengeServiceExceptions("Challenge with ID " + challengeId + " not found.");
        }

        ChallengeEntity existingChallenge = optionalChallenge.get();

        // Update the properties of existingChallenge
        existingChallenge.setTitle(challengeDto.getTitle());
        existingChallenge.setDescription(challengeDto.getDescription());
        existingChallenge.setStartDate(challengeDto.getStartDate());
        existingChallenge.setEndDate(challengeDto.getEndDate());
        existingChallenge.setDays(challengeDto.getDays());
        ChallengeEntity updatedChallenge = challengeRepository.save(existingChallenge);

        return modelMapper.map(updatedChallenge, ChallengeDto.class);
    }

    @Override
    public void deleteChallenge(String userId, String challengeId) {
        UserEntity userEntity = userRepository.findByUserId(userId);

        if (userEntity == null) {
            throw new UsernameNotFoundException(userId);
        }

        ChallengeEntity challengeToDelete = userEntity.getChallenges()
                .stream()
                .filter(challenge -> challenge.getChallengeId().equals(challengeId))
                .findFirst()
                .orElseThrow(() -> new ChallengeServiceExceptions("Challenge with ID " + challengeId + " not found."));

        userEntity.getChallenges().remove(challengeToDelete);
        userRepository.save(userEntity);
        challengeRepository.delete(challengeToDelete);
    }
}
