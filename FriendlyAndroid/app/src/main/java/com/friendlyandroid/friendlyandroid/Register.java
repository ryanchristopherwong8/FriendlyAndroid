package com.friendlyandroid.friendlyandroid;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class Register extends AppCompatActivity implements View.OnClickListener {

    Button bRegister;
    EditText etFirstName, etLastName, etUsername, etPassword;
    TextView loginLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etFirstName = (EditText) findViewById(R.id.etFirstName);
        etLastName = (EditText) findViewById(R.id.etLastName);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        bRegister = (Button) findViewById(R.id.bRegister);
        loginLink = (TextView) findViewById(R.id.tvLoginLink);

        bRegister.setOnClickListener(this);
        loginLink.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.bRegister:
                sendRegister();
                break;
            case R.id.tvLoginLink:
                finish();
                break;
        }
    }

    public void sendRegister() {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://murmuring-brushlands-62477.herokuapp.com/user/create/" + etUsername.getText() + "/" +
                etPassword.getText() + "/" +
                etFirstName.getText() + "/" +
                etLastName.getText();
        System.out.println(url);

        // Request a string response from the provided URL.
        JsonObjectRequest JSORequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("Type").equals("201")) {
                                System.out.println("SUCCESS");
                                successDialog();
                            } else {
                                System.out.println("FAIL");
                                failureDialog(response.getString("Message"));
                            }
                        }
                        catch (JSONException e) {
                            System.out.println("exception");
                        }
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

    private void successDialog() {
        AlertDialog ad = new AlertDialog.Builder(this)
                .setTitle("Success!")
                .setMessage("You can now login.")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void failureDialog(String error) {
        AlertDialog ad = new AlertDialog.Builder(this)
                .setTitle("Error!")
                .setMessage(error)
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
