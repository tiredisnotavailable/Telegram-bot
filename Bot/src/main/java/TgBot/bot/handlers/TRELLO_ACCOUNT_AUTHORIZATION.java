package TgBot.bot.handlers;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import TgBot.bot.InputMessageHandler;
import TgBot.botstate.BotState;
import TgBot.user.UserData;
import TgBot.user.UserProfileData;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TRELLO_ACCOUNT_AUTHORIZATION implements InputMessageHandler {

    UserData userData;
    UserProfileData userProfileData;

    public TRELLO_ACCOUNT_AUTHORIZATION(UserData userData) {
        this.userData = userData;
    }

    @Override
    public SendMessage handle(Message message) {

        userProfileData = userData.getUserProfileData(message.getFrom().getId());
        log.info("User data: {}, {}", userData.getUserBotState(message.getFrom().getId()),
                userData.getUserProfileData(message.getFrom().getId()));
        if (userProfileData == null) {
            userProfileData = new UserProfileData(message.getText());
            userProfileData.setNewTrelloAccount();
        } else if (userProfileData.getTrelloAccount() == null) {
            userProfileData.setNewTrelloAccount();
        }
        if (!userProfileData.getTrelloAccount().isAuthorized()) {
            return new SendMessage(message.getChatId(),
                    "Введите ключ с этого сайта: https://trello.com/1/authorize?expiration=1day&name=TaskTackBot&scope=read&response_type=token&key=0148b7f86dd8b73597c46ccc9f028269");
        }
        userProfileData.setChatId(message.getChatId());
        userData.setUserProfileData(message.getFrom().getId(), userProfileData);
        userData.setUsersCurrentBotState(message.getFrom().getId(), BotState.TRELLO_ACCOUNT);
        log.info("User data: {}, {}", userData.getUserBotState(message.getFrom().getId()),
                userData.getUserProfileData(message.getFrom().getId()));
        return new SendMessage(message.getChatId(),
                "Теперь вы можете подписываться на уведомления.\nДоступные команды:\n/commands - показать доступные команды\n/boards - показать ваши доски\n/organizations - показать ваши организации\n/sbdBoards - показать доски на которые вы подписаны\n/sbdOrganizations - показать организации на которые вы подписаны\n/sbBoard - Подписаться на доски\n/sbOrganization - Подписаться на организации\n/delBoard - Отписаться от доски\n/delOrganization - Отписаться от организации\n");
    }

    @Override
    public BotState getHandlerName() {
        return BotState.TRELLO_ACCOUNT_AUTHORIZATION;
    }

}