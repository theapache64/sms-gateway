package com.theah64.sg.api_server.database.tables;

import com.theah64.sg.api_server.database.Connection;
import com.theah64.sg.api_server.models.SMSRequest;
import com.theah64.sg.api_server.utils.Log;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by theapache64 on 29/11/16,9:57 AM.
 */
public class SMSRequests extends BaseTable<SMSRequest> {
    public static final String COLUMN_MESSAGE = "message";
    private static final SMSRequests instance = new SMSRequests();

    private static final String X = SMSRequest.class.getSimpleName();
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_TOTAL_PARTS = "total_parts";

    private SMSRequests() {
        super("sms_requests");
    }

    public static SMSRequests getInstance() {
        return instance;
    }

    @Override
    public String addv3(SMSRequest request) throws InsertFailedException {

        Log.d(X, "New sms request: " + request);

        String requestId = null;
        final String query = "INSERT INTO sms_requests (message,server_id,user_id,total_parts) VALUES (?,?,?,?);";
        final java.sql.Connection con = Connection.getConnection();
        try {
            final PreparedStatement ps = con.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, request.getMessage());
            ps.setString(2, request.getServerId());
            ps.setString(3, request.getUserId());
            ps.setInt(4, request.getTotalParts());
            ps.executeUpdate();
            final ResultSet rs = ps.getGeneratedKeys();
            if (rs.first()) {
                requestId = rs.getString(1);
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

        if (requestId == null) {
            throw new InsertFailedException("Failed to add new sms request");
        }

        return requestId;
    }

    @Override
    public SMSRequest get(String column1, String value1, String column2, String value2) {
        SMSRequest smsRequest = null;
        final String query = String.format("SELECT message,total_parts,created_at FROM sms_requests WHERE %s = ? AND %s = ? LIMIT 1", column1, column2);
        final java.sql.Connection con = Connection.getConnection();
        try {
            final PreparedStatement ps = con.prepareStatement(query);

            ps.setString(1, value1);
            ps.setString(2, value2);

            final ResultSet rs = ps.executeQuery();

            if (rs.first()) {
                final String message = rs.getString(COLUMN_MESSAGE);
                final int totalParts = rs.getInt(COLUMN_TOTAL_PARTS);
                smsRequest = new SMSRequest(message, null, null, totalParts);
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
        return smsRequest;
    }
}
