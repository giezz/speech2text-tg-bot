package giezz.speech2texttgbot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {
    @Value("${api.deepgram.url}")
    String deepgramApiUrl;

    @Value("${api.telegram.url}")
    String telegramApiUrl;

    @Value("${api.deepgram.token}")
    String deepgramToken;

    @Bean
    public RestClient deepgramApi() {
        return RestClient.builder()
            .baseUrl(deepgramApiUrl)
            .defaultHeader(HttpHeaders.AUTHORIZATION, "Token " + deepgramToken)
            .build();
    }

    @Bean
    public RestClient telegramApi() {
        return RestClient.builder()
            .baseUrl(telegramApiUrl)
            .build();
    }
}
