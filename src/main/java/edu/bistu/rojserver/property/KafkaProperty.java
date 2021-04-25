package edu.bistu.rojserver.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "property.kafka")
@Data
public class KafkaProperty
{
    private String server;
    private String location;     //.../kafka/bin/windows;
    private boolean windows;   //kafka CLIENT operating system
    private int defaultPartitionNumber = 3;
}
