package dania.app.web.repository;

import dania.app.web.entities.MemberEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

    List<MemberEntity> findAll();
    MemberEntity findById(Integer id);
    Long countAllByFlagEquals(byte value);
    Page<MemberEntity> findAllByFlagEquals(byte value, Pageable pageable);
    MemberEntity findByStaffNumber(String staffNumber);
}

