package dania.app.web.repository;

import dania.app.web.entities.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository  extends JpaRepository<ProjectEntity, Integer> {

    ProjectEntity findByProjectCode(String projectCode);

    ProjectEntity findOneById (Integer id);
}
