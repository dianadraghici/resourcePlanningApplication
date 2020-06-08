package dania.app.web.repository;

import dania.app.web.entities.ProjectPositionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProjectPositionRepository extends JpaRepository<ProjectPositionEntity, Integer> {

    List<ProjectPositionEntity> findProjectPositionEntityByProjectEntity_Id(Integer id);
    ProjectPositionEntity findOneById(Integer id);
}
