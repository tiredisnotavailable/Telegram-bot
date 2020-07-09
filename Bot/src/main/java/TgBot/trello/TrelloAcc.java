package TgBot.trello;

import java.util.ArrayList;
import java.util.List;

import org.trello4j.Trello;
import org.trello4j.TrelloImpl;
import org.trello4j.model.Board;
import org.trello4j.model.Member;
import org.trello4j.model.Notification;
import org.trello4j.model.Organization;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j

public class TrelloAcc {

    Member user;
    List<String> notifications;
    private String trelloAccountName;
    private Trello trelloApi;
    private String trelloApiKey = "0148b7f86dd8b73597c46ccc9f028269";
    private String trelloAccessToken;
    private boolean isAuthorized = false;
    private List<String> subscribedBoards;
    private List<String> subscribedOrganizations;

    public TrelloAcc(String trelloAccessToken) {
        this.setTrelloAccessToken(trelloAccessToken);
        trelloApi = new TrelloImpl(trelloApiKey, trelloAccessToken);
        user = trelloApi.getMemberByToken(trelloAccessToken);
        notifications = new ArrayList<>();
        if (user != null) {
            isAuthorized = true;
            trelloAccountName = user.getFullName();
            for (Notification notification : trelloApi.getNotificationsByMember(user.getId())) {
                notifications.add(notification.getId());
            }
        }
        subscribedBoards = new ArrayList<>();
        subscribedOrganizations = new ArrayList<>();
        log.info("User: {}, TrelloApi: {}, TrelloAccountName: {}", user, trelloApi, trelloAccountName);
    }

    public List<Board> getBoards() {
        List<Board> boards = new ArrayList<Board>();
        String userId = user.getId();
        boards = trelloApi.getBoardsByMember(userId);
        return boards;
    }

    public List<Board> subBoards() {
        List<Board> boards = new ArrayList<>();
        if (subscribedBoards != null) {
            for (String board : subscribedBoards) {
                boards.add(trelloApi.getBoard(board));
            }
            return boards;
        } else {
            return null;
        }

    }

    public List<Organization> subOrganizations() {
        List<Organization> organizations = new ArrayList<>();
        if (subscribedBoards != null) {
            for (String organization : subscribedOrganizations) {
                organizations.add(trelloApi.getOrganization(organization));
            }
            return organizations;
        } else {
            return null;
        }

    }

    public Board findBoard(String text) {
        List<Board> boards = getBoards();
        for (Board board : boards) {
            if (board.getName().equals(text)) {
                return board;
            }
        }
        return null;
    }

    public List<Organization> getOrganizations() {
        List<Organization> organizations = new ArrayList<Organization>();
        String userId = user.getId();
        organizations = trelloApi.getOrganizationsByMember(userId);
        return organizations;
    }

    public void addBoardToSubscribtion(Board board) {
        subscribedBoards.add(board.getId());
    }

    public Organization findOrganization(String text) {
        List<Organization> organizations = getOrganizations();
        for (Organization organization : organizations) {
            if (organization.getName().equals(text)) {
                return organization;
            }
        }
        return null;
    }

    public void addOrganizationToSubscribtion(Organization organization) {
        subscribedOrganizations.add(organization.getId());
    }

    public Board findSubscribedBoard(String text) {
        for (String boardId : subscribedBoards) {
            if (trelloApi.getBoard(boardId).getName().equals(text)) {
                return trelloApi.getBoard(boardId);
            }
        }
        return null;
    }

    public void deleteBoard(Board board) {
        subscribedBoards.remove(board.getId());
    }

    public Organization findSubscribedOrganization(String text) {
        for (String organizationId : subscribedOrganizations) {
            if (trelloApi.getOrganization(organizationId).getName().equals(text)) {
                return trelloApi.getOrganization(organizationId);
            }
        }
        return null;
    }

    public void deleteOrganization(Organization organization) {
        subscribedOrganizations.remove(organization.getId());
    }

    public String AFK() {
        List<Notification> newNotifications = getUnreadNotifications();
        String text = "";
        if (newNotifications.size() > 0) {
            for (Notification notification : newNotifications) {
                if (subscribedBoards.contains(notification.getData().getBoard().getId())
                        || subscribedOrganizations.contains(
                                trelloApi.getOrganizationByBoard(notification.getData().getBoard().getId()).getId())) {
                    if (notification.getType().equals("mentionedOnCard")) {
                        text = text + "Доска: " + notification.getData().getBoard().getName() + "\n" + "Участник: "
                                + notification.getMemberCreator().getFullName()
                                + " упомянул вас в комментариях с текстом:\n" + notification.getData().getText() + "\n";
                    } else if (notification.getType().equals("addedToCard")) {
                        text = text + "Доска: " + notification.getData().getBoard().getName() + "\n" + "Участник: "
                                + notification.getMemberCreator().getFullName() + " добавил вас к карточке:\n"
                                + notification.getData().getCard().getName() + "\n";
                    } else if (notification.getType().equals("removedFromCard")) {
                        text = text + "Доска: " + notification.getData().getBoard().getName() + "\n" + "Участник: "
                                + notification.getMemberCreator().getFullName()
                                + " удалил вас из участников карточки:\n" + notification.getData().getCard().getName()
                                + "\n";
                    } else if (notification.getType().equals("changeCard")) {
                        text = text + "Доска: " + notification.getData().getBoard().getName() + "\n" + "Участник: "
                                + notification.getMemberCreator().getFullName() + "\n" + "Переместил карточку: "
                                + notification.getData().getCard().getName() + "\n";
                    } else {
                        text = text + "Доска: " + notification.getData().getBoard().getName() + "\n" + "Участник: "
                                + notification.getMemberCreator().getFullName() + "\n" + notification.getType() + "\n";
                    }

                }
            }
        }
        return text;
    }

    private List<Notification> getUnreadNotifications() {
        List<Notification> allNotifications;
        List<Notification> unreadNotifications = new ArrayList<>();
        allNotifications = trelloApi.getNotificationsByMember(user.getId());
        for (Notification notification : allNotifications) {
            if (!notifications.contains(notification.getId())) {
                unreadNotifications.add(notification);
                notifications.add(notification.getId());
            }
        }

        return unreadNotifications;
    }

    public String showUnsubBoards() {
        return null;
    }

}
