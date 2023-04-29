package mys.serone.mystical.playerInfoSystem;

import java.util.List;

/**
 * Class for setters and getters for Player Information
 */
public class PlayerInfo {

    private List<String> userRankList;
    private List<String> userAdditionalPermission;

    /**
     * @return userRankList : Player Info rank list set by OnFirstJoinListener Event
     * @see mys.serone.mystical.listeners.OnFirstJoinListener
     */
    public List<String> getUserRankList() { return userRankList; }

    /**
     * @return userAdditionalPermission : Player Info additional permission set by OnFirstJoinListener Event
     * @see mys.serone.mystical.listeners.OnFirstJoinListener
     */
    public List<String> getUserAdditionalPermission() { return userAdditionalPermission; }

    /**
     * @param userRankList : To set the user rank list that is saved in the player_info.yml
     */
    public void setUserRankList(List<String> userRankList) {
        this.userRankList = userRankList;
    }

    /**
     * @param userAdditionalPermission : To set the user additional permission that is saved in the player_info.yml
     */
    public void setUserAdditionalPermission(List<String> userAdditionalPermission) { this.userAdditionalPermission = userAdditionalPermission; }
}

