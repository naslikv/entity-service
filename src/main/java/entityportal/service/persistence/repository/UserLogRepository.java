package entityportal.service.persistence.repository;

import entityportal.service.persistence.entity.UserLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLogRepository extends JpaRepository<UserLogEntity,Long> {
}
