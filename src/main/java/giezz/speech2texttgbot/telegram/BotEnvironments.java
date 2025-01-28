package giezz.speech2texttgbot.telegram;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class BotEnvironments {
    @Value("${telegram.bot.username}")
    String botUsername;

    @Value("${telegram.bot.token}")
    String botToken;

}
