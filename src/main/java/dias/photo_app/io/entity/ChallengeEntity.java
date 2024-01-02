package dias.photo_app.io.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity(name = "challenges")
public class ChallengeEntity implements Serializable {
    private static final long serialVersionUID = 8_480_221_491_105_041_380L;
    @Id
    @GeneratedValue
    private Long id;
    @Column(length = 30, nullable = false)
    private String challengeId;               // public ID to be shared inside server response
    @Column(length = 200, nullable = false)
    private String title;
    @Column(length = 1000, nullable = false)
    private String description;
    @Column(nullable = false)
    private Date startDate;
    @Column(nullable = false)
    private Date endDate;
    @Column(nullable = false)
    private int days;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userDetails;
}
