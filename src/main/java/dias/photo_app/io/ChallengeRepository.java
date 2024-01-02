package dias.photo_app.io;

import dias.photo_app.io.entity.ChallengeEntity;
import dias.photo_app.io.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChallengeRepository extends CrudRepository<ChallengeEntity, Long> {

    List<ChallengeEntity> findAllByUserDetails(UserEntity userEntity);
}

