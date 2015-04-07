package com.stream.service.response;

/**
 * Created by home on 2015-03-23.
 */
public class PollResponse extends  AbstractResponse {

    private String videoUrl;

    public PollResponse(String error, boolean success) {
        super(error, success);
    }

    public PollResponse(String error, boolean success, String videoUrl) {
        super(error, success);
        this.videoUrl = videoUrl;
    }

    public String getVideoUrl(){
        return this.videoUrl;
    }
}
