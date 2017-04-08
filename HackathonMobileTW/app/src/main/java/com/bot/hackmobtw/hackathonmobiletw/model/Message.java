package com.bot.hackmobtw.hackathonmobiletw.model;

/**
 * Created by fjmartins on 4/8/2017.
 */

public class Message {

    private String username, content;
    private boolean isSelf;

    public Message() {
    }

    public Message(String username, String content, boolean isSelf) {
        this.username = username;
        this.content = content;
        this.isSelf = isSelf;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String fromName) {
        this.username = fromName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isSelf() {
        return isSelf;
    }

    public void setSelf(boolean isSelf) {
        this.isSelf = isSelf;
    }
}
