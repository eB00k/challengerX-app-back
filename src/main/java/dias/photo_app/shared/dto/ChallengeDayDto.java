package dias.photo_app.shared.dto;

import dias.photo_app.shared.DayStatus;
import lombok.Data;

@Data
public class ChallengeDayDto {
    private long id;
    private String challengeDayId;
    private int dayNumber;
    private DayStatus dayStatus;
}
