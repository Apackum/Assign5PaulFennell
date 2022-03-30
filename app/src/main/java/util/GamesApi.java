package util;

import android.app.Application;

public class GamesApi extends Application{
    private String username;
    private String userId;
    //singleton
    private static GamesApi instance;

    public static GamesApi getInstance() {
//This will always return a new instance
        if (instance == null)
            instance = new GamesApi();
        return instance;
    }

    public GamesApi(){

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
