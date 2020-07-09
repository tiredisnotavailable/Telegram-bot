package TgBot.appconfig;

import lombok.Getter;
import lombok.Setter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import TgBot.bot.Bot;
import TgBot.bot.MessageHandler;

import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "telegrambot")
public class BotConfig {

    @Bean
    public Bot bot(MessageHandler messageHandler) {
        Bot bot = new Bot(messageHandler);
        bot.setBotToken(botToken);
        bot.setBotUserName(botUserName);
        bot.setWebHookPath(webHookPath);
        return bot;
    }

    private String webHookPath;
    private String botUserName;
    private String botToken;
}