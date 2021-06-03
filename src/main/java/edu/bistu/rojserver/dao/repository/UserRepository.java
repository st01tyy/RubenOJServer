package edu.bistu.rojserver.dao.repository;

import edu.bistu.rojserver.dao.entity.UserEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>
{
    Optional<UserEntity> findUserEntityByUsernameAndPassword(String username, String password);
    Optional<UserEntity> findUserEntityByUsername(String username);
    boolean existsByUsername(String username);

    Slice<UserEntity> findAllByUsernameLike(Pageable pageable, String username);
    Slice<UserEntity> findAllByRoleEquals(Pageable pageable, UserEntity.Role role);

    @Query(value = "select * from user_information", nativeQuery = true)
    Slice<UserEntity> findUsers(Pageable pageable);

    UserEntity getByUserID(Long userID);
}
