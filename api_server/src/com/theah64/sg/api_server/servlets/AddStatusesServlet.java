package com.theah64.sg.api_server.servlets;

import com.theah64.sg.api_server.database.tables.BaseTable;
import com.theah64.sg.api_server.database.tables.Recipients;
import com.theah64.sg.api_server.utils.Request;
import com.theah64.sg.api_server.utils.ServerHeaderSecurity;
import org.json.JSONException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by shifar on 30/11/16.
 * server_key (as header), message_id, array( recepient_id,status, occurred_at)
 */
public class AddStatusesServlet extends AdvancedBaseServlet {

    private static final String KEY_STATUSES = "statuses";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    @Override
    protected boolean isSecureServlet() {
        return false;
    }

    @Override
    protected String[] getRequiredParameters() {
        return new String[]{Recipients.COLUMN_SMS_REQUEST_ID, KEY_STATUSES};
    }

    @Override
    protected void doAdvancedPost() throws BaseTable.InsertFailedException, JSONException, BaseTable.UpdateFailedException, Request.RequestException {

        final ServerHeaderSecurity serverHeaderSecurity = new ServerHeaderSecurity(getHttpServletRequest().getHeader(ServerHeaderSecurity.KEY_AUTHORIZATION));
        final String serverId = serverHeaderSecurity.getServerId();


    }
}
