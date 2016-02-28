package com.friendlyandroid.friendlyandroid;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;


public class MainActivity extends FragmentActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private String username;
    private String password;
    Button bLogout;

    GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Intent loginIntent = getIntent(); // gets the previously created intent
        this.username = loginIntent.getExtras().get("username").toString();
        this.password = loginIntent.getExtras().get("password").toString();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bLogout = (Button) findViewById(R.id.bLogout);
        bLogout.setOnClickListener(this);

        if (mGoogleApiClient == null) {
            System.out.println("setting up GoogleApiClient");
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        findFriends();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.bLogout:
                sendLogout();
                finish();
                break;
        }
    }

    public void sendLogout() {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://murmuring-brushlands-62477.herokuapp.com/user/logout/" + this.username + "/" +
                this.password;
        System.out.println(url);

        // Request a string response from the provided URL.
        JsonObjectRequest JSORequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("THAT DIDN'T WORK");
            }
        });
        // Add the request to the RequestQueue.
        queue.add(JSORequest);
    }

    public void findFriends() {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
//        String url = "api.androidhive.info/contacts/";
        String url = "https://murmuring-brushlands-62477.herokuapp.com/user/index/" + this.username + "/" +
                this.password;
        System.out.println(url);

        // Request a string response from the provided URL.
        JsonArrayRequest JSARequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        String hello;
//                        renderListView(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("THAT DIDN'T WORK");
            }
        });
        // Add the request to the RequestQueue.
        queue.add(JSARequest);
    }

    private void renderListView(JSONArray array) {
        try {
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);

                LinearLayout LL = new LinearLayout(this);
                LL.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams LLparam = new LinearLayout.LayoutParams(
                        0,
                        ViewGroup.LayoutParams.MATCH_PARENT, 1.0f);
                LL.setLayoutParams(LLparam);

                TextView tvName = new TextView(this);
                LinearLayout.LayoutParams tvNameParam = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        0, 1.0f);
                tvName.setLayoutParams(tvNameParam);
                tvName.setText(object.getString("firstname") + " " + object.getString("lastname"));

                TextView tvDistance = new TextView(this);
                LinearLayout.LayoutParams tvDistanceParam = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        0, 1.0f);
                tvName.setLayoutParams(tvDistanceParam);
                tvName.setText("5km");

                LL.addView(tvName);
                LL.addView(tvDistance);

                LinearLayout mainListViewLayout = (LinearLayout) findViewById(R.id.mainListView);
                mainListViewLayout.addView(LL);
            }
        } catch (JSONException e) {

        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return  ;
        }

        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
//            mLatitudeText.setText(String.valueOf(mLastLocation.getLatitude()));
//            mLongitudeText.setText(String.valueOf(mLastLocation.getLongitude()));
        }
        System.out.println("Lat: " + String.valueOf(mLastLocation.getLatitude()));
        System.out.println("Lon: " + String.valueOf(mLastLocation.getLongitude()));
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }
}
