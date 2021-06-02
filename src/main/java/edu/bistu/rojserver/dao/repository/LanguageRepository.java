package edu.bistu.rojserver.dao.repository;

import edu.bistu.rojserver.dao.entity.LanguageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LanguageRepository extends JpaRepository<LanguageEntity, Long>
{
    List<LanguageEntity> findAll();
    Optional<LanguageEntity> findLanguageEntityByName(String name);
    LanguageEntity getByName(String name);
    boolean existsByName(String name);
}
