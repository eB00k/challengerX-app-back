package dias.photo_app.ui.model.response;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserRest {
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private List<ChallengeRest> challenges = new ArrayList<>();
}
