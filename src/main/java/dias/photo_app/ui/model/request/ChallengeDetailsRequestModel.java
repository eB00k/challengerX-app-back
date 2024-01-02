package dias.photo_app.ui.model.request;

import lombok.Data;

import java.util.Date;

@Data
public class ChallengeDetailsRequestModel {
    private String title;
    private String description;
    private Date startDate;
    private Date endDate;
    private int days;
}
