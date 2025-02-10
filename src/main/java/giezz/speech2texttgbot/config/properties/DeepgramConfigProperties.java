package giezz.speech2texttgbot.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "api.deepgram")
@Getter
@Setter
public class DeepgramConfigProperties {
    String url;
    String token;
    QueryParams queryParams;

    @Getter
    @Setter
    public static class QueryParams {
        String model;
        String smartFormat;
        String detectLanguage;
    }
}
