package com.stream.service.request;

import com.stream.gcm.GCM;
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

            //String requesterChannelName = StorageUtil.getStringValue(ServiceUtil.PayloadKeys.ChannelName.getKey());
            String registrationId = StorageUtil.getStringValue(GCM.PROPERTY_REG_ID);

            params.put(ServiceUtil.PayloadKeys.RegistrationId.getKey(), registrationId);
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
