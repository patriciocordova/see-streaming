package com.stream.service.request;

import com.stream.service.response.AbstractResponse;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;


/**
 * Created by home on 2015-03-20.
 */
public abstract class AbstractRequest {

    private Object lock = new Object();

    private boolean completed = false;

    private HttpClient httpClient = new DefaultHttpClient();

    protected AbstractResponse response;

    public AbstractRequest() {
        /*HttpParams httpParams = httpClient.getParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, 10000);
        HttpConnectionParams.setSoTimeout(httpParams, 10000);*/
    }

    public AbstractResponse getResponse(){
        return this.response;
    }


    public void run(){
        runOnThread();
    }

    protected void runOnThread() {

        new Thread(){
            @Override
            public void run() {
                response = threadLogic();

                synchronized (lock) {
                    completed = true;
                    lock.notifyAll();
                }
            }
        }.start();

        synchronized (lock){
            while(!completed){
                try {
                    lock.wait();
                }
                catch (InterruptedException e) {
                    // Ignore
                }
            }
        }
    }

    protected JSONObject executePost(HttpPost post){
        HttpResponse response = null;
        ByteArrayOutputStream out = null;
        try {
            response = httpClient.execute(post);
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                String responseString = out.toString();
                JSONObject object = new JSONObject(responseString);
                return object;
            }
        }
        catch (Exception e){
            // ignore
        }
        finally{

            try {
                if(response != null){
                    HttpEntity entity = response.getEntity();
                    if(entity != null) {
                        InputStream stream = entity.getContent();
                        if (stream != null) {
                            stream.close();
                        }
                    }
                }

                if(out != null){
                    out.flush();
                    out.close();
                }

            }
            catch (Exception e){
                // Ignore
            }
        }

        return null;
    }


    protected JSONObject executeGet(HttpGet get){
        HttpResponse response = null;
        ByteArrayOutputStream out = null;
        try {
            response = httpClient.execute(get);
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                String responseString = out.toString();
                JSONObject object = new JSONObject(responseString);
                return object;
            }
        }
        catch (Exception e){
            // Ignore
        }
        finally{
            try {
                if(response != null){
                    HttpEntity entity = response.getEntity();
                    if(entity != null) {
                        InputStream stream = entity.getContent();
                        if (stream != null) {
                            stream.close();
                        }
                    }
                }

                if(out != null){
                    out.flush();
                    out.close();
                }

            }
            catch (Exception e){
                // Ignore
            }
        }

        return null;
    }

    public abstract AbstractResponse threadLogic();

}
