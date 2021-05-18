package edu.bistu.rojserver.dao.repository;

import edu.bistu.rojserver.dao.DownloadFile;
import edu.bistu.rojserver.dao.entity.ProblemEntity;
import edu.bistu.rojserver.dao.entity.TestCaseFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TestCaseFileRepository extends JpaRepository<TestCaseFileEntity, Long>
{
    List<TestCaseFileEntity> findAllByProblemEntity(ProblemEntity problemEntity);

    @Query(value = "select input_file_name as fileName, input as file from test_case_file where case_id = ?1", nativeQuery = true)
    DownloadFile downloadTestCaseInputFileByCaseID(Long caseID);

    @Query(value = "select output_file_name as fileName, output as file from test_case_file where case_id = ?1", nativeQuery = true)
    DownloadFile downloadTestCaseOutputFileByCaseID(Long caseID);
}
