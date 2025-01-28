package giezz.speech2texttgbot.config;

import giezz.speech2texttgbot.telegram.Speech2TextBot;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.BotSession;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
@Slf4j
public class TelegramConfig {
    @Bean
    public BotSession speech2TextBotSession(final Speech2TextBot bot) {
        val botUsername = bot.getBotUsername();
        log.info("Инициализация бота '{}'", botUsername);
        try {
            val botSession = new TelegramBotsApi(DefaultBotSession.class).registerBot(bot);
            log.info("Бот '{}' готов", botUsername);
            return botSession;
        } catch (TelegramApiException thrown) {
            throw new BeanCreationException("Ошибка при инициализации бота", thrown);
        }
    }

}
