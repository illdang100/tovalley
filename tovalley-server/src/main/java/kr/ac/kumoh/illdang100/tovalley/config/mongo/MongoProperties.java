package kr.ac.kumoh.illdang100.tovalley.config.mongo;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "mongodb")
public class MongoProperties {
    String client;
    String name;
}
