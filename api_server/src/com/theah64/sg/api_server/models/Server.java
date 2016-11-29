package com.theah64.sg.api_server.models;

/**
 * Created by theapache64 on 29/11/16,8:02 AM.
 * name, deviceName, imei, deviceHash, fcmId, serverKey
 */
public class Server {

    private final String name, deviceName, imei, deviceHash, fcmId, serverKey;

    public Server(String name, String deviceName, String imei, String deviceHash, String fcmId, String serverKey) {
        this.name = name;
        this.deviceName = deviceName;
        this.imei = imei;
        this.deviceHash = deviceHash;
        this.fcmId = fcmId;
        this.serverKey = serverKey;
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
}

