package com.madrat.spaceshooter.utils.api.resourcereprs;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class User implements Comparable<User> {

    private String clientuuid;
    private String serveruuid;
    private String username;
    private int score;

    public User() {
    }

    public User(String serverUUID, String clientUUID, String userName, int score) {
        this.serveruuid = serverUUID;
        this.clientuuid = clientUUID;
        this.username = userName;
        this.score = score;
    }

    public boolean isValid() {
        return this.getServeruuid() != null
                && this.getClientuuid() != null
                && this.getUsername() != null
                && this.getScore() != 0;
    }

    public String getServeruuid() {
        return serveruuid;
    }

    public String getClientuuid() {
        return clientuuid;
    }

    public String getUsername() {
        return username;
    }

    public int getScore() {
        return score;
    }

    public void setServeruuid(String serveruuid) {
        this.serveruuid = serveruuid;
    }

    public void setClientuuid(String clientuuid) {
        this.clientuuid = clientuuid;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public int compareTo(User user) {
        return Integer.compare(this.score, user.score);
    }

    @Override
    public String toString() {
        String json = "";
        json += "{" + "\n" +
                "\t\"clientuuid\": \"" + clientuuid + "\"\n" +
                "\t\"serveruuid\": \"" + serveruuid + "\"\n" +
                "\t\"username\": \"" + username + "\"\n" +
                "\t\"score\": " + score + "\n" +
                "}";
        return json;
    }
}
