package com.example.loijaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loijaapp.retrofit.HomeAPI;
import com.example.loijaapp.retrofit.RetrofitService;
import com.example.loijaapp.utils.Tags;
import com.example.loijaapp.utils.CurrentSession;

import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*
Actividad principal de la aplicacion.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView textBienvenido, textRoles;
    private Button btnUsuarios, btnComandas, btnPreferencias, btnSesion;
    private CurrentSession currentSession;
    private SharedPreferences sharedPreferences;
    private HomeAPI homeAPI;

    /*
    Método que crea la actividad principal. Inicia las vistas, carga las preferencias compartidas y
    valída la sesión actual. Si la sesion no existe o es incorrecta salta a la actividad de login
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inicializeComponenents();
        sharedPreferences = this.getSharedPreferences(Tags.PREFERENCES,
                MODE_PRIVATE);
        currentSession = CurrentSession.build(sharedPreferences);

        if (currentSession == null) {
            aLoginActivity(null);
        }
        else {
            validarConexion();
        }

    }

    /*
    Método con los eventos por cada boton
     */
    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.main_btnComandas:
                intent = new Intent(this, OrderList.class);
                startActivity(intent);
                break;
            case R.id.main_btnUsuarios:
                intent = new Intent(this, UserList.class);
                startActivity(intent);
                break;
            case R.id.main_btnSesion:
                logout();
                break;
            case R.id.main_btnPreferencias:
                /*intent = new Intent(this, Preferences.class);
                startActivity(intent);*/
                break;
        }
    }

    /*
    Método que inicia las vistas en la interfaz
     */
    public void inicializeComponenents() {
        textBienvenido = findViewById(R.id.main_textBienvenido);
        textBienvenido.setText(getString(R.string.main_mensaje_bienvenida));
        textRoles = findViewById(R.id.main_textRoles);
        textRoles.setText("");
        btnComandas = findViewById(R.id.main_btnComandas);
        btnComandas.setEnabled(false);
        btnUsuarios = findViewById(R.id.main_btnUsuarios);
        btnUsuarios.setEnabled(false);
        btnPreferencias = findViewById(R.id.main_btnPreferencias);
        btnPreferencias.setEnabled(false);
        btnSesion = findViewById(R.id.main_btnSesion);
        btnSesion.setEnabled(true);
        btnSesion.setText(getText(R.string.main_btn_iniciar_sesion));
        btnSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aLoginActivity(null);
            }
        });
    }

    /*
    Crea un intent para ir a la pantalla de login. Puede recibir un mensaje de información
    para el usuario que se mostrará en un Toast
     */
    public void aLoginActivity(String mensaje) {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.putExtra("mensaje", mensaje);
        startActivity(intent);
    }

    /*
    Prepara la actividad una vez la sesión ha sido validada
     */
    public void prepararActivity() {
        textBienvenido.setText(String.format(getString(R.string.main_mensaje_username), currentSession.getUsername()));
        textRoles.setText(currentSession.getRoles().toString());
        if (currentSession.isAdmin()) {
            btnUsuarios.setEnabled(true);
            btnUsuarios.setOnClickListener(this::onClick);
        }
        btnComandas.setEnabled(true);
        btnComandas.setOnClickListener(this::onClick);
        btnSesion.setText("Cerrar sesión");
        btnSesion.setEnabled(true);
        btnSesion.setOnClickListener(this::onClick);
        btnPreferencias.setEnabled(false);
    }

    /*
    Envia una petición HTTP para comprobar si la sesion actual sigue vigente en el servidor.
     */
    private void validarConexion() {
        if (homeAPI == null) {
            homeAPI = RetrofitService.getInstance().create(HomeAPI.class);
        }
        homeAPI.validate(currentSession.getCookie())
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.isSuccessful()) {
                            prepararActivity();
                        }
                        else
                            aLoginActivity("Vuelve a iniciar sesión");
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(getBaseContext(), getString(R.string.main_error_conexion_servidor),
                                Toast.LENGTH_SHORT).show();
                        Logger.getLogger(this.getClass().getName()).log(Level.SEVERE,
                                "Fallo al establecer conexión con el servidor", t);
                    }
                });
    }

    /*
    Envia la petición HTTP para cerrar la sesión en el servidor. Tambien borra las sharedPreferences
    de la app para eliminar los datos de la sesión actual.
     */
    private void logout() {
        if (homeAPI == null) {
            homeAPI = RetrofitService.getInstance(getBaseContext()).create(HomeAPI.class);
        }
        homeAPI.logout(currentSession.getCookie())
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.isSuccessful()) {
                            sharedPreferences.edit().clear();
                            sharedPreferences.edit().clear().commit();
                            finish();
                            aLoginActivity(getString(R.string.main_sesion_cerrada));
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(MainActivity.this, getString(R.string.main_error_conexion_servidor), Toast.LENGTH_SHORT).show();
                        Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, "Fallo al establecer conexión con el servidor", t);
                    }
                });
    }

}