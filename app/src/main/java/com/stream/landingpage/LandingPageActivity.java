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
import com.stream.notification.NotificationHandler;
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LandingPageActivity extends Activity {

    /*@Override
    protected void onNewIntent(Intent intent) {

        if(intent.getAction().equals(android.content.Intent.ACTION_VIEW)){
            NotificationHandler.getInstance().pause();
        }

        super.onNewIntent(intent);

    }*/

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 0){
            NotificationHandler.getInstance().resume();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }*/

    private void loadPublishActivity(){
        Intent intent = new Intent(this, PublishActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        ActivityUtil.setMainActivity(this);

        final Button createChannel = (Button) findViewById(R.id.create_channel);
        final EditText channelName_EditText = (EditText) findViewById(R.id.channel_name);

        String channelName = StorageUtil.getStringValue(ServiceUtil.PayloadKeys.ChannelName.getKey());
        if(channelName != null && !channelName.isEmpty()) {
            channelName_EditText.setText("Your Channel Name is : " + channelName);
            channelName_EditText.setEnabled(false);

            createChannel.setVisibility(View.GONE);
        }
        else {
            channelName_EditText.setText("Enter Channel Name");
        }

        Set<String> subscriptions = StorageUtil.getStringValues(ServiceUtil.PayloadKeys.Subscriptions.getKey());

        final ListView subscriptionList = (ListView) findViewById(R.id.subscription_list);
        final TextView subscriptionText = (TextView) findViewById(R.id.subscription_list_text);

        if (subscriptions != null && !subscriptions.isEmpty()) {
            subscriptionList.setVisibility(View.VISIBLE);
            subscriptionText.setVisibility(View.VISIBLE);

            List<String> subs = new ArrayList<String>(subscriptions);

            ArrayAdapter<String> subscriptionListAdapter = new ArrayAdapter<String>(ActivityUtil.getMainActivity(),
                    android.R.layout.simple_list_item_1, android.R.id.text1, subs);

            subscriptionList.setAdapter(subscriptionListAdapter);

            //NotificationHandler.getInstance().start();
        }

        channelName_EditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (channelName_EditText.getText().toString().equals("Enter Channel Name")) {
                    channelName_EditText.setText("");
                }
            }
        });

        createChannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (StorageUtil.getBooleanValue(StorageUtil.SharedPreferenceKeys.ChannelCreated.getKey())) {
                    Toast.makeText(ActivityUtil.getMainActivity(), "Only one channel allowed per user", Toast.LENGTH_SHORT).show();
                    return;
                }

                String channelName = channelName_EditText.getText().toString();
                if (channelName == null || channelName.isEmpty()) {
                    Toast.makeText(ActivityUtil.getMainActivity(), "Please enter a channel name", Toast.LENGTH_SHORT).show();
                } else {
                    CreateChannel createChannel = new CreateChannel(channelName);
                    createChannel.run();

                    AbstractResponse response = createChannel.getResponse();
                    if (!response.isSuccess()) {
                        Toast.makeText(ActivityUtil.getMainActivity(), response.getError(), Toast.LENGTH_SHORT).show();
                    } else {
                        StorageUtil.setBooleanValue(StorageUtil.SharedPreferenceKeys.ChannelCreated.getKey(), true);
                        StorageUtil.setStringValue(ServiceUtil.PayloadKeys.ChannelName.getKey(), channelName);
                        Toast.makeText(ActivityUtil.getMainActivity(), "Success!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        final Button subscribe = (Button) findViewById(R.id.subscribe_channel);
        subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                GetAvailableChannels getAvailableChannels = new GetAvailableChannels();
                getAvailableChannels.run();

                GetAvailableChannelsResponse response = (GetAvailableChannelsResponse)getAvailableChannels.getResponse();
                if(!response.isSuccess()){
                    Toast.makeText(ActivityUtil.getMainActivity(), response.getError(), Toast.LENGTH_SHORT).show();
                    return;
                }

                List<String> channelsList = response.getChannelsList();

                // remove current user's channel before showing subscriptions
                String channelName = StorageUtil.getStringValue(ServiceUtil.PayloadKeys.ChannelName.getKey());
                if (channelName != null) {
                    channelsList.remove(channelName.trim());
                }

                // remove subscribed channels
                Set<String> currentSubscriptions = StorageUtil.getStringValues(ServiceUtil.PayloadKeys.Subscriptions.getKey());
                if(currentSubscriptions != null && !currentSubscriptions.isEmpty()){
                    channelsList.removeAll(currentSubscriptions);
                }

                if (channelsList == null || channelsList.isEmpty()) {
                    Toast.makeText(ActivityUtil.getMainActivity(), "No channels to subscribe!", Toast.LENGTH_SHORT).show();
                    return;
                }

                final ListView subscriptionList = (ListView) findViewById(R.id.subscription_list);
                final TextView subscriptionText = (TextView) findViewById(R.id.subscription_list_text);
                Set<String> subscriptions = StorageUtil.getStringValues(ServiceUtil.PayloadKeys.Subscriptions.getKey());
                if (subscriptions != null && !subscriptions.isEmpty()) {
                    ArrayAdapter<String> subscriptionsAdapter = new ArrayAdapter<String>(ActivityUtil.getMainActivity(),
                            android.R.layout.simple_list_item_1, android.R.id.text1, new ArrayList<String>(subscriptions));
                    subscriptionList.setAdapter(subscriptionsAdapter);
                    subscriptionList.setVisibility(View.VISIBLE);
                    subscriptionText.setVisibility(View.VISIBLE);
                }



                final ListView channelList = (ListView) findViewById(R.id.channels_list);
                final TextView channelText = (TextView) findViewById(R.id.channels_list_text);

                ArrayAdapter<String> channelListAdapter = new ArrayAdapter<String>(ActivityUtil.getMainActivity(),
                        android.R.layout.simple_list_item_1, android.R.id.text1, channelsList);

                channelList.setAdapter(channelListAdapter);
                channelList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

                channelList.setVisibility(View.VISIBLE);
                channelText.setVisibility(View.VISIBLE);

                channelList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {

                        SparseBooleanArray checked = channelList.getCheckedItemPositions();
                        /*Set<String> newSubscriptions = new HashSet<String>();
                        for (int i = 0; i < checked.size(); i++) {
                            int pos = checked.keyAt(i);
                            if (checked.valueAt(i)) {
                                String itemValue = (String) channelList.getItemAtPosition(position);
                                newSubscriptions.add(itemValue);
                            }
                        }*/

                        String newSubscription = null;
                        for (int i = 0; i < checked.size(); i++) {
                            int pos = checked.keyAt(i);
                            if (checked.valueAt(i)) {
                                newSubscription = (String) channelList.getItemAtPosition(position);
                            }
                        }

                        Subscribe subscribe = new Subscribe(newSubscription);
                        subscribe.run();

                        AbstractResponse response = subscribe.getResponse();
                        if (!response.isSuccess()) {
                            Toast.makeText(ActivityUtil.getMainActivity(), response.getError(), Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Set<String> oldSubscriptions = StorageUtil.getStringValues(ServiceUtil.PayloadKeys.Subscriptions.getKey());
                            if(oldSubscriptions == null){
                                oldSubscriptions = new HashSet<String>(1);
                            }

                            oldSubscriptions.add(newSubscription);
                            StorageUtil.setStringValues(ServiceUtil.PayloadKeys.Subscriptions.getKey(), oldSubscriptions);
                            ArrayAdapter<String> subscriptionsAdapter = new ArrayAdapter<String>(ActivityUtil.getMainActivity(),
                                    android.R.layout.simple_list_item_1, android.R.id.text1, new ArrayList<String>(oldSubscriptions));
                            subscriptionList.setAdapter(subscriptionsAdapter);

                            Toast.makeText(ActivityUtil.getMainActivity(), "Success!", Toast.LENGTH_SHORT).show();

                            /*if (!NotificationHandler.getInstance().hasStarted()) {
                                //NotificationHandler.getInstance().start();
                            }*/

                            subscriptionList.setVisibility(View.VISIBLE);
                            subscriptionText.setVisibility(View.VISIBLE);
                        }
                    }
                });

            }
        });

        Button start = (Button) findViewById(R.id.start_video);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadPublishActivity();
            }
        });
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
            NotificationHandler.getInstance().start();
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
        }
        else{
            Toast.makeText(this, response.getError(), Toast.LENGTH_SHORT).show();
        }
    }
}
