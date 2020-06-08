package dania.app.web.repository;

import dania.app.web.entities.ParametersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ParameterRepository extends JpaRepository<ParametersEntity, Long> {
    ParametersEntity findById(Integer id);
    List<ParametersEntity> findParametersEntityByType(String type);
    ParametersEntity findParametersEntityByIdAndType(Integer id, String type);
}
