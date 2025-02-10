package giezz.speech2texttgbot.deepgram.client;

import giezz.speech2texttgbot.config.properties.DeepgramConfigProperties;
import giezz.speech2texttgbot.util.FileUtils;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.nio.file.Path;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class DeepgramClient {
    DeepgramConfigProperties deepgramConfig;
    RestClient deepgramApi;
    MediaType AUDIO_OGG = MediaType.valueOf("audio/ogg");

    public String sendAudioRequest(final Path path) {
        return deepgramApi.post()
            .uri(uriBuilder -> uriBuilder
                .queryParam("model", deepgramConfig.getQueryParams().getModel())
                .queryParam("smart_format", deepgramConfig.getQueryParams().getSmartFormat())
                .queryParam("detect_language", deepgramConfig.getQueryParams().getDetectLanguage())
                .build())
            .contentType(AUDIO_OGG)
            .body(FileUtils.readFileBytes(path))
            .retrieve()
            .body(String.class);
    }
}
