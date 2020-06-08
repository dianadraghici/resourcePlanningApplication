package dania.app.web.repository;

import dania.app.web.entities.MemberPositionViewEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberPositionViewRepository extends JpaRepository<MemberPositionViewEntity, String> {

    MemberPositionViewEntity findMemberPositionViewEntityById(String id);
}
