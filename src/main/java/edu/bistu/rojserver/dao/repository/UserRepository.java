package edu.bistu.rojserver.dao.repository;

import edu.bistu.rojserver.dao.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>
{
    Optional<UserEntity> findUserEntityByUsernameAndPassword(String username, String password);
    boolean existsByUsername(String username);
}
