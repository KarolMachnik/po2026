package pl.agh.po.diskrak.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.agh.po.diskrak.persistence.entity.DistrictEntity;

import java.util.Optional;
import java.util.UUID;

public interface DistrictRepository extends JpaRepository<DistrictEntity, UUID> {

    Optional<DistrictEntity> findByName(String name);
}
