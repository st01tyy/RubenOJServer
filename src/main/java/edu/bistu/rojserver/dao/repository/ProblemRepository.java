package edu.bistu.rojserver.dao.repository;

import edu.bistu.rojserver.dao.entity.ProblemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProblemRepository extends JpaRepository<ProblemEntity, Long>
{

}
