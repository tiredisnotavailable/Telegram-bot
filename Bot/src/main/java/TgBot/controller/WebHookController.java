package TgBot.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import TgBot.Bot;

@RestController
public class WebHookController {

    public WebHookController(Bot bot) {
        this.bot = bot;
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public BotApiMethod<?> onUpdateRecieved(@RequestBody Update update) {
        return bot.onWebhookUpdateReceived(update);
    }

    private final Bot bot;
}