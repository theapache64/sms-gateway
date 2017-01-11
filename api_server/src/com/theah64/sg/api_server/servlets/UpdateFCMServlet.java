package com.theah64.sg.api_server.servlets;

import com.theah64.sg.api_server.database.tables.BaseTable;
import com.theah64.sg.api_server.database.tables.Servers;
import com.theah64.sg.api_server.utils.APIResponse;
import com.theah64.sg.api_server.utils.RequestException;
import com.theah64.sg.api_server.utils.ServerHeaderSecurity;
import org.json.JSONException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by theapache64 on 19/11/16,3:11 PM.
 */
@WebServlet(urlPatterns = {AdvancedBaseServlet.VERSION_CODE + "/update_fcm"})
public class UpdateFCMServlet extends AdvancedBaseServlet {

    @Override
    protected boolean isSecureServlet() {
        return false;
    }

    @Override
    protected String[] getRequiredParameters() {
        return new String[]{Servers.COLUMN_FCM_ID};
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    @Override
    protected void doAdvancedPost() throws RequestException, BaseTable.InsertFailedException, JSONException, BaseTable.UpdateFailedException {
        final ServerHeaderSecurity serverHeaderSecurity = new ServerHeaderSecurity(getHttpServletRequest().getHeader(ServerHeaderSecurity.KEY_AUTHORIZATION));
        final String serverId = serverHeaderSecurity.getServerId();
        final String fcmId = getStringParameter(Servers.COLUMN_FCM_ID);
        Servers.getInstance().update(Servers.COLUMN_ID, serverId, Servers.COLUMN_FCM_ID, fcmId);
        getWriter().write(new APIResponse("FCM id updated", null).getResponse());
    }
}
