package com.theah64.sg.api_server.models;

/**
 * Created by theapache64 on 29/11/16,8:02 AM.
 * name, deviceName, imei, deviceHash, fcmId, serverKey
 */
public class Server {

    private String name;
    private String email;
    private String simSerial;
    private final String deviceName;
    private final String imei;
    private final String serverKey;
    private final String deviceHash;
    private String fcmId;
    private String id;

    public Server(String id, String name, String email, String simSerial, String deviceName, String imei, String deviceHash, String fcmId, String serverKey) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.simSerial = simSerial;
        this.deviceName = deviceName;
        this.imei = imei;
        this.deviceHash = deviceHash;
        this.fcmId = fcmId;
        this.serverKey = serverKey;
    }

    public String getEmail() {
        return email;
    }

    public String getSimSerial() {
        return simSerial;
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

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Server{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", simSerial='" + simSerial + '\'' +
                ", deviceName='" + deviceName + '\'' +
                ", imei='" + imei + '\'' +
                ", deviceHash='" + deviceHash + '\'' +
                ", fcmId='" + fcmId + '\'' +
                ", serverKey='" + serverKey + '\'' +
                ", id='" + id + '\'' +
                '}';
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSimSerial(String simSerial) {
        this.simSerial = simSerial;
    }

    public void setFcmId(String fcmId) {
        this.fcmId = fcmId;
    }
}

