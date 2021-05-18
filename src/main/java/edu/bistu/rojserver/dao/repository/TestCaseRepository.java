package edu.bistu.rojserver.dao.repository;

import edu.bistu.rojserver.dao.entity.ProblemEntity;
import edu.bistu.rojserver.dao.entity.TestCaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TestCaseRepository extends JpaRepository<TestCaseEntity, Long>
{
    List<TestCaseEntity> findAllByProblemEntity(ProblemEntity problemEntity);

    int countByProblemEntity(ProblemEntity problemEntity);

    @Query(value = "select problem_id from test_case where case_id = ?1", nativeQuery = true)
    Long getTestCaseProblemID(Long caseID);
}
