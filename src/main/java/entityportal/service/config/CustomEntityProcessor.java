package entityportal.service.config;

import entityportal.service.model.Entity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
public class CustomEntityProcessor implements ItemProcessor<Entity, Entity> {
    @Override
    public Entity process(Entity item) throws Exception {
        try {
            String date = item.getDate();
            String time = item.getTime();
            String dateAndTime = date + " - " + time + " +0530";
            ZonedDateTime measuredOn = ZonedDateTime.parse(dateAndTime, DateTimeFormatter.ofPattern("MM/dd/yyyy - HH:mm:ss Z"));
            item.setModifiedOn(measuredOn);
            item.setTimeStamp(measuredOn.toEpochSecond());
            log.info("Timestamp is " + item.getTimeStamp());

        } catch (Exception e) {
            log.error("Unable to convert date");
        }
        return item;
    }
}
