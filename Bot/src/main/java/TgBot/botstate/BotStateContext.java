package TgBot.botstate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import TgBot.bot.InputMessageHandler;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class BotStateContext {

    private Map<BotState, InputMessageHandler> messageHandlers = new HashMap<>();

    public BotStateContext(List<InputMessageHandler> messageHandlers) {
        messageHandlers.forEach(handler -> this.messageHandlers.put(handler.getHandlerName(), handler));
    }

    public SendMessage processInputMessage(BotState currentState, Message message) {
        InputMessageHandler currentMessageHandler = findMessageHandler(currentState);
        if (currentMessageHandler == null) {
            log.info("------------{}-----------", currentState);
            log.info("------------NULL------------");
            return new SendMessage(message.getChatId(), "");
        }
        return currentMessageHandler.handle(message);
    }

    private InputMessageHandler findMessageHandler(BotState currentState) {
        if (isTrelloAccountState(currentState)) {
            log.info("currentState at BotState Context: {}", currentState);
            return messageHandlers.get(BotState.TRELLO_ACCOUNT);
        }

        return messageHandlers.get(currentState);
    }

    private boolean isTrelloAccountState(BotState currentState) {
        switch (currentState) {
            case TRELLO_ACCOUNT:
            case ASK_BOARDS_SUBSCRIBTION:
            case ASK_ORGANIZATION_SUBSCRIBTION:
            case SHOW_BOARDS:
            case SHOW_ORGANIZATIONS:
            case SHOW_COMMANDS:
            case SHOW_SUBSCRIBED_BOARDS:
            case SHOW_SUBSCRIBED_ORGANIZATIONS:
            case DELETE_BOARD:
            case DELETE_ORGANIZATION:
            case DELETE_ORGANIZATION_INFORMATION:
            case DELETE_BOARD_INFORMATION:
            case SUBSCRIBE_TO_BOARD:
            case SUBSCRIBE_TO_ORGANIZATION:
            case AFK:
                return true;
            default:
                return false;
        }
    }

}