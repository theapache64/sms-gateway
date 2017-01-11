package com.theah64.sg.api_server.database.tables;

import com.theah64.sg.api_server.database.Connection;
import com.theah64.sg.api_server.models.Recipient;
import com.theah64.sg.api_server.utils.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Created by theapache64 on 29/11/16,9:55 AM.
 */
public class Recipients extends BaseTable<Recipient> {

    public static final String TABLE_NAME = "recipients";
    public static final String COLUMN_RECIPIENT = "recipient";
    public static final String COLUMN_SMS_REQUEST_ID = "sms_request_id";
    private static final Recipients instance = new Recipients();


    private Recipients() {
        super(TABLE_NAME);
    }

    public static Recipients getInstance() {
        return instance;
    }

    public JSONArray add(String requestId, JSONArray jaRecipients) throws JSONException, InsertFailedException {

        JSONArray jaAdvRecipients = null;
        final StringBuilder queryBuilder = new StringBuilder("INSERT INTO recipients (sms_request_id,recipient) VALUES ");

        for (int i = 0; i < jaRecipients.length(); i++) {
            queryBuilder.append("(?,?),");
        }
        final String query = queryBuilder.substring(0, queryBuilder.length() - 1) + ";";

        final java.sql.Connection con = Connection.getConnection();
        try {
            final PreparedStatement ps = con.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            int x = 0;
            for (int i = 0; i < jaRecipients.length(); i++) {
                ps.setString(x + 1, requestId);
                ps.setString(x + 2, jaRecipients.getString(i));
                x = x + 2;
            }

            ps.executeUpdate();
            final ResultSet rs = ps.getGeneratedKeys();

            if (rs.first()) {
                int i = 0;
                jaAdvRecipients = new JSONArray();
                do {
                    final JSONObject joRecipient = new JSONObject();
                    joRecipient.put(SMSRequestStatuses.COLUMN_RECIPIENT_ID, rs.getString(1));
                    joRecipient.put(Recipients.COLUMN_RECIPIENT, jaRecipients.getString(i++));

                    jaAdvRecipients.put(joRecipient);
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

        if (jaAdvRecipients == null) {
            throw new InsertFailedException("Failed to insert recipients");
        }

        return jaAdvRecipients;
    }
}
