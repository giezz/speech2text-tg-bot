package giezz.speech2texttgbot.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.file.Path;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeepgramService {
    private final ObjectMapper objectMapper;
    private final DeepgramClient deepgramClient;

    public String getTranscription(Path path) {
        String response = deepgramClient.sendAudioRequest(path);
        response = extractTranscription(response);
        log.info("received transcription: {}", response);
        if (response.isBlank()) {
            response = "Не удалось конвертировать в текст";
        }

        return response;
    }

    private String extractTranscription(String json) {
        try {
            JsonNode root = objectMapper.readTree(json);
            JsonNode transcriptNode = root
                    .path("results")
                    .path("channels")
                    .get(0)
                    .path("alternatives")
                    .get(0)
                    .path("transcript");

            return transcriptNode.asText();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
