package TgBot.appconfig;

import lombok.Getter;
import lombok.Setter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.ApiContext;

import TgBot.Bot;

import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "telegrambot")
public class BotConfig {

    @Bean
    public Bot bot() {
        DefaultBotOptions options = ApiContext.getInstance(DefaultBotOptions.class);
        options.setProxyHost(proxyHost);
        options.setProxyPort(proxyPort);
        options.setProxyType(proxyType);

        Bot bot = new Bot(options);
        bot.setBotToken(botToken);
        bot.setBotUserName(botUserName);
        bot.setWebHookPath(webHookPath);
        return bot;
    }

    private String webHookPath;
    private String botUserName;
    private String botToken;
    private DefaultBotOptions.ProxyType proxyType;
    private String proxyHost;
    private int proxyPort;
}