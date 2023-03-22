package mys.serone.mystical.playerInfoSystem;

import java.util.List;

public class PlayerInfo {

    private String playerUUID;
    private Double userCoins;
    private List<String> userRankList;

    public String getPlayerUUID() { return playerUUID; }
    public Double getUserCoins() { return userCoins; }
    public List<String> getUserRankList() { return userRankList; }

    public void setPlayerUUID(String playerUUID) { this.playerUUID = playerUUID; }
    public void setUserCoins(Double userCoins) { this.userCoins = userCoins; }
    public void setUserRankList(List<String> userRankList) {
        this.userRankList = userRankList;
    }
}

