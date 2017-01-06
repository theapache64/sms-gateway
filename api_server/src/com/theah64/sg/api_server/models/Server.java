package com.theah64.sg.api_server.models;

/**
 * Created by theapache64 on 29/11/16,8:02 AM.
 * name, deviceName, imei, deviceHash, fcmId, serverKey
 */
public class Server {

    private final String name,email, deviceName, imei, deviceHash, fcmId, serverKey;
    private String id;

    public Server(String id, String name, String email, String deviceName, String imei, String deviceHash, String fcmId, String serverKey) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.deviceName = deviceName;
        this.imei = imei;
        this.deviceHash = deviceHash;
        this.fcmId = fcmId;
        this.serverKey = serverKey;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getImei() {
        return imei;
    }

    public String getDeviceHash() {
        return deviceHash;
    }

    public String getFcmId() {
        return fcmId;
    }

    public String getServerKey() {
        return serverKey;
    }

    @Override
    public String toString() {
        return "Server{" +
                "name='" + name + '\'' +
                ", deviceName='" + deviceName + '\'' +
                ", imei='" + imei + '\'' +
                ", deviceHash='" + deviceHash + '\'' +
                ", fcmId='" + fcmId + '\'' +
                ", serverKey='" + serverKey + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }
}

