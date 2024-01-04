package dias.photo_app.io;

import dias.photo_app.io.entity.ChallengeDayEntity;
import dias.photo_app.shared.dto.ChallengeDayDto;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChallengeDayRepository extends CrudRepository<ChallengeDayEntity, Long> {
    List<ChallengeDayEntity> findByChallengeId(Long challengeId);

    Optional<ChallengeDayEntity> findByChallengeDayId(String challengeDayId);
}
