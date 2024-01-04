package dias.photo_app.io.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity(name = "tasks")
public class TaskEntity {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String taskName;

    @Column(nullable = false)
    private boolean isDone;

    @ManyToOne
    @JoinColumn(name = "challenge_day_id")
    private ChallengeDayEntity challengeDay;
}