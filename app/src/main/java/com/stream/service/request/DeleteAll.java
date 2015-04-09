package com.stream.service.request;

import com.stream.service.response.AbstractResponse;
import com.stream.service.response.CreateChannelResponse;
import com.stream.service.response.DeleteAllResponse;
import com.stream.util.ServiceUtil;
import com.stream.util.StorageUtil;

import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import java.net.URL;

/**
 * Created by home on 2015-04-09.
 */
public class DeleteAll extends AbstractRequest {


    @Override
    public AbstractResponse threadLogic() {
        try {

            URL postUrl = new URL(ServiceUtil.DELETE_ALL);
            HttpDelete delete = new HttpDelete(postUrl.toURI());
            final JSONObject result = executeDelete(delete);
            return new DeleteAllResponse(result.getString(ServiceUtil.ResponseKeys.Error.getKey()), result.getBoolean(ServiceUtil.ResponseKeys.Success.getKey()));
        }
        catch (Exception e) {
            // Ignore
        }

        return new DeleteAllResponse("Delete All!", false);
    }
}
