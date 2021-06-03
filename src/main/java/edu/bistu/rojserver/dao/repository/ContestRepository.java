package edu.bistu.rojserver.dao.repository;

import edu.bistu.rojserver.dao.entity.ContestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ContestRepository extends JpaRepository<ContestEntity, Long>
{
    @Query(value = "select * from contest order by contest_id desc", nativeQuery = true)
    List<ContestEntity> getContestEntityList();

    ContestEntity getByContestID(Long contestID);

    Optional<ContestEntity> findByContestID(Long contestID);

    @Query(value = "select distinct submission.user_id from submission inner join problem on submission.problem_id = problem.problem_id and problem.contest_id = ?1 where submission.problem_status = 3", nativeQuery = true)
    List<Long> getContestants(Long contestID);

    @Query(value = "select sum(a.score) as totalScore, count(*) as passNumber from (select problem.score as score from submission inner join problem on submission.problem_id = problem.problem_id and problem.contest_id = ?2 where submission.user_id = ?1 and submission.problem_status = 3 and submission.result = 7 group by submission.problem_id) a", nativeQuery = true)
    RankTableItem getContestantInformation(Long userID, Long contestID);
}
