package com.example.loijaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.loijaapp.model.MyUser;
import com.example.loijaapp.retrofit.HomeAPI;
import com.example.loijaapp.retrofit.RetrofitService;
import com.example.loijaapp.utils.Tags;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*
Actividad que contiene las vistas y lógica para iniciar sesion en la aplicación.
 */
public class LoginActivity extends AppCompatActivity implements TextWatcher{

    private MaterialButton btnLogIn;
    private TextInputEditText editUsername, editPassword;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private HomeAPI homeAPI;
    private Intent intent;

    /*
    Crea la actividad. Puede recibir desde Bundle un String que mostrará en Toast.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences = this.getSharedPreferences(Tags.PREFERENCES, MODE_PRIVATE);
        editor = sharedPreferences.edit();

        btnLogIn = findViewById(R.id.main_btnLogIn);
        editUsername = findViewById(R.id.main_editUsername);
        editPassword = findViewById(R.id.main_editPassword);

        intent = getIntent();
        String mensaje = intent.getStringExtra("mensaje");
        if (mensaje != null) {
            Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
        }

        btnLogIn.setEnabled(false);
        editUsername.addTextChangedListener(this);
        editPassword.addTextChangedListener(this);
        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

    }

    /*
    Metodos del TextWatcher que verifican si hay habilitan o desabilitan el boton de login si
    no hay carácteres escritos en los EditText.
     */
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (editPassword.getText().length() == 0 | editUsername.getText().length() == 0)
            btnLogIn.setEnabled(false);
        else
            btnLogIn.setEnabled(true);
    }

    /*
    Lleva a cabo la petición HTTP para iniciar la sesión. Si la sesión es correcta escribe los datos
    de la sesion actual en sharedPreferences.
     */
    private void login() {
        if (homeAPI == null)
            homeAPI = RetrofitService.getInstance().create(HomeAPI.class);

        String username = String.valueOf(editUsername.getText());
        String password = String.valueOf(editPassword.getText());

        String authHeader = "Basic " + Base64.encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8), Base64.NO_WRAP);

        homeAPI.login(authHeader, username)
                .enqueue(new Callback<MyUser>() {
                    @Override
                    public void onResponse(Call<MyUser> call, Response<MyUser> response) {
                        if (response.isSuccessful()) {
                            editor.putString(Tags.USERNAME, response.body().getUsername());
                            editor.putString(Tags.PASSWORD, response.body().getPassword());
                            editor.putStringSet(Tags.USER_ROLES, response.body().getRolNames());
                            editor.putString(Tags.FIRSTNAME, response.body().getFirstname());
                            editor.putString(Tags.SURNAME, response.body().getSurname());
                            editor.putString(Tags.COOKIE, response.headers().get("Set-Cookie"));
                            editor.commit();
                            intent = new Intent(getBaseContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Toast.makeText(LoginActivity.this, getString(R.string.login_error_credenciales), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<MyUser> call, Throwable t) {
                        Toast.makeText(LoginActivity.this, getString(R.string.main_error_conexion_servidor), Toast.LENGTH_SHORT).show();
                        Logger.getLogger(LoginActivity.class.getName()).log(Level.SEVERE, "Fallo al establecer conexión con el servidor", t);
                    }
                });
    }
}