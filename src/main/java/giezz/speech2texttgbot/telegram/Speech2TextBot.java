package giezz.speech2texttgbot.telegram;

import giezz.speech2texttgbot.api.DeepgramService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.Voice;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.nio.file.Path;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
@Slf4j
public class Speech2TextBot extends TelegramLongPollingBot {
    BotEnvironments botEnvironments;
    DeepgramService deepgramService;

    @Override
    public String getBotUsername() {
        return botEnvironments.getBotUsername();
    }

    @Override
    public String getBotToken() {
        return botEnvironments.getBotToken();
    }

    @Override
    public void onUpdateReceived(final Update update) {
        val message = update.getMessage();
        if (!update.hasMessage() || !message.hasVoice()) {
            return;
        }
        val voice = message.getVoice();
        log.debug("Voice: {}", voice);
        sendText(
            message.getFrom().getId(),
            deepgramService.getTranscription(downloadVoiceFile(voice)));
    }

    private Path downloadVoiceFile(final Voice voice) {
        val getFile = new GetFile();
        getFile.setFileId(voice.getFileId());
        try {
            val chatFilePath = execute(getFile).getFilePath();
            return downloadFile(chatFilePath).toPath();
        } catch (TelegramApiException thrown) {
            throw new RuntimeException(thrown);
        }
    }

    private void sendText(final Long id, final String message) {
        val sm = SendMessage.builder()
            .chatId(id.toString())
            .text(message)
            .build();
        try {
            execute(sm);
        } catch (TelegramApiException thrown) {
            throw new RuntimeException(thrown);
        }
    }

}
