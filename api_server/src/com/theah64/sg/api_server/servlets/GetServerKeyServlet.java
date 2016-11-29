package com.theah64.sg.api_server.servlets;

import com.theah64.sg.api_server.database.tables.BaseTable;
import com.theah64.sg.api_server.database.tables.Preference;
import com.theah64.sg.api_server.database.tables.Servers;
import com.theah64.sg.api_server.models.Server;
import com.theah64.sg.api_server.utils.APIResponse;
import com.theah64.sg.api_server.utils.DarKnight;
import com.theah64.sg.api_server.utils.MailHelper;
import com.theah64.sg.api_server.utils.RandomString;
import org.json.JSONException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by theapache64 on 29/11/16,8:23 AM.
 * 5	get_server_key	device_hash, name,fcm_id	server_key	To get new server key	NOT READY	CLIENT-API
 */
@WebServlet(urlPatterns = {AdvancedBaseServlet.VERSION_CODE + "/get_server_key"})
public class GetServerKeyServlet extends AdvancedBaseServlet {


    private static final int API_KEY_LENGTH = 10;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        super.doPost(req, resp);
    }

    @Override
    protected boolean isSecureServlet() {
        return false;
    }

    @Override
    protected String[] getRequiredParameters() {
        return new String[]{Servers.COLUMN_IMEI, Servers.COLUMN_DEVICE_NAME, Servers.COLUMN_NAME, Servers.COLUMN_FCM_ID};
    }

    @Override
    protected void doAdvancedPost() throws BaseTable.InsertFailedException, JSONException, BaseTable.UpdateFailedException {

        final String imei = getStringParameter(Servers.COLUMN_IMEI);
        final String deviceName = getStringParameter(Servers.COLUMN_DEVICE_NAME);
        final String deviceHash = DarKnight.getEncrypted(imei + deviceName);

        final Servers servers = Servers.getInstance();

        //Checking if the email already has an api_key.
        String serverKey = servers.get(Servers.COLUMN_DEVICE_HASH, deviceHash, Servers.COLUMN_SERVER_KEY, true);
        final String fcmId = getStringParameter(Servers.COLUMN_FCM_ID);
        boolean isNewServer;

        if (serverKey == null) {
            serverKey = RandomString.getNewApiKey(API_KEY_LENGTH);

            final String name = getStringParameter(Servers.COLUMN_NAME);

            final Server server = new Server(name, deviceName, imei, deviceHash, fcmId, serverKey);
            servers.add(server);

            final String message = String.format("Hey, New server established\n\nServer: %s\n\nThat's it. :) ", server.toString());
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //Sending mail to admin about the new member join
                    MailHelper.sendMail(Preference.getInstance().getString(Preference.KEY_ADMIN_EMAIL), "SG - New server joined", message);
                }
            }).start();

            isNewServer = true;
        } else {
            isNewServer = false;
        }

        if (!isNewServer) {
            //Updating fcm on old server
            servers.update(Servers.COLUMN_SERVER_KEY, serverKey, Servers.COLUMN_FCM_ID, fcmId);
        }


        getWriter().write(new APIResponse("Server established", Servers.COLUMN_SERVER_KEY, serverKey).getResponse());


    }
}
