package pl.agh.po.diskrak.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.agh.po.diskrak.persistence.entity.TrackSegmentEntity;

import java.util.List;
import java.util.UUID;

public interface TrackSegmentRepository extends JpaRepository<TrackSegmentEntity, UUID> {

    @Query("select t from TrackSegmentEntity t join fetch t.fromNode join fetch t.toNode")
    List<TrackSegmentEntity> findAllWithNodes();
}
