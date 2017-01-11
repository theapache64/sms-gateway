package com.theah64.sg.api_server.utils;

import com.theah64.sg.api_server.database.tables.Servers;

/**
 * Created by theapache64 on 29/11/16,8:04 AM.
 */
public class ServerHeaderSecurity extends HeaderSecurity {

    private String serverId;

    public ServerHeaderSecurity(String authorization) throws RequestException {
        super(authorization);
    }

    @Override
    protected void isAuthorized() throws RequestException {

        if (getAuthorization() == null) {
            //No api key passed along with request
            throw new RequestException("Unauthorized access");
        }

        final Servers servers = Servers.getInstance();
        this.serverId = servers.get(Servers.COLUMN_SERVER_KEY, getAuthorization(), Servers.COLUMN_ID, true);
        if (this.serverId == null) {
            throw new RequestException("No server found with the key " + getAuthorization());
        }

    }

    public String getServerId() {
        return serverId;
    }

    @Override
    public String getUserId() {
        throw new IllegalArgumentException("Invalid method");
    }
}

