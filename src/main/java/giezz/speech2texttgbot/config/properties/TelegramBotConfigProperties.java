package giezz.speech2texttgbot.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "telegram.bot")
@Getter
@Setter
public class TelegramBotConfigProperties {
    String username;
    String token;
}
