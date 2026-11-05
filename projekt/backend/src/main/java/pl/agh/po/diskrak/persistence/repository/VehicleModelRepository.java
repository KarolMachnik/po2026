package pl.agh.po.diskrak.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.agh.po.diskrak.persistence.entity.VehicleModelEntity;

import java.util.List;

public interface VehicleModelRepository extends JpaRepository<VehicleModelEntity, String> {

    List<VehicleModelEntity> findByVehicleCategory(String vehicleCategory);
}
