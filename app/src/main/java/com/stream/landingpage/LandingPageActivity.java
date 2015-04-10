package com.stream.landingpage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.stream.R;

import com.stream.gcm.GCM;

import com.stream.publishing.PublishActivity;
import com.stream.service.request.CreateChannel;
import com.stream.service.request.DeleteAll;
import com.stream.service.request.GetAvailableChannels;
import com.stream.service.request.Subscribe;
import com.stream.service.response.AbstractResponse;
import com.stream.service.response.GetAvailableChannelsResponse;
import com.stream.util.ActivityUtil;
import com.stream.util.ServiceUtil;
import com.stream.util.StorageUtil;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LandingPageActivity extends Activity {


    private void initializeChannelNameEditTest(){

        EditText channelName_EditText = (EditText) findViewById(R.id.channel_name);
        String channelName = StorageUtil.getStringValue(ServiceUtil.PayloadKeys.ChannelName.getKey());
        if(channelName != null && !channelName.isEmpty()) {
            channelName_EditText.setText("Your Channel Name is : " + channelName);
            channelName_EditText.setEnabled(false);

            // Remove create channel button when channel already exists
            Button createChannel = (Button) findViewById(R.id.create_channel);
            createChannel.setVisibility(View.GONE);
        }
        else {
            channelName_EditText.setText("Enter Channel Name");
        }

        // On clicking the channel name, clear the default text
        channelName_EditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(v instanceof  EditText) {
                    EditText editText = (EditText)v;
                    if (editText.getText().toString().equals("Enter Channel Name")) {
                        editText.setText("");
                    }
                }
            }
        });
    }

    private void initializeSubscriptionsList(){

        Set<String> storedSubscriptions = StorageUtil.getStringValues(ServiceUtil.PayloadKeys.Subscriptions.getKey());

        ListView subscriptionList = (ListView) findViewById(R.id.subscription_list);
        TextView subscriptionText = (TextView) findViewById(R.id.subscription_list_text);

        if (storedSubscriptions != null && !storedSubscriptions.isEmpty()) {
            subscriptionList.setVisibility(View.VISIBLE);
            subscriptionText.setVisibility(View.VISIBLE);

            List<String> subscriptions = new ArrayList<String>(storedSubscriptions);

            ArrayAdapter<String> subscriptionListAdapter = new ArrayAdapter<String>(ActivityUtil.getMainActivity(),
                    android.R.layout.simple_list_item_1, android.R.id.text1, subscriptions);

            subscriptionList.setAdapter(subscriptionListAdapter);
        }
    }

    private void initializeCreateChannel(){

        Button createChannel = (Button) findViewById(R.id.create_channel);
        createChannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText channelName_EditText = (EditText) findViewById(R.id.channel_name);
                String channelName = channelName_EditText.getText().toString();
                if (channelName == null || channelName.isEmpty()) {
                    Toast.makeText(ActivityUtil.getMainActivity(), "Please enter a channel name", Toast.LENGTH_SHORT).show();
                }
                else {
                    CreateChannel createChannel = new CreateChannel(channelName);
                    createChannel.run();

                    AbstractResponse response = createChannel.getResponse();
                    if (!response.isSuccess()) {
                        Toast.makeText(ActivityUtil.getMainActivity(), response.getError(), Toast.LENGTH_SHORT).show();
                    }
                    else {
                        // Store channel name
                        //StorageUtil.setBooleanValue(StorageUtil.SharedPreferenceKeys.ChannelCreated.getKey(), true);
                        StorageUtil.setStringValue(ServiceUtil.PayloadKeys.ChannelName.getKey(), channelName);
                        Toast.makeText(ActivityUtil.getMainActivity(), "Success!", Toast.LENGTH_SHORT).show();

                        // Change channel name edit text and Remove button, once channel is created
                        if(v instanceof  Button){
                            Button button = (Button)v;
                            button.setVisibility(View.GONE);
                        }

                        channelName_EditText.setText("Your Channel Name is : " + channelName);
                        channelName_EditText.setEnabled(false);
                    }
                }
            }
        });
    }

    private void loseTheKeyboard(){
        // Make keyboard disappear
        Button subscribe = (Button) findViewById(R.id.subscribe_channel);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(subscribe.getWindowToken(), 0);
    }

    private List<String> getChannelsList(){
        GetAvailableChannels getAvailableChannels = new GetAvailableChannels();
        getAvailableChannels.run();
        GetAvailableChannelsResponse response = (GetAvailableChannelsResponse)getAvailableChannels.getResponse();
        if(!response.isSuccess()){
            Toast.makeText(ActivityUtil.getMainActivity(), response.getError(), Toast.LENGTH_SHORT).show();
        }

        List<String> channelsList = response.getChannelsList();

        if (channelsList == null || channelsList.isEmpty()) {
            Toast.makeText(ActivityUtil.getMainActivity(), "No channels to subscribe!", Toast.LENGTH_SHORT).show();
            return null;
        }

        // remove current user's channel before showing subscriptions
        String channelName = StorageUtil.getStringValue(ServiceUtil.PayloadKeys.ChannelName.getKey());
        if (channelName != null) {
            channelsList.remove(channelName.trim());
        }

        // remove any subscribed channels
        Set<String> currentSubscriptions = StorageUtil.getStringValues(ServiceUtil.PayloadKeys.Subscriptions.getKey());
        if(currentSubscriptions != null && !currentSubscriptions.isEmpty()){
            channelsList.removeAll(currentSubscriptions);
        }

        if (channelsList == null || channelsList.isEmpty()) {
            Toast.makeText(ActivityUtil.getMainActivity(), "No channels to subscribe!", Toast.LENGTH_SHORT).show();
            return null;
        }

        return channelsList;
    }


    private void setAvailableChannelList(List<String> channelsList){
        ListView channelList = (ListView) findViewById(R.id.channels_list);
        TextView channelText = (TextView) findViewById(R.id.channels_list_text);

        ArrayAdapter<String> channelListAdapter = new ArrayAdapter<String>(ActivityUtil.getMainActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, channelsList);

        channelList.setAdapter(channelListAdapter);

        channelList.setVisibility(View.VISIBLE);
        channelText.setVisibility(View.VISIBLE);
    }

    private void initializeSubscribeToChannel(){
        Button subscribe = (Button) findViewById(R.id.subscribe_channel);
        subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loseTheKeyboard();

                List<String> channelsList = getChannelsList();

                if(channelsList != null) {
                    setAvailableChannelList(channelsList);
                }
            }
        });
    }

    private void initializeStartVideo(){
        Button start = (Button) findViewById(R.id.start_video);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityUtil.getMainActivity(), PublishActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void initializeSelectFromSubscriptionList() {

        ListView channelList = (ListView) findViewById(R.id.channels_list);
        channelList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Object o = parent.getItemAtPosition(position);
                String selectedChannel = null;
                if(o instanceof String){
                    selectedChannel = (String)o;
                }
                else{
                    Toast.makeText(ActivityUtil.getMainActivity(), "Please make a valid selection", Toast.LENGTH_SHORT).show();;
                    return;
                }

                Subscribe subscribe = new Subscribe(selectedChannel);
                subscribe.run();

                AbstractResponse response = subscribe.getResponse();
                if (!response.isSuccess()) {
                    Toast.makeText(ActivityUtil.getMainActivity(), response.getError(), Toast.LENGTH_SHORT).show();
                }
                else {
                    Set<String> oldSubscriptions = StorageUtil.getStringValues(ServiceUtil.PayloadKeys.Subscriptions.getKey());
                    if (oldSubscriptions == null || oldSubscriptions.isEmpty()) {
                        oldSubscriptions = new HashSet<String>(1);
                    }

                    oldSubscriptions.add(selectedChannel);
                    StorageUtil.setStringValues(ServiceUtil.PayloadKeys.Subscriptions.getKey(), oldSubscriptions);

                    ArrayAdapter<String> subscriptionsAdapter = new ArrayAdapter<String>(ActivityUtil.getMainActivity(),
                            android.R.layout.simple_list_item_1, android.R.id.text1, new ArrayList<String>(oldSubscriptions));
                    ListView subscriptionList = (ListView) findViewById(R.id.subscription_list);
                    subscriptionList.setAdapter(subscriptionsAdapter);
                    subscriptionList.setVisibility(View.VISIBLE);

                    TextView subscriptionText = (TextView) findViewById(R.id.subscription_list_text);
                    subscriptionText.setVisibility(View.VISIBLE);

                    // TODO Remove subscription from available channels list
                    /*List<String> channelList = getChannelsList();
                    if(channelList != null && !channelList.isEmpty())
                        channelList = new ArrayList<String>();*/


                    //setAvailableChannelList(channelList);



                    Toast.makeText(ActivityUtil.getMainActivity(), "Success!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void initialize(){
        initializeChannelNameEditTest();

        initializeSubscriptionsList();

        initializeCreateChannel();

        initializeSubscribeToChannel();

        initializeStartVideo();

        initializeSelectFromSubscriptionList();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        ActivityUtil.setMainActivity(this);

        boolean status = GCM.getInstance().registerGCM();
        if(!status){
            Toast.makeText(this, "GCM Unavailable. Use Poll", Toast.LENGTH_SHORT).show();
        }

        initialize();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_landing_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.poll) {
            //NotificationHandler.getInstance().start();
            return true;
        }
        else if (id == R.id.reset){
            resetEverything();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void resetEverything() {

        // call server delete all
        DeleteAll deleteAll = new DeleteAll();
        deleteAll.run();

        // if true
        AbstractResponse response = deleteAll.getResponse();
        if(response.isSuccess()) {

            // remove all shared prefs
            getApplicationContext().getSharedPreferences("SeeSetup", 0).edit().clear().commit();


            // reset UI
            EditText editText = (EditText) findViewById(R.id.channel_name);
            editText.setText("Enter Channel Name");
            editText.setEnabled(true);

            Button createChannel = (Button) findViewById(R.id.create_channel);
            createChannel.setVisibility(View.VISIBLE);
            createChannel.setEnabled(true);

            ListView subscriptionList = (ListView) findViewById(R.id.subscription_list);
            TextView subscriptionText = (TextView) findViewById(R.id.subscription_list_text);

            subscriptionList.setVisibility(View.INVISIBLE);
            subscriptionList.setAdapter(null);
            subscriptionText.setVisibility(View.INVISIBLE);

            ListView channelList = (ListView) findViewById(R.id.channels_list);
            channelList.setAdapter(null);

            TextView channelText = (TextView) findViewById(R.id.channels_list_text);
            channelText.setVisibility(View.GONE);


            //register
            GCM.getInstance().registerGCM();
        }
        else{
            Toast.makeText(this, response.getError(), Toast.LENGTH_SHORT).show();
        }
    }
}
