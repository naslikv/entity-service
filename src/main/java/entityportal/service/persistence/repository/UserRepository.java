package entityportal.service.persistence.repository;

import entityportal.service.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity,Long> {
    boolean existsByUserName(String userName);

    boolean existsByUserNameAndPassword(String userName,String password);
    Optional<UserEntity> findByUserName(String userName);
}
