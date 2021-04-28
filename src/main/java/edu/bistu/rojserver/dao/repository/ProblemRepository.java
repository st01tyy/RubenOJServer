package edu.bistu.rojserver.dao.repository;

import edu.bistu.rojserver.dao.entity.ProblemEntity;
import edu.bistu.rojserver.dao.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProblemRepository extends JpaRepository<ProblemEntity, Long>
{
    List<ProblemEntity> getAllByAuthor(UserEntity author);
    Optional<ProblemEntity> findByProblemID(Long problemID);
}
