package entityportal.service.persistence.repository;

import entityportal.service.persistence.entity.ImportJobEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImportJobRepository extends JpaRepository<ImportJobEntity,Long> {
}
