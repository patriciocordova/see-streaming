package com.stream;

import com.stream.util.ServiceUtil;
import com.stream.util.StorageUtil;

public class Credentials {
    public static String address = "52.10.144.216:1935/";
    public static String protocol = "rtmp";
    public static String streamName = "/myStream";
    public static String user = "see";
    public static String pass = "see";

    public static String getPublishUrl(){
        return protocol + "://" + user + ":" + pass + "@" + address + StorageUtil.getStringValue(ServiceUtil.PayloadKeys.ChannelName.getKey()) + streamName;
    }

    public static String getConsumeUrl(){
        return protocol + "://" + address;
    }
}
