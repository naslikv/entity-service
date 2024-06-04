package entityportal.service.config;

import entityportal.service.model.Entity;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class EntityFieldSetMapper implements FieldSetMapper<Entity> {
    @Override
    public Entity mapFieldSet(FieldSet fieldSet) throws BindException {
        Entity entity =new Entity();
        entity.setType(fieldSet.readString("type"));
        entity.setDate(fieldSet.readString("date"));
        entity.setTime(fieldSet.readString("time"));
        entity.setValue(fieldSet.readString("value"));
        return entity;
    }
}
