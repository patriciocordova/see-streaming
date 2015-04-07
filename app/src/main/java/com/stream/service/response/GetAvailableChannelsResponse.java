package com.stream.service.response;

import java.util.List;

/**
 * Created by home on 2015-03-23.
 */
public class GetAvailableChannelsResponse extends AbstractResponse {

    private List<String> channelsList;

    public GetAvailableChannelsResponse(String error, boolean success) {
        super(error, success);
    }

    public GetAvailableChannelsResponse(String error, boolean success, List<String> channelsList) {
        super(error, success);
        this.channelsList = channelsList;
    }

    public List<String> getChannelsList(){
        return this.channelsList;
    }

}
