package com.stream.service.response;

/**
 * Created by home on 2015-03-23.
 */
public abstract class AbstractResponse {

    private boolean success;

    private String error;

    public AbstractResponse(String error, boolean success){
        this.success = success;
        this.error = error;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
