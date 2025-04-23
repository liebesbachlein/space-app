package app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import app.entity.SpaceType;

@Repository
public interface SpaceTypeRepository extends JpaRepository<SpaceType, Integer> {
    boolean existsByName(String name);
}
