package dias.photo_app.io;

import dias.photo_app.io.entity.ChallengeEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChallengeRepository extends CrudRepository<ChallengeEntity, Long> {

}

