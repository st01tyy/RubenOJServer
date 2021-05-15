package edu.bistu.rojserver.dao.repository;

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
    List<ProblemEntity> getAllByAuthor(UserEntity author);
    Optional<ProblemEntity> findByProblemID(Long problemID);

    @Query(value = "select problemid as problemID, title as title, difficulty as difficulty, count(distinct submissions.author_userid) as acceptedSubmissionNumber from problems left join submissions on problemid=submissions.problem_entity_problemid and test_submission=0 and result='Accepted'where status=4 group by problemid order by problemID desc limit ?1, ?2", nativeQuery = true)
    List<ProblemTableItem> findProblemTableItems(int offset, int count);

    int countByStatus(ProblemEntity.Status status);
}
