package giezz.speech2texttgbot.exception;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class UncheckedTelegramApiException extends RuntimeException {
    public UncheckedTelegramApiException(String message, TelegramApiException cause) {
        super(message, cause);
    }
}
