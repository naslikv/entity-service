package entityportal.service.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import entityportal.service.common.Constants;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class Entity {
    Long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_TIME_FORMAT_ISO_8601)
    ZonedDateTime modifiedOn;
    String type;
    String value;
    User createdBy;
    String date;
    String time;
    Long timeStamp;
    Long createdByUserID;
}
