package com.stream.util;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created by home on 2015-03-20.
 */
public final class ServiceUtil {

    // WOWZA SERVER
    public static final String STREAM_URL = "rtsp://52.10.125.217:1935/live/myStream";
    public static final String PUBLISHER_USERNAME = "see";
    public static final String PUBLISHER_PASSWORD = "see";

    // REST SERVICE
    public static final String LOCAL_URL_HOST = "http://192.168.1.3:8080/SeeService";
    public static final String REMOTE_URL_HOST = "http://seedev.elasticbeanstalk.com";


    public static final String REGISTER_URL = REMOTE_URL_HOST + "/see/register";


    public static final String CREATE_CHANNEL = REMOTE_URL_HOST + "/see/createChannel";
    public static final String GET_CHANNELS = REMOTE_URL_HOST + "/see/getChannels";
    public static final String SUBSCRIBE = REMOTE_URL_HOST + "/see/subscribe";
    public static final String POLL = REMOTE_URL_HOST + "/see/poll";
    public static final String DELETE_ALL = REMOTE_URL_HOST + "/see/deleteAll";


    public static final String DEFAULT_CHARSET = "UTF-8";


    public enum PayloadKeys{
        UserId("userId"), ChannelName("channelName"), ChannelNames("channelNames"),
        Subscriptions("subscriptions"), RequesterChannelName("requestorChannelName"),
        NewSubscription("newSubscription"), RegistrationId("registrationId");

        private String key;

        PayloadKeys(String key){
            this.key = key;
        }

        public String getKey(){
            return this.key;
        }
    }

    public enum ResponseKeys{
        Success("success"), Error("errorMessage"),
        Channels("channels"), VideoUrl("videoUrl");

        private String key;

        ResponseKeys(String key){
            this.key = key;
        }

        public String getKey(){
            return this.key;
        }
    }


    public static HttpPost getPostRequest(String url, StringEntity entity) throws MalformedURLException, URISyntaxException {
        URL postUrl = new URL(url);
        HttpPost post = new HttpPost(postUrl.toURI());
        post.setEntity(entity);
        post.setHeader("Content-Type", "application/json");
        post.setHeader("Accept-Encoding", "application/json");

        return post;
    }

    public static HttpGet getRequest(String url) throws MalformedURLException, URISyntaxException {
        URL getUrl = new URL(url);
        HttpGet get = new HttpGet(getUrl.toURI());
        return get;
    }

    private static StringEntity POLL_STRING_ENTITY;
    static{
        JSONObject params = new JSONObject();
        try {
            params.put(PayloadKeys.ChannelName.getKey(), StorageUtil.getStringValue(PayloadKeys.ChannelName.getKey()));
            POLL_STRING_ENTITY = new StringEntity(params.toString(), ServiceUtil.DEFAULT_CHARSET);
        }
        catch (Exception e){
        }
    }

    public static StringEntity getPollRequestEntity(){

        StringEntity POLL_STRING_ENTITY = null;
        JSONObject params = new JSONObject();
        try {
            params.put(PayloadKeys.ChannelName.getKey(), StorageUtil.getStringValue(PayloadKeys.ChannelName.getKey()));
            POLL_STRING_ENTITY = new StringEntity(params.toString(), ServiceUtil.DEFAULT_CHARSET);
        }
        catch (Exception e) {
            e.printStackTrace();
        }



        return POLL_STRING_ENTITY;
    }



}
