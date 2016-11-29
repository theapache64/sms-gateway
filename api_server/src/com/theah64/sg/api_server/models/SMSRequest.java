package com.theah64.sg.api_server.models;

/**
 * Created by theapache64 on 29/11/16,9:58 AM.
 */
public class SMSRequest {
    private final String message, serverId, userId;

    public SMSRequest(String message, String serverId, String userId) {
        this.message = message;
        this.serverId = serverId;
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public String getServerId() {
        return serverId;
    }

    public String getUserId() {
        return userId;
    }
}
