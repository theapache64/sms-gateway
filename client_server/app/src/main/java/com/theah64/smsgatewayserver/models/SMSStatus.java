package com.theah64.smsgatewayserver.models;

/**
 * Created by theapache64 on 9/1/17.
 */

public class SMSStatus {
    private final String id,messageId, recipientId,status;
    private final long occurredAt;

    public SMSStatus(String id, String messageId, String recipientId, String status, long occurredAt) {
        this.id = id;
        this.messageId = messageId;
        this.recipientId = recipientId;
        this.status = status;
        this.occurredAt = occurredAt;
    }

    public String getId() {
        return id;
    }

    public String getMessageId() {
        return messageId;
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
