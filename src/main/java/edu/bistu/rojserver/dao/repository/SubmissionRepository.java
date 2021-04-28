package edu.bistu.rojserver.dao.repository;

import edu.bistu.rojserver.dao.entity.SubmissionEntity;
import edu.bistu.rojserver.dao.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubmissionRepository extends JpaRepository<SubmissionEntity, Long>
{
    List<SubmissionEntity> findAllByAuthor(UserEntity author);
}
