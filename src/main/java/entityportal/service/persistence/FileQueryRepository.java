package entityportal.service.persistence;

import entityportal.service.model.File;
import entityportal.service.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Service
public class FileQueryRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FileQueryRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<File> getAllFiles() {
        String sql = " SELECT fe.ID as id," +
                "fe.FileName as fileName," +
                "fe.Url as url," +
                "fe.CreatedBy as createdBy," +
                "fe.ModifiedBy as modifiedBy," +
                "fe.CreatedDate as createdDate," +
                "fe.ModifiedDate as modifiedDate," +
                "ue.UserName as createdUser," +
                "uem.UserName as modifiedUser " +
                " FROM file_entity fe " +
                " LEFT OUTER JOIN user_entity ue on ue.ID=fe.CreatedBy " +
                " LEFT OUTER JOIN user_entity uem on uem.ID=fe.ModifiedBy ";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            File file = new File();
            file.setId(rs.getLong("id"));
            file.setName(rs.getString("fileName"));
            User createdBy = new User();
            createdBy.setId(rs.getLong("createdBy"));
            createdBy.setUserName(rs.getString("createdUser"));
            file.setCreatedBy(createdBy);
            User modifiedBy = new User();
            modifiedBy.setId(rs.getLong("modifiedBy"));
            modifiedBy.setUserName(rs.getString("modifiedUser"));
            file.setModifiedBy(modifiedBy);
            ZoneId zoneId = ZoneId.of("Asia/Kolkata");
            Long createdDate = rs.getLong("createdDate");
            if (createdDate > 0L) {
                file.setCreatedOn(ZonedDateTime.ofInstant(Instant.ofEpochSecond(createdDate), zoneId));
            }
            Long modifiedDate = rs.getLong("modifiedDate");
            if (modifiedDate > 0L) {
                file.setModifiedOn(ZonedDateTime.ofInstant(Instant.ofEpochSecond(modifiedDate), zoneId));
            }
            return file;
        });

    }
}
