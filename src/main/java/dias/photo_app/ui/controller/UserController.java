package dias.photo_app.ui.controller;

import dias.photo_app.exceptions.UserServiceExceptions;
import dias.photo_app.service.ChallengeService;
import org.modelmapper.ModelMapper;
import dias.photo_app.service.UserService;
import dias.photo_app.shared.dto.ChallengeDto;
import dias.photo_app.shared.dto.UserDto;
import dias.photo_app.ui.model.request.ChallengeDetailsRequestModel;
import dias.photo_app.ui.model.request.UserDetailsRequestModel;
import dias.photo_app.ui.model.response.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private ChallengeService challengeService;

    @Autowired ModelMapper modelMapper;

    // GET: http://localhost:4001/photo-app/users?page=0&limit25
    @GetMapping
    public List<UserRest> getUsers(@RequestParam(value = "page", defaultValue = "0") int page,
                                   @RequestParam(value = "limit", defaultValue = "25") int limit) {
        List<UserRest> returnValue = new ArrayList<>();

        List<UserDto> users = userService.getUsers(page, limit);

        for (UserDto userDto : users) {
            UserRest userModel = modelMapper.map(userDto, UserRest.class);
            returnValue.add(userModel);
        }

        return returnValue;
    }

    // GET: http://localhost:4001/photo-app/users/userId
    @GetMapping(value = "/{userId}")
    public UserRest getUserById(@PathVariable String userId) {
        UserDto userDto = userService.getUserById(userId);

        return modelMapper.map(userDto, UserRest.class);
    }

    // POST: http://localhost:4001/challenger-app/users + payload
    @PostMapping
    public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception {
        UserRest returnValue = new UserRest();
        ModelMapper modelMapper = new ModelMapper();

        if(userDetails.getFirstName().isEmpty() || userDetails.getLastName().isEmpty() ||
                userDetails.getEmail().isEmpty() || userDetails.getPassword().isEmpty()) {
            throw new UserServiceExceptions(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
        }

        UserDto userDto = modelMapper.map(userDetails, UserDto.class);

        UserDto createdUser = userService.createUser(userDto);

        return modelMapper.map(createdUser, UserRest.class);
    }

    // PUT: http://localhost:4001/challenger-app/users/userId + payload
    @PutMapping(path="/{userId}")
    public UserRest updateUser(@PathVariable String userId, @RequestBody UserDetailsRequestModel userDetails) {
        UserRest returnValue = new UserRest();

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userDetails, userDto);

        UserDto updatedUser = userService.updateUser(userId, userDto);
        BeanUtils.copyProperties(updatedUser, returnValue);

        return returnValue;
    }

    // DELETE: http://localhost:4001/challenger-app/users/userId
    @DeleteMapping(path="/{userId}")
    public OperationStatusModel deleteUser(@PathVariable String userId) {
        OperationStatusModel returnValue = new OperationStatusModel();
        userService.deleteUser(userId);
        returnValue.setOperationName(RequestOperationName.DELETE.name());
        returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());

        return returnValue;
    }

    // GET: http://localhost:4001/challenger-app/users/email-verification?token=secret-token-value
    @GetMapping(path = "/email-verification")
    public OperationStatusModel verifyEmailToken(@RequestParam(value = "token", defaultValue = "") String token) {
        OperationStatusModel returnValue = new OperationStatusModel();
        returnValue.setOperationName(RequestOperationName.VERIFY_EMAIL.name());

        boolean isVerified = userService.verifyEmailToken(token);

        if (isVerified == true) {
            returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
        } else {
            returnValue.setOperationResult(RequestOperationStatus.ERROR.name());
        }

        return returnValue;
    }

}
