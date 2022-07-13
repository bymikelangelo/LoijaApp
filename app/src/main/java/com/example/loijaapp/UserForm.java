package com.example.loijaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.loijaapp.model.MyUser;
import com.example.loijaapp.retrofit.RetrofitService;
import com.example.loijaapp.retrofit.UserAPI;
import com.example.loijaapp.utils.CurrentSession;
import com.example.loijaapp.utils.Tags;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/*
Actividad que contiene el formulario de usuario.
 */
public class UserForm extends AppCompatActivity {

    private TextInputEditText textFirstname, textSurname, textUsername, textPassword;
    private MaterialButton btnSaveUser;
    private SharedPreferences sharedPreferences;
    private CurrentSession currentSession;
    private UserAPI userAPI;
    private MyUser user;

    /*
    Crea la actividad. Puede recibir un Bundle con los datos del usuario seleccionado en la
    actividad UserList. Si se ha recibido un usuario, carga los datos en las vistas.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_form);

        sharedPreferences = this.getSharedPreferences(Tags.PREFERENCES,
                MODE_PRIVATE);
        currentSession = CurrentSession.build(sharedPreferences);

        textFirstname = findViewById(R.id.textFirstname);
        textSurname = findViewById(R.id.textSurname);
        textUsername = findViewById(R.id.textUsername);
        textPassword = findViewById(R.id.textPassword);
        btnSaveUser = findViewById(R.id.btnSaveUser);

        Intent intent = getIntent();
        user = (MyUser) intent.getSerializableExtra("usuario");

        if (user != null) {
            textFirstname.setText(user.getFirstname());
            textSurname.setText(user.getSurname());
            textUsername.setText(user.getUsername());
            textPassword.setText(user.getPassword());
            this.setTitle("Modificar usuario");
            btnSaveUser.setText("Guardar cambios");
        }

        btnSaveUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardarUsuario();
            }
        });

    }

    /*
    Envia la peticion HTTP para guardar/modificar el usuario. El método modifica con los nuevos
    parámetros el usuario recibido del Bundle. Si no existe usuario recibido, crea uno nuevo que
    será almacenado como un nuevo registro en la base de datos
     */
    private void guardarUsuario() {
        String firstname = String.valueOf(textFirstname.getText());
        String surname = String.valueOf(textSurname.getText());
        String username = String.valueOf(textUsername.getText());
        String password = String.valueOf(textPassword.getText());

        if (user != null) {
            user.setFirstname(firstname);
            user.setSurname(surname);
            user.setPassword(password);
            user.setUsername(username);
        }
        else {
            user = new MyUser(firstname, surname, username, password);
        }

        if (userAPI == null)
            userAPI = RetrofitService.getInstance().create(UserAPI.class);

        userAPI.save(currentSession.getCookie(), user)
                .enqueue(new Callback<MyUser>() {
                    @Override
                    public void onResponse(Call<MyUser> call, Response<MyUser> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(UserForm.this, " Usuario guardado", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else
                            Toast.makeText(UserForm.this, "Fallo al guardar el usuario", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<MyUser> call, Throwable t) {
                        Toast.makeText(UserForm.this, getString(R.string.main_error_conexion_servidor), Toast.LENGTH_SHORT).show();
                        Logger.getLogger(UserForm.class.getName()).log(Level.SEVERE, "Fallo al establecer conexión con el servidor", t);
                    }
                });
    }

}