package TgBot.bot.handlers;

import java.util.List;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.trello4j.model.Board;
import org.trello4j.model.Organization;

import TgBot.bot.InputMessageHandler;
import TgBot.botstate.BotState;
import TgBot.user.UserData;
import TgBot.user.UserProfileData;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class TRELLO_ACCOUNT implements InputMessageHandler {

	private UserData userData;
	private UserProfileData userProfileData;

	public TRELLO_ACCOUNT(UserData userData) {
		this.userData = userData;
	}

	@Override
	public SendMessage handle(Message message) {
		userProfileData = userData.getUserProfileData(message.getFrom().getId());
		BotState currentBotState = getUserCurrentBotState(message);
		log.info("currentBotState: {}", currentBotState);
		if (currentBotState == BotState.SHOW_COMMANDS) {
			userData.setUsersCurrentBotState(message.getFrom().getId(), BotState.TRELLO_ACCOUNT);
			return SHOW_COMMANDS(message);
		} else if (currentBotState == BotState.SHOW_BOARDS) {
			userData.setUsersCurrentBotState(message.getFrom().getId(), BotState.TRELLO_ACCOUNT);
			return SHOW_BOARDS(message);
		} else if (currentBotState == BotState.SHOW_ORGANIZATIONS) {
			userData.setUsersCurrentBotState(message.getFrom().getId(), BotState.TRELLO_ACCOUNT);
			return SHOW_ORGANIZATIONS(message);
		} else if (currentBotState == BotState.SHOW_SUBSCRIBED_BOARDS) {
			userData.setUsersCurrentBotState(message.getFrom().getId(), BotState.TRELLO_ACCOUNT);
			return SHOW_SUBSCRIBED_BOARDS(message);
		} else if (currentBotState == BotState.SHOW_SUBSCRIBED_ORGANIZATIONS) {
			userData.setUsersCurrentBotState(message.getFrom().getId(), BotState.TRELLO_ACCOUNT);
			return SHOW_SUBSCRIBED_ORGANIZATIONS(message);
		} else if (currentBotState == BotState.ASK_BOARDS_SUBSCRIBTION) {
			userData.setUsersCurrentBotState(message.getFrom().getId(), BotState.SUBSCRIBE_TO_BOARD);
			return ASK_BOARDS_SUBSCRIBTION(message);
		} else if (currentBotState == BotState.SUBSCRIBE_TO_BOARD) {
			return SUBSCRIBE_TO_BOARD(message);
		} else if (currentBotState == BotState.ASK_ORGANIZATION_SUBSCRIBTION) {
			userData.setUsersCurrentBotState(message.getFrom().getId(), BotState.SUBSCRIBE_TO_ORGANIZATION);
			return ASK_ORGANIZATIONS_SUBSCRIBTION(message);
		} else if (currentBotState == BotState.SUBSCRIBE_TO_ORGANIZATION) {
			return SUBSCRIBE_TO_ORGANIZATION(message);
		} else if (currentBotState == BotState.DELETE_BOARD_INFORMATION) {
			userData.setUsersCurrentBotState(message.getFrom().getId(), BotState.DELETE_BOARD);
			return DELETE_BOARD_INFORMATION(message);
		} else if (currentBotState == BotState.DELETE_BOARD) {
			return DELETE_BOARD(message);
		} else if (currentBotState == BotState.DELETE_ORGANIZATION_INFORMATION) {
			userData.setUsersCurrentBotState(message.getFrom().getId(), BotState.DELETE_ORGANIZATION);
			return DELETE_ORGANIZATION_INFORMATION(message);
		} else if (currentBotState == BotState.DELETE_ORGANIZATION) {
			return DELETE_ORGANIZATION(message);
		}

		return SHOW_COMMANDS(message);

	}

	private SendMessage SHOW_COMMANDS(Message message) {
		// Показать доступные команды
		String replyToUserText = "Доступные команды:\n/commands - показать доступные команды\n/boards - показать ваши доски\n/organizations - показать ваши организации\n/sbdBoards - показать доски на которые вы подписаны\n/sbdOrganizations - показать организации на которые вы подписаны\n/sbBoard - Подписаться на доски\n/sbOrganization - Подписаться на организации\n/delBoard - Отписаться от доски\n/delOrganization - Отписаться от организации\n";
		return new SendMessage(message.getChatId(), replyToUserText);
	}

	private SendMessage SHOW_BOARDS(Message message) {
		// Показать доски usera
		List<Board> boards = userProfileData.getMyBoards();
		String replyToUserText = "";
		SendMessage replyToUser = null;
		for (Board board : boards) {
			replyToUserText = replyToUserText + board.getName() + "\n";
		}
		replyToUser = new SendMessage(message.getChatId(), replyToUserText);

		return replyToUser;
	}

	private SendMessage SHOW_ORGANIZATIONS(Message message) {
		// Показать организации usera
		List<Organization> organizations = userProfileData.getMyOrganizations();
		String replyToUserText = "";
		SendMessage replyToUser = null;
		for (Organization organization : organizations) {
			replyToUserText = replyToUserText + organization.getName() + "\n";
		}
		replyToUser = new SendMessage(message.getChatId(), replyToUserText);
		return replyToUser;
	}

	private SendMessage SHOW_SUBSCRIBED_BOARDS(Message message) {
		// Показать доски на которые подписан user
		List<Board> sbdBoards = userProfileData.getSubscribedBoards();
		if (sbdBoards.size() == 0) {
			return new SendMessage(message.getChatId(), "У вас нет подписок");
		}
		String replyToUserText = "";
		for (Board board : sbdBoards) {
			replyToUserText = replyToUserText + board.getName() + "\n";
		}

		return new SendMessage(message.getChatId(), replyToUserText);
	}

	private SendMessage SHOW_SUBSCRIBED_ORGANIZATIONS(Message message) {
		// Показать организации на которые подписан user

		List<Organization> sbdOrganizations = userProfileData.getSubscribedOrganizations();
		if (sbdOrganizations.size() == 0) {
			return new SendMessage(message.getChatId(), "У вас нет подписок");
		}
		String replyToUserText = "";
		for (Organization organization : sbdOrganizations) {
			replyToUserText = replyToUserText + organization.getName() + "\n";
		}
		return new SendMessage(message.getChatId(), replyToUserText);
	}

	private SendMessage ASK_BOARDS_SUBSCRIBTION(Message message) {

		log.info("userBotState at ask board subscribtion: {}", userData.getUserBotState(message.getFrom().getId()));

		return new SendMessage(message.getChatId(),
				"Введите имя доски, чтобы подписаться или напишите /exit чтобы закончить. Доступные доски:\n"
						+ SHOW_BOARDS(message).getText());
	}

	private SendMessage SUBSCRIBE_TO_BOARD(Message message) {
		// Подписаться на доску
		if (message.getText().equals("/exit")) {
			userData.setUsersCurrentBotState(message.getFrom().getId(), BotState.TRELLO_ACCOUNT);
			return new SendMessage(message.getChatId(), "");
		}
		Board board = userProfileData.findBoard(message.getText());
		if (board != null) {
			userProfileData.addBoardToSubscribtion(board);
			userData.setUserProfileData(message.getFrom().getId(), userProfileData);
			return new SendMessage(message.getChatId(), "Доска добавлена");
		}
		return new SendMessage(message.getChatId(), "Доска не найдена");
	}

	private SendMessage ASK_ORGANIZATIONS_SUBSCRIBTION(Message message) {

		log.info("userBotState at ask organization subscribtion: {}",
				userData.getUserBotState(message.getFrom().getId()));
		return new SendMessage(message.getChatId(),
				"Введите имя организации, чтобы подписаться или напишите /exit чтобы закончить. Доступные организации:\n"
						+ SHOW_ORGANIZATIONS(message).getText());
	}

	private SendMessage SUBSCRIBE_TO_ORGANIZATION(Message message) {
		// Подписаться на организацию
		if (message.getText().equals("/exit")) {
			userData.setUsersCurrentBotState(message.getFrom().getId(), BotState.TRELLO_ACCOUNT);
			return new SendMessage(message.getChatId(), "");
		}
		Organization organization = userProfileData.findOrganization(message.getText());
		if (organization != null) {
			userProfileData.addOrganizationToSubscribtion(organization);
			userData.setUserProfileData(message.getFrom().getId(), userProfileData);
			return new SendMessage(message.getChatId(), "Организация добавлена");
		}
		return new SendMessage(message.getChatId(), "Организация не найдена");
	}

	private SendMessage DELETE_BOARD_INFORMATION(Message message) {
		return new SendMessage(message.getChatId(),
				"Введите имя доски, чтобы удалить или напишите /exit чтобы закончить. Доступные доски для удаления:\n"
						+ SHOW_SUBSCRIBED_BOARDS(message).getText());
	}

	private SendMessage DELETE_BOARD(Message message) {
		// Удалить доску из подписок
		if (message.getText().equals("/exit")) {
			userData.setUsersCurrentBotState(message.getFrom().getId(), BotState.TRELLO_ACCOUNT);
			return new SendMessage(message.getChatId(), "");
		}
		Board board = userProfileData.findSubscribedBoard(message.getText());
		if (board != null) {
			userProfileData.deleteBoard(board);
			userData.setUserProfileData(message.getFrom().getId(), userProfileData);
			return new SendMessage(message.getChatId(), "Доска удалена");
		}
		return new SendMessage(message.getChatId(), "Доска не найдена");
	}

	private SendMessage DELETE_ORGANIZATION_INFORMATION(Message message) {
		return new SendMessage(message.getChatId(),
				"Введите имя организации, чтобы удалить или напишите /exit чтобы закончить. Доступные организации для удаления:\n"
						+ SHOW_SUBSCRIBED_ORGANIZATIONS(message).getText());
	}

	private SendMessage DELETE_ORGANIZATION(Message message) {
		// Удалить организацию из подписок
		if (message.getText().equals("/exit")) {
			userData.setUsersCurrentBotState(message.getFrom().getId(), BotState.TRELLO_ACCOUNT);
			return new SendMessage(message.getChatId(), "");
		}
		Organization organization = userProfileData.findSubscribedOrganization(message.getText());
		if (organization != null) {
			userProfileData.deleteOrganization(organization);
			userData.setUserProfileData(message.getFrom().getId(), userProfileData);
			return new SendMessage(message.getChatId(), "Организация удалена");
		}
		return new SendMessage(message.getChatId(), "Организация не найдена");
	}

	private BotState getUserCurrentBotState(Message message) {
		if (message.getText().equals("/boards")) {
			return BotState.SHOW_BOARDS;
		} else if (message.getText().equals("/organizations")) {
			return BotState.SHOW_ORGANIZATIONS;
		} else if (message.getText().equals("/sbdBoards")) {
			return BotState.SHOW_SUBSCRIBED_BOARDS;
		} else if (message.getText().equals("/sbdOrganizations")) {
			return BotState.SHOW_SUBSCRIBED_ORGANIZATIONS;
		} else if (message.getText().equals("/sbBoard")) {
			return BotState.ASK_BOARDS_SUBSCRIBTION;
		} else if (message.getText().equals("/sbOrganization")) {
			return BotState.ASK_ORGANIZATION_SUBSCRIBTION;
		} else if (message.getText().equals("/delBoard")) {
			return BotState.DELETE_BOARD_INFORMATION;
		} else if (message.getText().equals("/delOrganization")) {
			return BotState.DELETE_ORGANIZATION_INFORMATION;
		} else if (message.getText().equals("/commands")) {
			return BotState.SHOW_COMMANDS;
		}
		log.info("userData.userBotState: {}", userData.getUserBotState(message.getFrom().getId()));
		return userData.getUserBotState(message.getFrom().getId());
	}

	@Override
	public BotState getHandlerName() {
		return BotState.TRELLO_ACCOUNT;
	}

}