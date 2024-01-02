package dias.photo_app.ui.model.response;

import lombok.Data;

import java.util.Date;

@Data
public class ChallengeRest {
    private String challengeId;
    private String title;
    private String description;
    private Date startDate;
    private Date endDate;
    private int days;
}
