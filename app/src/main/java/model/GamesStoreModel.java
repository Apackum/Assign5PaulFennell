package model;

import com.google.firebase.Timestamp;

public class GamesStoreModel {

    private String gameTitle;
    private String gameDescription;
    private String imageUrl;
    private String userId;
    private Timestamp timeAdded;
    private String userName;

    public GamesStoreModel() {
    }

    public GamesStoreModel(String gameTitle, String gameDescription, String imageUrl, String userId, com.google.firebase.Timestamp timeAdded, String userName) {
        this.gameTitle = gameTitle;
        this.gameDescription = gameDescription;
        this.imageUrl = imageUrl;
        this.userId = userId;
        this.timeAdded = timeAdded;
        this.userName = userName;
    }

    public String getGameTitle() {
        return gameTitle;
    }

    public void setGameTitle(String gameTitle) {
        this.gameTitle = gameTitle;
    }

    public String getGameDescription() {
        return gameDescription;
    }

    public void setGameDescription(String gameDescription) {
        this.gameDescription = gameDescription;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Timestamp getTimeAdded() {
        return timeAdded;
    }

    public void setTimeAdded(Timestamp timeAdded) {
        this.timeAdded = timeAdded;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}