package com.theah64.sg.api_server.database.tables;

import com.theah64.sg.api_server.database.Connection;
import com.theah64.sg.api_server.models.Server;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by theapache64 on 29/11/16,8:01 AM.
 */
public class Servers extends BaseTable<Server> {
    private static final Servers instance = new Servers();
    public static final String COLUMN_FCM_ID = "fcm_id";
    public static final String COLUMN_DEVICE_HASH = "device_hash";
    public static final String COLUMN_IMEI = "imei";
    public static final String COLUMN_DEVICE_NAME = "device_name";
    public static final String COLUMN_SERVER_KEY = "server_key";

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
        final String query = "INSERT INTO servers (name, device_name, imei,device_hash, fcm_id, server_key) VALUES (?,?,?,?,?,?); ";

        final java.sql.Connection con = Connection.getConnection();

        try {

            final PreparedStatement ps = con.prepareStatement(query);

            ps.setString(1, server.getName());
            ps.setString(2, server.getDeviceHash());
            ps.setString(3, server.getImei());
            ps.setString(4, server.getDeviceHash());
            ps.setString(5, server.getFcmId());
            ps.setString(6, server.getServerKey());

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
}
