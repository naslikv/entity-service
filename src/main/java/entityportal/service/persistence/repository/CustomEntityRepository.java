package entityportal.service.persistence.repository;

import entityportal.service.persistence.entity.CustomEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomEntityRepository extends JpaRepository<CustomEntity,Long> {
}
