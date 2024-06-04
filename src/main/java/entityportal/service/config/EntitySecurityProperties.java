package entityportal.service.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties
@Component
public class EntitySecurityProperties {
    @Value("${entity.security.tokenendpoint}")
    String tokenUrl;
    @Value("${entity.security.tokenverifyendpoint}")
    String verifyTokenUrl;
}
