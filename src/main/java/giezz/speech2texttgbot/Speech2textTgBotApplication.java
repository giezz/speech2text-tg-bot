package giezz.speech2texttgbot;

import giezz.speech2texttgbot.config.properties.DeepgramConfigProperties;
import giezz.speech2texttgbot.config.properties.TelegramBotConfigProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({DeepgramConfigProperties.class, TelegramBotConfigProperties.class})
public class Speech2textTgBotApplication {
    public static void main(String[] args) {
        SpringApplication.run(Speech2textTgBotApplication.class, args);
    }
}
