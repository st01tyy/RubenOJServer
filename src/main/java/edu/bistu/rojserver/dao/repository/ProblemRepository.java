package edu.bistu.rojserver.dao.repository;

import edu.bistu.rojserver.dao.ProblemStatus;
import edu.bistu.rojserver.dao.ProblemTableItem;
import edu.bistu.rojserver.dao.entity.ProblemEntity;
import edu.bistu.rojserver.dao.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProblemRepository extends JpaRepository<ProblemEntity, Long>
{
    List<ProblemEntity> findAllByAuthor(UserEntity author);

    Optional<ProblemEntity> findByProblemID(Long problemID);

    ProblemEntity getByProblemID(Long problemID);

    @Query(value = "select problem.problem_id as problemID, title as title, difficulty as difficulty, count(distinct submission.user_id) as acceptedSubmissionNumber from problem left join submission on problem.problem_id = submission.problem_id and submission.problem_status > 2 and result = 7 where problem.problem_status = 4 group by problem.problem_id order by problem.problem_id desc limit ?1, ?2", nativeQuery = true)
    List<ProblemTableItem> findProblemTableItems(int offset, int count);

    int countByProblemStatus(ProblemStatus problemStatus);

    @Query(value = "select user_id from problem where problem_id = ?1", nativeQuery = true)
    Long getAuthorID(Long problemID);

    boolean existsByProblemID(Long problemID);
}
