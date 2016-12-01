package com.theah64.sg.api_server.database.tables;

import com.theah64.sg.api_server.database.Connection;
import com.theah64.sg.api_server.models.SMSRequest;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by theapache64 on 29/11/16,9:57 AM.
 */
public class SMSRequests extends BaseTable<SMSRequest> {
    public static final String COLUMN_MESSAGE = "message";
    private static final SMSRequests instance = new SMSRequests();

    private SMSRequests() {
        super("sms_requests");
    }

    public static SMSRequests getInstance() {
        return instance;
    }

    @Override
    public String addv3(SMSRequest request) throws InsertFailedException {
        String requestId = null;
        final String query = "INSERT INTO sms_requests (message,server_id,user_id) VALUES (?,?,?);";
        final java.sql.Connection con = Connection.getConnection();
        try {
            final PreparedStatement ps = con.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, request.getMessage());
            ps.setString(2, request.getServerId());
            ps.setString(3, request.getUserId());
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
}
