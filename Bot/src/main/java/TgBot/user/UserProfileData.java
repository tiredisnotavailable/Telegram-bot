package TgBot.user;

import java.util.List;

import org.trello4j.model.Board;
import org.trello4j.model.Organization;

import TgBot.trello.TrelloAcc;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
public class UserProfileData {

    private String trelloAccessToken;
    private TrelloAcc trelloAccount = null;
    private Long chatId;
    private String newNotification;

    public UserProfileData(String trelloAccessToken) {
        this.trelloAccessToken = trelloAccessToken;
    }

    public void setNewTrelloAccount() {

        log.info("trelloAccessToken: {}", trelloAccessToken);
        trelloAccount = new TrelloAcc(trelloAccessToken);
    }

    public List<Board> getMyBoards() {
        return trelloAccount.getBoards();
    }

    public Board findBoard(String text) {
        return trelloAccount.findBoard(text);
    }

    public List<Organization> getMyOrganizations() {
        return trelloAccount.getOrganizations();
    }

    public List<Board> getSubscribedBoards() {

        return trelloAccount.subBoards();
    }

    public List<Organization> getSubscribedOrganizations() {
        return trelloAccount.subOrganizations();
    }

    public void addBoardToSubscribtion(Board board) {
        trelloAccount.addBoardToSubscribtion(board);
    }

    public Organization findOrganization(String text) {
        return trelloAccount.findOrganization(text);
    }

    public void addOrganizationToSubscribtion(Organization organization) {
        trelloAccount.addOrganizationToSubscribtion(organization);
    }

    public Board findSubscribedBoard(String text) {
        return trelloAccount.findSubscribedBoard(text);
    }

    public void deleteBoard(Board board) {
        trelloAccount.deleteBoard(board);
    }

    public Organization findSubscribedOrganization(String text) {
        return trelloAccount.findSubscribedOrganization(text);
    }

    public void deleteOrganization(Organization organization) {
        trelloAccount.deleteOrganization(organization);
    }

    public void AFK() {
        newNotification = trelloAccount.AFK();
    }

}