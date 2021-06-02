package edu.bistu.rojserver.dao.repository;

import edu.bistu.rojserver.dao.DownloadFile;
import edu.bistu.rojserver.dao.entity.CheckerFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CheckerFileRepository extends JpaRepository<CheckerFileEntity, Long>
{
    CheckerFileEntity getByCheckerID(Long checkerID);

    @Query(value = "select file_name as fileName, arr as file from checker_file where checker_id = ?1", nativeQuery = true)
    DownloadFile download(Long checkerID);
}
