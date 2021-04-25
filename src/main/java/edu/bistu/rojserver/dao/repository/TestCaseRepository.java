package edu.bistu.rojserver.dao.repository;

import edu.bistu.rojserver.dao.entity.TestCaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestCaseRepository extends JpaRepository<TestCaseEntity, Long>
{

}
