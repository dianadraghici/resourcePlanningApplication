package dania.app.web.repository;


import dania.app.web.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findById(Integer id);

}

