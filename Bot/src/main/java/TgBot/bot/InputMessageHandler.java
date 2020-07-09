package TgBot.bot;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import TgBot.botstate.BotState;

public interface InputMessageHandler {
    SendMessage handle(Message message);

    BotState getHandlerName();
}