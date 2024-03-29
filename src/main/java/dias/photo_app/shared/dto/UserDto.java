package dias.photo_app.shared.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class UserDto implements Serializable {
    private static final long serialVersionUID = 7296822659397222860L;
    private long id;
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String encryptedPassword;
    private String emailVerificationToken;
    private Boolean emailVerificationStatus = false;
    private List<ChallengeDto> challenges = new ArrayList<>();
}
