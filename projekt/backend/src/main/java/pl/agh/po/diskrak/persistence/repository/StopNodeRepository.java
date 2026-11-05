package pl.agh.po.diskrak.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.agh.po.diskrak.persistence.entity.StopNodeEntity;

import java.util.List;
import java.util.UUID;

public interface StopNodeRepository extends JpaRepository<StopNodeEntity, UUID> {

    List<StopNodeEntity> findByDepotTrue();

    List<StopNodeEntity> findByNameContainingIgnoreCase(String fragment);
}
