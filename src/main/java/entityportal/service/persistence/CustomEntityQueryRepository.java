package entityportal.service.persistence;

import entityportal.service.model.Entity;
import entityportal.service.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Service
public class CustomEntityQueryRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CustomEntityQueryRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Entity> getAllEntities() {
        String sql = " SELECT ce.ID as id," +
                "ce.Date as date," +
                "ce.Type as type," +
                "ce.Value as value," +
                "ce.CreatedBy as createdBy," +
                "ue.UserName as createdUser " +
                " FROM custom_entity ce" +
                " LEFT OUTER JOIN user_entity ue on ue.ID=ce.CreatedBy " +
                " order by ce.Date desc ";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Entity entity = new Entity();
            entity.setId(rs.getLong("id"));
            entity.setType(rs.getString("type"));
            entity.setValue(rs.getString("value"));
            User createdBy = new User();
            createdBy.setId(rs.getLong("createdBy"));
            createdBy.setUserName(rs.getString("createdUser"));
            entity.setCreatedBy(createdBy);
            ZoneId zoneId = ZoneId.of("Asia/Kolkata");
            Long date = rs.getLong("date");
            if (date > 0L) {
                entity.setModifiedOn(ZonedDateTime.ofInstant(Instant.ofEpochSecond(date), zoneId));
            }

            return entity;
        });
    }
}
