package dias.photo_app.service;

import dias.photo_app.io.entity.UserEntity;
import dias.photo_app.shared.dto.ChallengeDto;
import dias.photo_app.shared.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    List<UserDto> getUsers(int page, int limit);

    UserDto getUserById(String userId);

    UserDto createUser(UserDto userDto);

    UserDto getUser(String email);

    UserDto updateUser(String userId, UserDto userDto);

    void deleteUser(String userId);

    boolean verifyEmailToken(String token);
}
