package com.theah64.smsgatewayserver.models;

/**
 * Created by theapache64 on 9/1/17.
 */

public class SMSStatus {

    public static final String STATUS_SENT_TO_RECIPIENT = "SENT_TO_RECIPIENT";
    public static final String STATUS_FAILED_TO_SEND_TO_RECIPIENT = "FAILED_TO_SEND_TO_RECIPIENT";
    public static final String REASON_GENERIC_FAILURE = "REASON_GENERIC_FAILURE";
    public static final String REASON_NO_SERVICE = "NO_SERVICE";
    public static final String REASON_NULL_PDU = "NULL_PDU";
    public static final String REASON_RADIO_OFF = "RADIO_OFF";
    public static final String STATUS_DELIVERED_TO_RECIPIENT = "DELIVERED_TO_RECIPIENT";
    public static final String STATUS_FAILED_TO_DELIVER_TO_RECIPIENT = "FAILED_TO_DELIVER_TO_RECIPIENT";

    private final String id;
    private final String recipientId;
    private String status;
    private String reason;
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

    public void setStatus(String status) {
        this.status = status;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
