package edu.bistu.rojserver.dao.repository;

import edu.bistu.rojserver.dao.ProblemStatus;
import edu.bistu.rojserver.dao.ProblemTableItem;
import edu.bistu.rojserver.dao.entity.ProblemEntity;
import edu.bistu.rojserver.dao.entity.SubmissionEntity;
import edu.bistu.rojserver.dao.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import shared.SubmissionResult;

import java.util.List;

public interface SubmissionRepository extends JpaRepository<SubmissionEntity, Long>
{
    List<SubmissionEntity> findAllByAuthor(UserEntity author);

    List<SubmissionEntity> findAllByAuthorAndProblemEntity(UserEntity author, ProblemEntity problemEntity);

    List<SubmissionEntity> findAllByProblemEntity(ProblemEntity problemEntity);
}
