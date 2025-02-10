package giezz.speech2texttgbot.config;

import giezz.speech2texttgbot.config.properties.DeepgramConfigProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Configuration
@RequiredArgsConstructor
public class RestClientConfig {
    final DeepgramConfigProperties deepgramConfig;

    @Value("${api.telegram.url}")
    String telegramApiUrl;

    @Bean
    public RestClient deepgramApi() {
        return RestClient.builder()
            .baseUrl(deepgramConfig.getUrl())
            .defaultHeader(AUTHORIZATION, "Token " + deepgramConfig.getToken())
            .build();
    }

    @Bean
    public RestClient telegramApi() {
        return RestClient.builder()
            .baseUrl(telegramApiUrl)
            .build();
    }
}
