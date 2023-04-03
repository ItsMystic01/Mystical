package mys.serone.mystical.playerInfoSystem;

import java.util.List;

public class PlayerInfo {

    private Double userCoins;
    private List<String> userRankList;
    private List<String> userAdditionalPermission;

    public Double getUserCoins() { return userCoins; }
    public List<String> getUserRankList() { return userRankList; }
    public List<String> getUserAdditionalPermission() { return userAdditionalPermission; }

    public void setUserCoins(Double userCoins) { this.userCoins = userCoins; }
    public void setUserRankList(List<String> userRankList) {
        this.userRankList = userRankList;
    }
    public void setUserAdditionalPermission(List<String> userAdditionalPermission) { this.userAdditionalPermission = userAdditionalPermission; }
}

