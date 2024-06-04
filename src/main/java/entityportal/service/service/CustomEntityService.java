package entityportal.service.service;

import entityportal.service.model.Entity;
import entityportal.service.model.User;
import entityportal.service.persistence.CustomEntityQueryRepository;
import entityportal.service.persistence.entity.CustomEntity;
import entityportal.service.persistence.repository.CustomEntityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CustomEntityService {
    private final CustomEntityQueryRepository customEntityQueryRepository;
    private final CustomEntityRepository customEntityRepository;

    @Autowired
    public CustomEntityService(CustomEntityQueryRepository customEntityQueryRepository,
                               CustomEntityRepository customEntityRepository) {
        this.customEntityQueryRepository = customEntityQueryRepository;
        this.customEntityRepository = customEntityRepository;
    }

    public List<Entity> getEntities() {
        return customEntityQueryRepository.getAllEntities();
    }

    public Entity modify(Entity customEntity) {
        Optional<CustomEntity> entity = customEntityRepository.findById(customEntity.getId());
        if (entity.isPresent()) {
            CustomEntity entityToModify = entity.get();
            if (customEntity.getModifiedOn() != null) {
                entityToModify.setDate(customEntity.getModifiedOn().toEpochSecond());
            }
            if (customEntity.getValue() != null) {
                entityToModify.setValue(customEntity.getValue());
            }
            if (customEntity.getType() != null) {
                entityToModify.setType(customEntity.getType());
            }
            CustomEntity savedEntity = null;
            try {
                savedEntity = customEntityRepository.save(entityToModify);
            } catch (Exception e) {
                e.printStackTrace();
                log.info(e.getMessage());
            }
            if (savedEntity != null) {
                Entity response = new Entity();
                response.setId(savedEntity.getId());
                response.setType(savedEntity.getType());
                response.setValue(savedEntity.getValue());
                response.setModifiedOn(ZonedDateTime.ofInstant(Instant.ofEpochSecond(savedEntity.getDate()), ZoneId.of("Asia/Kolkata")));
                User createdBy = new User();
                createdBy.setId(savedEntity.getCreatedBy());
                response.setCreatedBy(createdBy);
                return response;
            }
        }
        return null;
    }

    public Entity getEntityByID(Long id) {
        Optional<CustomEntity> entity = customEntityRepository.findById(id);
        if (entity.isPresent()) {
            Entity response = new Entity();
            response.setId(entity.get().getId());
            response.setType(entity.get().getType());
            response.setValue(entity.get().getValue());
            response.setModifiedOn(ZonedDateTime.ofInstant(Instant.ofEpochSecond(entity.get().getDate()), ZoneId.of("Asia/Kolkata")));
            User createdBy = new User();
            createdBy.setId(entity.get().getCreatedBy());
            response.setCreatedBy(createdBy);
            return response;
        }
        return null;
    }
}
