package entityportal.service.persistence.repository;

import entityportal.service.persistence.entity.ImportTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImportTypeRepository extends JpaRepository<ImportTypeEntity,Long> {
}
