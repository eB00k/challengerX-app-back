package dias.photo_app.io.entity;

import dias.photo_app.shared.DayStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity(name = "challenge_days")
public class ChallengeDayEntity implements Serializable {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private int dayNumber;

    @Column(nullable = false)
    private String challengeDayId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DayStatus dayStatus;

    @ManyToOne
    @JoinColumn(name = "challenge_id")
    private ChallengeEntity challenge;
}
