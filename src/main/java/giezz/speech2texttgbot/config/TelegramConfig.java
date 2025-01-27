package giezz.speech2texttgbot.config;

import giezz.speech2texttgbot.telegram.Speech2TextBot;
import lombok.extern.slf4j.Slf4j;
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
    public BotSession speech2TextBotSession(Speech2TextBot bot) {
        String botUsername = bot.getBotUsername();
        log.info("Initializing bot '{}'", botUsername);
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            BotSession botSession = telegramBotsApi.registerBot(bot);
            log.info("Bot '{}' is ready", botUsername);
            return botSession;
        } catch (TelegramApiException e) {
            throw new BeanCreationException("Failed to create the bot", e);
        }
    }

}
