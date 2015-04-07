package com.stream.service.request;

import com.stream.service.response.AbstractResponse;
import com.stream.service.response.CreateChannelResponse;
import com.stream.util.ServiceUtil;
import com.stream.util.StorageUtil;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;


/**
 * Created by home on 2015-03-20.
 */
public class CreateChannel extends AbstractRequest {

    private String channelName;

    public CreateChannel(String channelName){
        this.channelName = channelName;
    }

    @Override
    public AbstractResponse threadLogic() {
        try {
            JSONObject params = new JSONObject();
            params.put(ServiceUtil.PayloadKeys.ChannelName.getKey(), channelName);
            params.put(ServiceUtil.PayloadKeys.UserId.getKey(), StorageUtil.getStringValue(ServiceUtil.PayloadKeys.UserId.getKey()));
            StringEntity stringEntity = new StringEntity(params.toString(), ServiceUtil.DEFAULT_CHARSET);

            HttpPost post = ServiceUtil.getPostRequest(ServiceUtil.CREATE_CHANNEL, stringEntity);
            final JSONObject result = executePost(post);

            return new CreateChannelResponse(result.getString(ServiceUtil.ResponseKeys.Error.getKey()), result.getBoolean(ServiceUtil.ResponseKeys.Success.getKey()));
        }
        catch (Exception e) {
            // Ignore
        }

        return new CreateChannelResponse("Create channel failed!", false);
    }

}
