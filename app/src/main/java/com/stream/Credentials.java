package com.stream;

public class Credentials {
    public static String address = "52.10.144.216:1935/live/myStream";
    public static String protocol = "rtmp";
    public static String user = "see";
    public static String pass = "see";

    public static String getPublishUrl(){
        return protocol + "://" + user + ":" + pass + "@" + address;
    }

    public static String getConsumeUrl(){
        return protocol + "://" + address;
    }
}
