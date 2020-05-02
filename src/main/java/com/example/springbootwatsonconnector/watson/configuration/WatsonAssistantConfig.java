package com.example.springbootwatsonconnector.watson.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "watson.assistant")
public class WatsonAssistantConfig {
    private String assistantId;
    private String apiKey;
    private String versionDate;
    private String serviceURL;
    private long sessionTimeout;

}
