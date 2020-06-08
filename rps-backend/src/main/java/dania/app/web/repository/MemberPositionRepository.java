package dania.app.web.repository;

import dania.app.web.entities.MemberPositionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MemberPositionRepository extends JpaRepository<MemberPositionEntity,Long> {

    List<MemberPositionEntity> findMemberPositionEntityByMemberEntity_Id(Integer id);
    List<MemberPositionEntity> findMemberPositionEntityByProjectPositionEntity_Id(Integer id);
    MemberPositionEntity findMemberPositionEntityById(Integer id);
}
