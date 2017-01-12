package com.theah64.sg.api_server.servlets;

import com.theah64.sg.api_server.database.tables.BaseTable;
import com.theah64.sg.api_server.database.tables.Recipients;
import com.theah64.sg.api_server.database.tables.SMSRequestStatuses;
import com.theah64.sg.api_server.database.tables.SMSRequests;
import com.theah64.sg.api_server.models.SMSRequest;
import com.theah64.sg.api_server.models.SMSRequestStatus;
import com.theah64.sg.api_server.utils.APIResponse;
import com.theah64.sg.api_server.utils.RequestException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by theapache64 on 1/12/16,6:10 AM.
 */
@WebServlet(urlPatterns = {AdvancedBaseServlet.VERSION_CODE + "/get_status"})
public class GetStatusServlet extends AdvancedBaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    @Override
    protected boolean isSecureServlet() {
        return true;
    }

    @Override
    protected String[] getRequiredParameters() {
        return new String[]{Recipients.COLUMN_SMS_REQUEST_ID};
    }

    @Override
    protected void doAdvancedPost() throws BaseTable.InsertFailedException, JSONException, BaseTable.UpdateFailedException, RequestException {

        final String userId = getHeaderSecurity().getUserId();
        final String smsReqId = getStringParameter(Recipients.COLUMN_SMS_REQUEST_ID);

        //Getting the message first
        final SMSRequest smsRequest = SMSRequests.getInstance().get(SMSRequests.COLUMN_ID, smsReqId, SMSRequests.COLUMN_USER_ID, userId);

        final JSONArray jaStatuses = SMSRequestStatuses.getInstance().getStatuses(smsReqId, userId);
        final JSONObject joData = new JSONObject();

        joData.put(SMSRequests.COLUMN_TOTAL_PARTS, smsRequest.getTotalParts());
        joData.put(SMSRequestStatus.KEY_STATUSES, jaStatuses);

        getWriter().write(new APIResponse("status available", joData).getResponse());
    }
}
