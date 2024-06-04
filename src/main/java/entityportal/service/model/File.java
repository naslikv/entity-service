package entityportal.service.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import entityportal.service.common.Constants;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class File {
    Long id;
    String name;
    String url;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_TIME_FORMAT_ISO_8601)
    ZonedDateTime createdOn;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_TIME_FORMAT_ISO_8601)
    ZonedDateTime modifiedOn;
    User createdBy;
    User modifiedBy;
}
