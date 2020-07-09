package TgBot.bot;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import TgBot.botstate.BotState;
import TgBot.botstate.BotStateContext;
import TgBot.user.UserData;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MessageHandler {

    public MessageHandler(BotStateContext botStateContext, UserData userData) {
        this.botStateContext = botStateContext;
        this.userData = userData;
    }

    public SendMessage handleUpdate(Update update) {
        SendMessage replyMessage = null;
        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            log.info("New message from User: {}, chatId: {}, with text: {}", message.getFrom().getUserName(),
                    message.getChatId(), message.getText());
            replyMessage = handleInputMessage(message);
        }

        return replyMessage;
    }

    private SendMessage handleInputMessage(Message message) {
        String inputMessage = message.getText();
        Integer userId = message.getFrom().getId();
        BotState botState;
        SendMessage replyMessage;

        switch (inputMessage) {
            case "/start":
                botState = BotState.START;
                break;
            case "Помощь.":
                botState = BotState.SHOW_HELP_MENU;
                break;
            default:
                botState = userData.getUserBotState(userId);
                log.info("botstate at MessageHandler: {}", userData.getUserBotState(userId));
                break;
        }

        userData.setUsersCurrentBotState(userId, botState);

        replyMessage = botStateContext.processInputMessage(botState, message);

        return replyMessage;
    }

    private BotStateContext botStateContext;
    private UserData userData;
}