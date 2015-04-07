package com.stream.service.request;

import com.stream.service.response.AbstractResponse;
import com.stream.service.response.RegisterUserResponse;
import com.stream.util.ServiceUtil;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

/**
 * Created by home on 2015-03-20.
 */
public class RegisterUser extends AbstractRequest {

    private String userId;

    public RegisterUser(String userId){
        this.userId = userId;
    }

    @Override
    public AbstractResponse threadLogic() {
        try {
            JSONObject params = new JSONObject();
            params.put(ServiceUtil.PayloadKeys.UserId.getKey(), userId);
            StringEntity stringEntity = new StringEntity(params.toString(), ServiceUtil.DEFAULT_CHARSET);

            HttpPost post = ServiceUtil.getPostRequest(ServiceUtil.REGISTER_URL, stringEntity);
            JSONObject result = executePost(post);

            return new RegisterUserResponse(result.getString(ServiceUtil.ResponseKeys.Error.getKey()), result.getBoolean(ServiceUtil.ResponseKeys.Success.getKey()));
        }
        catch (Exception e) {
            // Ignore
        }

        return new RegisterUserResponse("Registration Failed!", false);
    }
}
