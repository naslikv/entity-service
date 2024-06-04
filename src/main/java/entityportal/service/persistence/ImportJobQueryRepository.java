package entityportal.service.persistence;

import entityportal.service.model.File;
import entityportal.service.model.ImportJob;
import entityportal.service.model.ImportType;
import entityportal.service.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Service
public class ImportJobQueryRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ImportJobQueryRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<ImportJob> getAllImports() {
        String sql = " SELECT ije.ID as id," +
                "ije.TypeID as typeID," +
                "ite.Type as importType," +
                "ite.Description as importDescription," +
                "ije.FileID as fileID," +
                "fe.FileName as fileName," +
                "ije.BatchReference as batchReference," +
                "ije.CreatedBy as createdBy," +
                "ije.ModifiedBy as modifiedBy," +
                "ije.CreatedDate as createdDate," +
                "ije.ModifiedDate as modifiedDate," +
                "ue.UserName as createdUser," +
                "uem.UserName as modifiedUser " +
                " FROM import_job_entity ije " +
                " LEFT OUTER JOIN user_entity ue on ue.ID=ije.CreatedBy " +
                " LEFT OUTER JOIN user_entity uem on uem.ID=ije.ModifiedBy " +
                " LEFT OUTER JOIN import_type_entity ite on ite.ID=ije.TypeID" +
                " LEFT OUTER JOIN file_entity fe on fe.ID=ije.FileID " +
                " order by ije.ModifiedDate desc ";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
           ImportJob job=new ImportJob();
            job.setId(rs.getLong("id"));
            job.setBatchReference(rs.getString("batchReference"));
            File file=new File();
            file.setId(rs.getLong("fileID"));
            file.setName(rs.getString("fileName"));
            job.setFile(file);
            ImportType importType=new ImportType();
            importType.setId(rs.getLong("typeID"));
            importType.setKey(rs.getString("importType"));
            importType.setDescription(rs.getString("importDescription"));
            job.setType(importType);
            User createdBy = new User();
            createdBy.setId(rs.getLong("createdBy"));
            createdBy.setUserName(rs.getString("createdUser"));
            job.setCreatedBy(createdBy);
            User modifiedBy = new User();
            modifiedBy.setId(rs.getLong("modifiedBy"));
            modifiedBy.setUserName(rs.getString("modifiedUser"));
            job.setModifiedBy(modifiedBy);
            ZoneId zoneId = ZoneId.of("Asia/Kolkata");
            Long createdDate = rs.getLong("createdDate");
            if (createdDate > 0L) {
                job.setCreatedOn(ZonedDateTime.ofInstant(Instant.ofEpochSecond(createdDate), zoneId));
            }
            Long modifiedDate = rs.getLong("modifiedDate");
            if (modifiedDate > 0L) {
                job.setModifiedOn(ZonedDateTime.ofInstant(Instant.ofEpochSecond(modifiedDate), zoneId));
            }
            return job;
        });
    }
}
