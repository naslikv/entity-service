package entityportal.service.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import entityportal.service.common.Constants;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class ImportJob {
    Long id;
    File file;
    ImportType type;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_TIME_FORMAT_ISO_8601)
    ZonedDateTime createdOn;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_TIME_FORMAT_ISO_8601)
    ZonedDateTime modifiedOn;
    User createdBy;
    User modifiedBy;
    String batchReference;
}