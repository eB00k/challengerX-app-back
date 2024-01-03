package dias.photo_app.service.impl;

import dias.photo_app.exceptions.UserServiceExceptions;
import dias.photo_app.io.entity.ChallengeEntity;
import dias.photo_app.io.entity.UserEntity;
import dias.photo_app.io.UserRepository;
import dias.photo_app.service.UserService;
import dias.photo_app.shared.Utils;
import dias.photo_app.shared.dto.ChallengeDto;
import dias.photo_app.shared.dto.UserDto;
import dias.photo_app.ui.model.response.ErrorMessages;
import jdk.swing.interop.SwingInterOpUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Utils utils;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public List<UserDto> getUsers(int page, int limit) {
        List<UserDto> returnValue = new ArrayList<>();
        Pageable pageableRequest = PageRequest.of(page, limit);;
        Page<UserEntity> usersPage = userRepository.findAll(pageableRequest);

        List<UserEntity> users = usersPage.getContent();

        for (UserEntity userEntity : users) {
            UserDto userDto = modelMapper.map(userEntity, UserDto.class);
            System.out.println(userDto.getFirstName());
            returnValue.add(userDto);
        }
        return returnValue;
    }

    @Override
    public UserDto getUserById(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId);

        if(userEntity == null) {
            throw new UsernameNotFoundException(userId);
        }

        for(ChallengeEntity challenge : userEntity.getChallenges()) {
            System.out.println(challenge.getTitle());
        }

        return modelMapper.map(userEntity, UserDto.class);
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        // checking whether the user with the specified email exists to prevent duplicate records
        UserEntity storedUserDetails = userRepository.findByEmail(userDto.getEmail());
        if(storedUserDetails != null) {
            throw new IllegalArgumentException("Record with such email already exits");
        }

        UserEntity userEntity = modelMapper.map(userDto, UserEntity.class);


        String publicUserId = utils.generateUserId(30);
        userEntity.setUserId(publicUserId);
        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
        userEntity.setEmailVerificationToken(Utils.generateEmailVerificationToken(publicUserId));
        userEntity.setEmailVerificationStatus(false);

        // a new User
        UserEntity userDetails = new UserEntity(userEntity.getUserId(),userEntity.getFirstName(), userEntity.getLastName(), userEntity.getEmail(), userEntity.getEncryptedPassword(), userEntity.getEmailVerificationToken(), userEntity.getEmailVerificationStatus());

        // list of challenges
        for(ChallengeEntity challenge : userEntity.getChallenges()) {
            challenge.setChallengeId(utils.generateChallengeId(30));
            challenge.setUserDetails(userDetails);
            System.out.println(challenge);
        }

        UserEntity storedUser = userRepository.save(userEntity);
        return modelMapper.map(storedUser, UserDto.class);
    }

    @Override
    public UserDto getUser(String email) {
        UserDto returnValue = new UserDto();
        UserEntity userEntity = userRepository.findByEmail(email);

        if(userEntity == null) {
            throw new UserServiceExceptions(ErrorMessages.EMAIL_ADDRESS_NOT_FOUND.getErrorMessage());
        }

        BeanUtils.copyProperties(userEntity, returnValue);

        return returnValue;
    }

    @Override
    public UserDto updateUser(String userId, UserDto userDto) {
        UserDto returnValue = new UserDto();
        UserEntity userEntity = userRepository.findByUserId(userId);
        if (userEntity == null) {
            throw new UserServiceExceptions(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
        }

        userEntity.setFirstName(userDto.getFirstName());
        userEntity.setLastName(userDto.getLastName());
        UserEntity updatedEntity = userRepository.save(userEntity);
        BeanUtils.copyProperties(updatedEntity, returnValue);

        return returnValue;
    }



    @Override
    public void deleteUser(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId);
        if(userEntity == null) {
            throw new UsernameNotFoundException(userId);
        }
        userRepository.delete(userEntity);
    }

    @Override
    public boolean verifyEmailToken(String token) {
        boolean returnValue = false;
        // Find user by token
        UserEntity userEntity = userRepository.findUserByEmailVerificationToken(token);

        if (userEntity != null) {
            boolean hasTokenExpired = Utils.hasTokenExpired(token);
            if (!hasTokenExpired) {
                userEntity.setEmailVerificationToken(null);
                userEntity.setEmailVerificationStatus(Boolean.TRUE);
                userRepository.save(userEntity);
                returnValue = true;
            }
        }
        return returnValue;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(username);

        if(userEntity == null) {
            throw new UsernameNotFoundException(username);
        }

        return new User(username, userEntity.getEncryptedPassword(), new ArrayList<>());
    }
}
