package com.theah64.sg.api_server.servlets;

import com.theah64.sg.api_server.database.tables.BaseTable;
import com.theah64.sg.api_server.database.tables.SMSRequestStatuses;
import com.theah64.sg.api_server.models.SMSRequestStatus;
import com.theah64.sg.api_server.utils.APIResponse;
import com.theah64.sg.api_server.utils.RequestException;
import com.theah64.sg.api_server.utils.ServerHeaderSecurity;
import org.json.JSONArray;
import org.json.JSONException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by shifar on 30/11/16.
 * server_key (as header), message_id, array( recepient_id,status, occurred_at)
 */
@WebServlet(urlPatterns = {AdvancedBaseServlet.VERSION_CODE + "/add_statuses"})
public class AddStatusesServlet extends AdvancedBaseServlet {

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
        return new String[]{SMSRequestStatus.KEY_STATUSES};
    }

    @Override
    protected void doAdvancedPost() throws BaseTable.InsertFailedException, JSONException, BaseTable.UpdateFailedException, RequestException {
        new ServerHeaderSecurity(getHttpServletRequest().getHeader(ServerHeaderSecurity.KEY_AUTHORIZATION));
        final JSONArray jaStatuses = new JSONArray(getStringParameter(SMSRequestStatus.KEY_STATUSES));
        SMSRequestStatuses.getInstance().add(jaStatuses);
        getWriter().write(new APIResponse("Status(es) added", null).getResponse());
    }
}
