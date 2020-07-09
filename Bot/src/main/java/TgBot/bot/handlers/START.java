package TgBot.bot.handlers;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import TgBot.bot.InputMessageHandler;
import TgBot.botstate.BotState;
import TgBot.user.UserData;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class START implements InputMessageHandler {

    private UserData userData;

    public START(UserData userData) {
        this.userData = userData;
    }

    @Override
    public SendMessage handle(Message message) {

        return processInputMessage(message);
    }

    private SendMessage processInputMessage(Message message) {
        Long chatId = message.getChatId();
        log.info("userProfileData: {}", userData.getUserProfileData(message.getFrom().getId()));
        userData.setUsersCurrentBotState(message.getFrom().getId(), BotState.TRELLO_ACCOUNT_AUTHORIZATION);
        return new SendMessage(chatId,
                "Для входа в аккаунт перейдите по ссылке: https://trello.com/1/authorize?expiration=1day&name=TaskTackBot&scope=read&response_type=token&key=0148b7f86dd8b73597c46ccc9f028269");
    }

    @Override
    public BotState getHandlerName() {

        return BotState.START;
    }

}