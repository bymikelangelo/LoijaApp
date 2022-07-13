package com.example.loijaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.example.loijaapp.retrofit.RetrofitService;
import com.example.loijaapp.utils.Tags;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

/*
Actividad que permite escribir una IP y un puerto para acceder al servidor.
NO ESTA IMPLEMENTADA.
 */
public class Preferences extends AppCompatActivity implements TextWatcher {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private TextInputEditText textIPAddress, textPort;
    private MaterialButton btnSavePreferences;
    private String IPAddress, port;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        btnSavePreferences = findViewById(R.id.pref_btnSavePreferences);
        textIPAddress = findViewById(R.id.pref_textIPAdress);
        textPort = findViewById(R.id.pref_textPort);

        textPort.addTextChangedListener(this);
        textIPAddress.addTextChangedListener(this);

        sharedPreferences = this.getSharedPreferences(Tags.PREFERENCES,
                MODE_PRIVATE);
        editor = sharedPreferences.edit();

        IPAddress = sharedPreferences.getString(Tags.IP_ADDRESS, "localhost");
        port = sharedPreferences.getString(Tags.PORT, "8080");

        textIPAddress.setText(IPAddress);
        textPort.setText(port);

        btnSavePreferences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putString(Tags.IP_ADDRESS, textIPAddress.getText().toString());
                editor.putString(Tags.PORT, textPort.getText().toString());
                editor.commit();
                RetrofitService.delete();
                finish();
            }
        });

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (textIPAddress.getText().length() == 0 | textPort.getText().length() == 0)
            btnSavePreferences.setEnabled(false);
        else
            btnSavePreferences.setEnabled(true);
    }
}