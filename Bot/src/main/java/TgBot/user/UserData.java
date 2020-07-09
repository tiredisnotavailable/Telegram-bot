package TgBot.user;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import TgBot.bot.Bot;
import TgBot.botstate.BotState;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class UserData {

    private Bot bot;
    private Map<Integer, BotState> usersBotStates = new HashMap<>();
    private Map<Integer, UserProfileData> usersProfileData = new HashMap<>();

    public UserData(@Lazy Bot bot) {
        this.bot = bot;
    }

    public BotState getUserBotState(Integer userId) {
        BotState botState = usersBotStates.get(userId);
        log.info("botstate at USERDATA: {}, UserId: {}, usersBotStates size: {}", botState, userId,
                usersBotStates.size());
        if (botState == null) {
            botState = BotState.START;
        }
        return botState;
    }

    @Scheduled(fixedRate = 5000)
    public void afk() {
        usersProfileData.forEach((k, v) -> {
            v.AFK();
            if (!v.getNewNotification().equals("")) {
                log.info("USERDATA SCHEDULED TEXT: {}", v.getNewNotification());
                bot.SendMsg(v.getNewNotification(), v.getChatId());
                v.setNewNotification("");
            } else {
            }
        });
    }

    public void setUsersCurrentBotState(Integer userId, BotState botState) {
        usersBotStates.put(userId, botState);
    }

    public UserProfileData getUserProfileData(Integer userId) {
        UserProfileData userProfileData = usersProfileData.get(userId);
        return userProfileData;
    }

    public void setUserProfileData(Integer userId, UserProfileData profileData) {
        usersProfileData.put(userId, profileData);
    }

}