package com.example.loijaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.loijaapp.adapter.UserAdapter;
import com.example.loijaapp.model.MyUser;
import com.example.loijaapp.retrofit.RetrofitService;
import com.example.loijaapp.retrofit.UserAPI;
import com.example.loijaapp.utils.ClickListener;
import com.example.loijaapp.utils.Tags;
import com.example.loijaapp.utils.CurrentSession;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*
Actividad que muestra los usuarios almacenados en el servidor en forma de lista. Solo es accesible
por un usuario administrador
 */
public class UserList extends AppCompatActivity {

    private RecyclerView userList;
    private FloatingActionButton fabAddUser;
    private CurrentSession currentSession;
    private SharedPreferences sharedPreferences;
    private UserAPI userAPI;

    /*
    Crea la actividad. Llama al método obtenerUsuarios() para crear el RecycleView con la vista
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        sharedPreferences = this.getSharedPreferences(Tags.PREFERENCES,
                MODE_PRIVATE);
        currentSession = CurrentSession.build(sharedPreferences);

        fabAddUser = findViewById(R.id.uList_fabAddUser);
        userList = findViewById(R.id.uList_userList);

        userList.setLayoutManager(new LinearLayoutManager(this));
        fabAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), UserForm.class);
                startActivity(intent);
            }
        });

        obtenerUsuarios();
    }

    /*
    Método que es llamado cuando la actividad es mostrada de nuevo en la pantalla desde un estado
    de segundo plano. Actualiza los datos mostrados en en RecycleView.
     */
    @Override
    protected void onResume() {
        super.onResume();
        obtenerUsuarios();
    }

    /*
    Envia la peticion HTTP para obtener los usuarios almacenados en el servidor.
     */
    private void obtenerUsuarios() {
        if (userAPI == null)
            userAPI = RetrofitService.getInstance().create(UserAPI.class);

        userAPI.listUsers(currentSession.getCookie())
                .enqueue(new Callback<List<MyUser>>() {
                    @Override
                    public void onResponse(Call<List<MyUser>> call, Response<List<MyUser>> response) {
                        if (response.isSuccessful()) {
                            List<MyUser> users = response.body();
                            UserAdapter userAdapter = new UserAdapter(users, getBaseContext());
                            userList.setAdapter(userAdapter);
                            userAdapter.setOnItemClickListener(new ClickListener() {
                                @Override
                                public void onItemClick(int position, View v) {
                                    modificarUsuario(users.get(position));
                                }
                            });
                        }
                        else {
                            Toast.makeText(UserList.this, response.message(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<List<MyUser>> call, Throwable t) {
                        Toast.makeText(UserList.this, getString(R.string.main_error_conexion_servidor), Toast.LENGTH_SHORT).show();
                        Logger.getLogger(UserList.class.getName()).log(Level.SEVERE, "Fallo al establecer conexión con el servidor", t);
                    }
                });
    }

    /*
    Crea un intent para ir a la actividad de formulario de usuario para modificar el usuario
    existente seleccionado en el RecycleView.
     */
    private void modificarUsuario(MyUser user) {
        Intent intent = new Intent(this.getBaseContext(), UserForm.class);
        intent.putExtra("usuario", user);
        startActivity(intent);
    }
}
