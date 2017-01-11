package com.theah64.smsgatewayserver.models;

/**
 * Created by theapache64 on 9/1/17.
 */

public class SMSStatus {

    private final String id, recipientId, status,reason;
    private final long occurredAt;

    public SMSStatus(String id, String recipientId, String status, String reason, long occurredAt) {
        this.id = id;
        this.recipientId = recipientId;
        this.status = status;
        this.reason = reason;
        this.occurredAt = occurredAt;
    }

    public String getReason() {
        return reason;
    }

    public String getId() {
        return id;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public String getStatus() {
        return status;
    }

    public long getOccurredAt() {
        return occurredAt;
    }
}
