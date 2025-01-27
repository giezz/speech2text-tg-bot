package giezz.speech2texttgbot.telegram;

import giezz.speech2texttgbot.api.DeepgramService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.Voice;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
@RequiredArgsConstructor
@Slf4j
public class Speech2TextBot extends TelegramLongPollingBot {
    private final BotEnvironments botEnvironments;
    private final DeepgramService deepgramService;

    @Override
    public String getBotUsername() {
        return botEnvironments.getBotUsername();
    }

    @Override
    public String getBotToken() {
        return botEnvironments.getBotToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        System.out.println(update);
        if (!update.hasMessage() || !update.getMessage().hasVoice()) {
            return;
        }
        Path path = Path.of("voice.ogg");
        try {
            downloadVoiceFile(update.getMessage().getVoice(), path);
            String transcription = deepgramService.getTranscription(path);
            sendText(update.getMessage().getFrom().getId(), transcription);
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            sendText(update.getMessage().getFrom().getId(), "Произошла ошибка, попробуйте еще раз");
        } finally {
            deleteFile(path);
        }
    }

    private void downloadVoiceFile(Voice voice, Path localPath) {
        GetFile getFile = new GetFile();
        getFile.setFileId(voice.getFileId());
        try {
            String chatFilePath = execute(getFile).getFilePath();
            log.info("Downloading file: {}", chatFilePath);
            downloadFile(chatFilePath, localPath.toFile());
            log.info("File downloaded successfully");
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendText(Long id, String message) {
        SendMessage sm = SendMessage.builder()
                .chatId(id.toString())
                .text(message)
                .build();
        try {
            execute(sm);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void deleteFile(Path path) {
        try {
            Files.deleteIfExists(path);
            log.info("Deleted file: {}", path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
