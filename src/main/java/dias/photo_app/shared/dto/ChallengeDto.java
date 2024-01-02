package dias.photo_app.shared.dto;

import lombok.Data;
import java.util.Date;

@Data
public class ChallengeDto {
    private long id;
    private String challengeId;
    private String title;
    private String description;
    private Date startDate;
    private Date endDate;
    private int days;
    private UserDto userDetails;
}
