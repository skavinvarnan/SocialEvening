package com.kavin.socialevening.server.dto;

import java.util.List;

/**
 * Copyright 2015 (C) Virtual Applets
 * Created on : 01/11/15
 * Author     : Kavin Varnan
 */
public class FriendsParseCloud {
    private List<String> friendList;
    private String teamName;

    public List<String> getFriendList() {
        return friendList;
    }

    public void setFriendList(List<String> friendList) {
        this.friendList = friendList;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
}
