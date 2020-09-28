package com.swack.transport.fcm;

/**
 * Created by Abhi on 13 Nov 2017 013.
 */

public class NotificationVO {
    private String title;
    private String message;
    private String iconUrl;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

}
