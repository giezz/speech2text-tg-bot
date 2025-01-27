package giezz.speech2texttgbot.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
@RequiredArgsConstructor
public class DeepgramClient {
    private final RestClient deepgramApi;

    public String sendAudioRequest(Path path) {
        return deepgramApi.post()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("model", "nova-2")
                        .queryParam("smart_format", "true")
                        .queryParam("detect_language", "true")
                        .build())
                .contentType(MediaType.valueOf("audio/ogg"))
                .body(readAudioFileBytes(path))
                .retrieve()
                .body(String.class);
    }

    private byte[] readAudioFileBytes(Path path) {
        try {
            return Files.readAllBytes(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
