package giezz.speech2texttgbot.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;

import java.nio.file.Path;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
@Slf4j
public class DeepgramService {
    ObjectMapper objectMapper;
    DeepgramClient deepgramClient;

    public String getTranscription(final Path path) {
        String transcription = extractTranscriptionFromResponse(deepgramClient.sendAudioRequest(path));
        log.debug("Получена транскрипция: {}", transcription);
        if (transcription.isBlank()) {
            transcription = "Не удалось конвертировать в текст";
        }
        return transcription;
    }

    private String extractTranscriptionFromResponse(final String json) {
        try {
            val transcriptNode = objectMapper.readTree(json)
                .path("results")
                .path("channels")
                .get(0)
                .path("alternatives")
                .get(0)
                .path("transcript");
            return transcriptNode.asText();
        } catch (JsonProcessingException thrown) {
            throw new RuntimeException(thrown);
        }
    }

}
