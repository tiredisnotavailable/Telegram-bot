package TgBot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.telegram.telegrambots.ApiContextInitializer;

@ComponentScan(basePackages = "TgBot.Bot")
@SpringBootApplication
public class Main {
    public static void main(String[] args) {

        SpringApplication.run(Main.class, args);

    }
}
