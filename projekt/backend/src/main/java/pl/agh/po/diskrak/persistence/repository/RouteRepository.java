package pl.agh.po.diskrak.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.agh.po.diskrak.persistence.entity.RouteEntity;

import java.util.List;
import java.util.UUID;

public interface RouteRepository extends JpaRepository<RouteEntity, UUID> {

    List<RouteEntity> findByLineNumber(String lineNumber);
}
