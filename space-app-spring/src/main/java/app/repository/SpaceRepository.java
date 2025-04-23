package app.repository;


import app.entity.Space;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface SpaceRepository extends JpaRepository<Space, Integer> {
    boolean existsByName(String name);

    Optional<Space> findByName(String name);

    List<Space> findAllByOrderByIdAsc();
}
