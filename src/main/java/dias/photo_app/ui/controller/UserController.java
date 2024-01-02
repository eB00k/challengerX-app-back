package dias.photo_app.ui.controller;

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

    // GET: http://localhost:4000/photo-app/users?page=0&limit25
    @GetMapping
    public List<UserRest> getUsers(@RequestParam(value = "page", defaultValue = "0") int page,
                                   @RequestParam(value = "limit", defaultValue = "25") int limit) {
        ModelMapper modelMapper = new ModelMapper();
        List<UserRest> returnValue = new ArrayList<>();

        List<UserDto> users = userService.getUsers(page, limit);

        for (UserDto userDto : users) {
            UserRest userModel = modelMapper.map(userDto, UserRest.class);
            returnValue.add(userModel);
        }

        return returnValue;
    }

    // GET: http://localhost:4000/photo-app/users/userId
    @GetMapping(value = "/{userId}")
    public UserRest getUserById(@PathVariable String userId) {
        ModelMapper modelMapper = new ModelMapper();

        UserDto userDto = userService.getUserById(userId);

        return modelMapper.map(userDto, UserRest.class);
    }

    // POST: http://localhost:4000/challenger-app/users + payload
    @PostMapping
    public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception {
        UserRest returnValue = new UserRest();
        ModelMapper modelMapper = new ModelMapper();

        if(userDetails.getFirstName().isEmpty() || userDetails.getLastName().isEmpty() ||
                userDetails.getEmail().isEmpty() || userDetails.getPassword().isEmpty()) {
//            throw new UserServiceExceptions(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
            throw new NullPointerException("The object is null");
        }

        UserDto userDto = modelMapper.map(userDetails, UserDto.class);

        UserDto createdUser = userService.createUser(userDto);

        return modelMapper.map(createdUser, UserRest.class);
    }

    // PUT: http://localhost:4000/challenger-app/users/userId + payload
    @PutMapping(path="/{userId}")
    public UserRest updateUser(@PathVariable String userId, @RequestBody UserDetailsRequestModel userDetails) {
        UserRest returnValue = new UserRest();

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userDetails, userDto);

        UserDto updateUser = userService.updateUser(userId, userDto);
        BeanUtils.copyProperties(updateUser, returnValue);

        return returnValue;
    }

    // DELETE: http://localhost:4000/challenger-app/users/userId
    @DeleteMapping(path="/{userId}")
    public OperationStatusModel deleteUser(@PathVariable String userId) {
        OperationStatusModel returnValue = new OperationStatusModel();
        userService.deleteUser(userId);
        returnValue.setOperationName(RequestOperationName.DELETE.name());
        returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());

        return returnValue;
    }

    // POST: http://localhost:4000/challenger-app/users/{userId}/posts + payload
    @PostMapping(path = "/{userId}/challenges")
    public ChallengeRest createChallenge(@PathVariable String userId, @RequestBody ChallengeDetailsRequestModel challengeDetails) {
        // Validate challengeDetails...
        System.out.println("cccccccccccc1");
        ModelMapper modelMapper = new ModelMapper();
        ChallengeDto challengeDto = modelMapper.map(challengeDetails, ChallengeDto.class);
        ChallengeDto createdChallenge = challengeService.createChallenge(userId, challengeDto);

        return modelMapper.map(createdChallenge, ChallengeRest.class);
    }

//    // GET: http://localhost:4000/photo-app/users/{userId}/posts
//    @GetMapping(path = "/{userId}/posts")
//    public List<PostRest> getPosts(@PathVariable String userId) {
//        List<PostRest> returnValue = new ArrayList<>();
//
//        List<PostDto> posts = userService.getPosts(userId);
//
//        for (PostDto postDto : posts) {
//            PostRest postModel = new PostRest();
//            BeanUtils.copyProperties(postDto, postModel);
//            returnValue.add(postModel);
//        }
//
//        return returnValue;
//    }
}
