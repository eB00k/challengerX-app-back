package dias.photo_app.service.impl;

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

@Service
public class ChallengeServiceImpl implements ChallengeService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ChallengeRepository challengeRepository;
    @Autowired
    Utils utils;
    @Override
    public ChallengeDto createChallenge(String userId, ChallengeDto challengeDto) {
        UserEntity userEntity = userRepository.findByUserId(userId);
        if(userEntity == null) {
            throw new UsernameNotFoundException(userId);
        }


        ModelMapper modelMapper = new ModelMapper();
        ChallengeEntity challengeEntity = modelMapper.map(challengeDto, ChallengeEntity.class);
        challengeEntity.setUserDetails(userEntity);
        challengeEntity.setChallengeId(utils.generateChallengeId(30));
        ChallengeEntity savedChallenge = challengeRepository.save(challengeEntity);

        return modelMapper.map(savedChallenge, ChallengeDto.class);
    }
}
