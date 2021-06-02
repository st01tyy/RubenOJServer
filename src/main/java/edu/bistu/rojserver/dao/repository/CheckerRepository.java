package edu.bistu.rojserver.dao.repository;

import edu.bistu.rojserver.dao.entity.CheckerEntity;
import edu.bistu.rojserver.dao.entity.ProblemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CheckerRepository extends JpaRepository<CheckerEntity, Long>
{
    Optional<CheckerEntity> findByProblemEntity(ProblemEntity problemEntity);

    Optional<CheckerEntity> findByCheckerID(Long checkerID);
}
