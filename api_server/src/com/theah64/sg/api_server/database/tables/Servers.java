package com.theah64.sg.api_server.database.tables;

import com.theah64.sg.api_server.database.Connection;
import com.theah64.sg.api_server.models.Server;
import com.theah64.sg.api_server.models.User;

import java.nio.channels.SeekableByteChannel;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by theapache64 on 29/11/16,8:01 AM.
 */
public class Servers extends BaseTable<Server> {
    public static final String COLUMN_FCM_ID = "fcm_id";
    public static final String COLUMN_DEVICE_HASH = "device_hash";
    public static final String COLUMN_IMEI = "imei";
    public static final String COLUMN_DEVICE_NAME = "device_name";
    public static final String COLUMN_SERVER_KEY = "server_key";
    private static final Servers instance = new Servers();
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_SIM_SERIAL = "sim_serial";

    private Servers() {
        super("servers");
    }

    public static Servers getInstance() {
        return instance;
    }


    /**
     * name, deviceName, imei, deviceHash, fcmId, serverKey;
     *
     * @param server
     * @return
     * @throws InsertFailedException
     */
    @Override
    public boolean add(Server server) throws InsertFailedException {
        boolean isFailed = false;
        final String query = "INSERT INTO servers (name, device_name, imei,device_hash, fcm_id, server_key,email,sim_serial) VALUES (?,?,?,?,?,?,?,?); ";

        final java.sql.Connection con = Connection.getConnection();

        try {

            final PreparedStatement ps = con.prepareStatement(query);

            ps.setString(1, server.getName());
            ps.setString(2, server.getDeviceName());
            ps.setString(3, server.getImei());
            ps.setString(4, server.getDeviceHash());
            ps.setString(5, server.getFcmId());
            ps.setString(6, server.getServerKey());
            ps.setString(7, server.getEmail());
            ps.setString(8, server.getSimSerial());


            isFailed = ps.executeUpdate() != 1;
            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
            isFailed = true;
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (isFailed) {
            throw new InsertFailedException("Failed to add new server");
        }

        return true;
    }


    @Override
    public Server get(String column, String value) {

        Server server = null;

        final String query = String.format("SELECT id,server_key FROM servers WHERE %s = ? LIMIT 1 ", column);

        final java.sql.Connection con = Connection.getConnection();
        try {

            final PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, value);
            final ResultSet rs = ps.executeQuery();

            if (rs.first()) {
                final String id = rs.getString(COLUMN_ID);
                final String serverKey = rs.getString(COLUMN_SERVER_KEY);
                server = new Server(id, null, null, null, null, null, null, null, serverKey);
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

        return server;
    }

    /**
     * @return least used server.
     */
    public Server getLeastUsedServer() {
        Server server = null;
        final String query = "SELECT  s.id,s.name,s.fcm_id, COUNT(sr.id) AS request_handled FROM servers s LEFT JOIN sms_requests sr ON sr.server_id = s.id WHERE s.is_active = 1 GROUP BY s.id ORDER BY request_handled LIMIT 1;";
        final java.sql.Connection con = Connection.getConnection();
        try {
            final Statement stmt = con.createStatement();
            final ResultSet rs = stmt.executeQuery(query);

            if (rs.first()) {
                final String id = rs.getString(COLUMN_ID);
                final String name = rs.getString(COLUMN_NAME);
                final String fcmId = rs.getString(COLUMN_FCM_ID);
                server = new Server(id, name, null, null, null, null, null, fcmId, null);

            }
            rs.close();
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return server;
    }

    @Override
    public boolean update(Server server) {
        boolean isUpdated = false;

        final String query = "UPDATE servers SET name = ?, email = ?,sim_serial = ?, fcm_id = ? WHERE id = ?";
        final java.sql.Connection con = Connection.getConnection();
        try {
            final PreparedStatement ps = con.prepareStatement(query);

            ps.setString(1, server.getName());
            ps.setString(2, server.getEmail());
            ps.setString(3, server.getSimSerial());
            ps.setString(4, server.getFcmId());
            ps.setString(5, server.getId());

            isUpdated = ps.executeUpdate() == 1;
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

        if (!isUpdated) {
            throw new IllegalArgumentException("Failed to update server");
        }

        return true;
    }
}
