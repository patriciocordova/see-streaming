package com.stream.service.request;

import com.stream.service.response.AbstractResponse;
import com.stream.service.response.PollResponse;
import com.stream.util.ServiceUtil;

import org.apache.http.client.methods.HttpPost;
import org.json.JSONObject;


/**
 * Created by home on 2015-03-22.
 */
public class Poll extends AbstractRequest {

    @Override
    public AbstractResponse threadLogic() {
        try {
            HttpPost post = ServiceUtil.getPostRequest(ServiceUtil.POLL, ServiceUtil.getPollRequestEntity());
            JSONObject result = executePost(post);

            if (!result.getBoolean(ServiceUtil.ResponseKeys.Success.getKey())) {
                return new PollResponse(result.getString(ServiceUtil.ResponseKeys.Error.getKey()), false);
            }
            else {
                return new PollResponse(null, true, result.getString(ServiceUtil.ResponseKeys.VideoUrl.getKey()));
            }
        } catch (Exception e) {
            // Ignore
        }

        return new PollResponse("Poll failed!", false);
    }
}
