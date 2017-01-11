package com.theah64.sg.api_server.models;

/**
 * Created by theapache64 on 29/11/16,9:58 AM.
 */
public class SMSRequest {
    private final String message, serverId, userId;
    private final int totalParts;

    public SMSRequest(String message, String serverId, String userId, int totalParts) {
        this.message = message;
        this.serverId = serverId;
        this.userId = userId;
        this.totalParts = totalParts;
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

    public int getTotalParts() {
        return totalParts;
    }
}
