package com.theah64.sg.api_server.database.tables;

import com.theah64.sg.api_server.database.Connection;
import com.theah64.sg.api_server.models.SMSRequestStatus;
import com.theah64.sg.api_server.utils.Request;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by theapache64 on 29/11/16,5:19 PM.
 * recipient_id INT NOT NULL,
 * status ENUM('SENT','DELIVERED','FAILED') NOT NULL DEFAULT 'SENT',
 * occurred_at INT(11) NOT NULL ,
 */
public class SMSRequestStatuses extends BaseTable<SMSRequestStatus> {

    private static final SMSRequestStatuses instance = new SMSRequestStatuses();
    public static final String COLUMN_RECIPIENT_ID = "recipient_id";
    private static final String COLUMN_STATUS = "status";
    private static final String COLUMN_OCCURRED_AT = "occurred_at";

    private SMSRequestStatuses() {
        super("sms_request_statuses");
    }

    public static SMSRequestStatuses getInstance() {
        return instance;
    }

    public void add(final JSONArray jaStatuses) throws InsertFailedException, JSONException {

        final String query = "INSERT INTO sms_request_statuses (recipient_id, status, occurred_at) VALUES (?,?,?);";
        final java.sql.Connection con = Connection.getConnection();
        try {
            final PreparedStatement ps = con.prepareStatement(query);

            for (int i = 0; i < jaStatuses.length(); i++) {
                final JSONObject joStatus = jaStatuses.getJSONObject(i);

                final String recId = joStatus.getString(COLUMN_RECIPIENT_ID);
                final String status = joStatus.getString(COLUMN_STATUS);
                final String occurredAt = joStatus.getString(COLUMN_OCCURRED_AT);

                ps.setString(1, recId);
                ps.setString(2, status);
                ps.setString(3, occurredAt);

                if (ps.executeUpdate() != 1) {
                    throw new InsertFailedException("Failed to insert : " + joStatus);
                }
            }

            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    public JSONArray getStatuses(final String requestId, final String userId) throws JSONException, Request.RequestException {

        JSONArray jaStatuses = null;

        final String query = "SELECT r.recipient, sqs.status, sqs.occurred_at FROM sms_request_statuses sqs INNER JOIN recipients r ON r.id = sqs.recipient_id INNER JOIN sms_requests sr ON r.sms_request_id = sr.id WHERE sr.id = ? AND sr.user_id = ? GROUP BY r.id;";

        final java.sql.Connection con = Connection.getConnection();

        try {
            final PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, requestId);
            ps.setString(2, userId);

            final ResultSet rs = ps.executeQuery();

            if (rs.first()) {

                jaStatuses = new JSONArray();

                do {
                    final String recipient = rs.getString(Recipients.COLUMN_RECIPIENT);
                    final String status = rs.getString(COLUMN_STATUS);
                    final String occurredAt = rs.getString(COLUMN_OCCURRED_AT);

                    final JSONObject joStatus = new JSONObject();
                    joStatus.put(Recipients.COLUMN_RECIPIENT, recipient);
                    joStatus.put(COLUMN_STATUS, status);
                    joStatus.put(COLUMN_OCCURRED_AT, occurredAt);

                    jaStatuses.put(joStatus);

                } while (rs.next());

            }

            rs.close();
            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (jaStatuses == null) {
            throw new Request.RequestException("No status found");
        }

        return jaStatuses;
    }
}
