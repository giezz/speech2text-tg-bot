package giezz.speech2texttgbot.telegram;

import giezz.speech2texttgbot.config.properties.TelegramBotConfigProperties;
import giezz.speech2texttgbot.deepgram.service.DeepgramService;
import giezz.speech2texttgbot.exception.UncheckedTelegramApiException;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.facilities.filedownloader.DownloadFileException;
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
    TelegramBotConfigProperties telegramConfig;
    DeepgramService deepgramService;

    @Override
    public String getBotUsername() {
        return telegramConfig.getUsername();
    }

    @Override
    public String getBotToken() {
        return telegramConfig.getToken();
    }

    @Override
    public void onUpdateReceived(final Update update) {
        if (!update.hasMessage() || !update.getMessage().hasVoice()) {
            return;
        }
        val message = update.getMessage();
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
            val file = downloadFile(execute(getFile).getFilePath());
            file.deleteOnExit();
            return file.toPath();
        } catch (TelegramApiException thrown) {
            throw new UncheckedTelegramApiException("Ошибка при скачивании файла", thrown);
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
            throw new UncheckedTelegramApiException("Ошибка при отправки сообщения", thrown);
        }
    }
}
