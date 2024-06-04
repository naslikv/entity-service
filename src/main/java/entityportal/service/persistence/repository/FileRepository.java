package entityportal.service.persistence.repository;

import entityportal.service.persistence.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<FileEntity,Long> {
}
