package dias.photo_app.io.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long Id;
    @Column(nullable = false)
    private String userId;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String encryptedPassword;
    private String emailVerificationToken;
    private Boolean emailVerificationStatus = false;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userDetails", cascade = CascadeType.ALL)
    private List<ChallengeEntity> challenges = new ArrayList<>();

    public UserEntity(String userId, String firstName, String lastName, String email, String encryptedPassword, String emailVerificationToken, Boolean emailVerificationStatus) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.encryptedPassword = encryptedPassword;
        this.emailVerificationToken = emailVerificationToken;
        this.emailVerificationStatus = emailVerificationStatus;
    }
}
