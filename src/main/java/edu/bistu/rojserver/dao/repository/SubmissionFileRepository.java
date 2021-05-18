package edu.bistu.rojserver.dao.repository;

import edu.bistu.rojserver.dao.DownloadFile;
import edu.bistu.rojserver.dao.entity.SubmissionFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SubmissionFileRepository extends JpaRepository<SubmissionFileEntity, Long>
{
    @Query(value = "select source_file_name as fileName, source as file from submission_file where submission_id = ?1", nativeQuery = true)
    DownloadFile downloadSourceFileBySubmissionID(Long submissionID);
}
