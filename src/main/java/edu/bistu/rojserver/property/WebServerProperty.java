package edu.bistu.rojserver.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "property.webserver")
@Data
public class WebServerProperty
{
    private int page_size = 20;
    private int page_count_limit = 7;
}
