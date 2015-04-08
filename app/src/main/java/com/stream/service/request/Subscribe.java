package com.stream.service.request;

import com.stream.service.response.AbstractResponse;
import com.stream.service.response.SubscribeResponse;
import com.stream.util.ServiceUtil;
import com.stream.util.StorageUtil;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;


/**
 * Created by home on 2015-03-20.
 */
public class Subscribe extends AbstractRequest {

    private String newSubscription;

    public Subscribe(String newSubscription){
        this.newSubscription = newSubscription;
    }

    @Override
    public AbstractResponse threadLogic() {
        try {
            JSONObject params = new JSONObject();

            //JSONArray channelNamesArray = new JSONArray();
            /*Iterator<String> iterChannelNames = this.channelNames.iterator();
            while(iterChannelNames.hasNext()){
                String channel = iterChannelNames.next();
                channelNamesArray.put(channel);
            }*/

            /*JSONObject object = new JSONObject();

            params.put(ServiceUtil.PayloadKeys.ChannelNames.getKey(), channelNamesArray);
            params.put(ServiceUtil.PayloadKeys.UserId.getKey(), StorageUtil.getStringValue(ServiceUtil.PayloadKeys.UserId.getKey()));*/

            String requesterChannelName = StorageUtil.getStringValue(ServiceUtil.PayloadKeys.ChannelName.getKey());

            params.put(ServiceUtil.PayloadKeys.RequesterChannelName.getKey(), requesterChannelName);
            params.put(ServiceUtil.PayloadKeys.NewSubscription.getKey(), this.newSubscription);

            StringEntity stringEntity = new StringEntity(params.toString(), ServiceUtil.DEFAULT_CHARSET);

            HttpPost post = ServiceUtil.getPostRequest(ServiceUtil.SUBSCRIBE, stringEntity);
            JSONObject result = executePost(post);

            return new SubscribeResponse(result.getString(ServiceUtil.ResponseKeys.Error.getKey()), result.getBoolean(ServiceUtil.ResponseKeys.Success.getKey()));
        }
        catch (Exception e) {

        }

        return new SubscribeResponse("Subscribe failed!", false);
    }
}
