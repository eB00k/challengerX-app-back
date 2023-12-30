package dias.photo_app.ui.controller;

import dias.photo_app.exceptions.UserServiceExceptions;
import dias.photo_app.io.entity.UserEntity;
import dias.photo_app.service.UserService;
import dias.photo_app.shared.dto.UserDto;
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

    // GET: http://localhost:4000/photo-app/users?page=0&limit25
    @GetMapping
    public List<UserRest> getUsers(@RequestParam(value = "page", defaultValue = "0") int page,
                                   @RequestParam(value = "limit", defaultValue = "25") int limit) {
        List<UserRest> returnValue = new ArrayList<>();

        List<UserDto> users = userService.getUsers(page, limit);

        for (UserDto userDto : users) {
            UserRest userModel = new UserRest();
            BeanUtils.copyProperties(userDto, userModel);
            returnValue.add(userModel);
        }

        return returnValue;
    }

    // GET: http://localhost:4000/photo-app/users/userId
    @GetMapping(value = "/{userId}")
    public UserRest getUserById(@PathVariable String userId) {
        UserRest returnValue = new UserRest();

        UserDto userDto = userService.getUserById(userId);
        BeanUtils.copyProperties(userDto, returnValue);

        return returnValue;
    }

    // POST: http://localhost:4000/photo-app/users + payload
    @PostMapping
    public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception {
        UserRest returnValue = new UserRest();

        if(userDetails.getFirstName().isEmpty() || userDetails.getLastName().isEmpty() ||
                userDetails.getEmail().isEmpty() || userDetails.getPassword().isEmpty()) {
//            throw new UserServiceExceptions(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
            throw new NullPointerException("The object is null");
        }

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userDetails, userDto);
        UserDto createdUser = userService.createUser(userDto);
        BeanUtils.copyProperties(createdUser, returnValue);
        return returnValue;
    }

    // PUT: http://localhost:4000/photo-app/users/userId + payload
    @PutMapping(path="/{userId}")
    public UserRest updateUser(@PathVariable String userId, @RequestBody UserDetailsRequestModel userDetails) {
        UserRest returnValue = new UserRest();

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userDetails, userDto);

        UserDto updateUser = userService.updateUser(userId, userDto);
        BeanUtils.copyProperties(updateUser, returnValue);

        return returnValue;
    }

    // DELETE: http://localhost:4000/photo-app/users/userId
    @DeleteMapping(path="/{userId}")
    public OperationStatusModel deleteUser(@PathVariable String userId) {
        OperationStatusModel returnValue = new OperationStatusModel();
        userService.deleteUser(userId);
        returnValue.setOperationName(RequestOperationName.DELETE.name());
        returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());

        return returnValue;
    }
}
