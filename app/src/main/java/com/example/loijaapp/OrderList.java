package com.example.loijaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.loijaapp.adapter.FactoryOrderAdapter;
import com.example.loijaapp.model.FactoryOrder;
import com.example.loijaapp.retrofit.FactoryOrderAPI;
import com.example.loijaapp.retrofit.RetrofitService;
import com.example.loijaapp.utils.ClickListener;
import com.example.loijaapp.utils.CurrentSession;
import com.example.loijaapp.utils.Tags;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*
Actividad que muestra las comandas almacenadas en el servidor en forma de lista.
 */
public class OrderList extends AppCompatActivity {

    private RecyclerView orderList;
    private FloatingActionButton fabAddOrder;
    private CurrentSession currentSession;
    private SharedPreferences sharedPreferences;
    private FactoryOrderAPI orderAPI;

    /*
    Crea la actividad. Llama al método obtenerComandas() para crear el RecycleView con la vista.
    El boton de anádir nuevas comandas solo está disponible en usuarios ADMIN o MANAGER
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        sharedPreferences = this.getSharedPreferences(Tags.PREFERENCES,
                MODE_PRIVATE);
        currentSession = CurrentSession.build(sharedPreferences);

        fabAddOrder = findViewById(R.id.oList_fabAddOrder);
        orderList = findViewById(R.id.oList_orderList);

        orderList.setLayoutManager(new LinearLayoutManager(this));

        fabAddOrder.setEnabled(false);
        if (currentSession.isManager() | currentSession.isAdmin()) {
            fabAddOrder.setEnabled(true);
            fabAddOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getBaseContext(), NewOrderForm.class);
                    startActivity(intent);
                }
            });
        }
        else
            fabAddOrder.setEnabled(false);

        obtenerComandas();

    }

    /*
    Método que es llamado cuando la actividad es mostrada de nuevo en la pantalla desde un estado
    de segundo plano. Actualiza los datos mostrados en en RecycleView.
     */
    @Override
    protected void onResume() {
        super.onResume();
        obtenerComandas();
    }

    /*
    Envia la peticion HTTP para obtener las comandas almacenadas en el servidor.
     */
    private void obtenerComandas() {
        if (orderAPI == null)
            orderAPI = RetrofitService.getInstance().create(FactoryOrderAPI.class);

        orderAPI.listOrders(currentSession.getCookie())
                .enqueue(new Callback<List<FactoryOrder>>() {
                    @Override
                    public void onResponse(Call<List<FactoryOrder>> call, Response<List<FactoryOrder>> response) {
                        if (response.isSuccessful()) {
                            List<FactoryOrder> orders = response.body();
                            FactoryOrderAdapter orderAdapter = new FactoryOrderAdapter(orders, getBaseContext());
                            orderList.setAdapter(orderAdapter);
                            orderAdapter.setOnItemClickListener(new ClickListener() {
                                @Override
                                public void onItemClick(int position, View v) {
                                    modificarComanda(orders.get(position));
                                }
                            });
                        }
                        else {
                            Toast.makeText(OrderList.this, response.message(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<FactoryOrder>> call, Throwable t) {
                        Toast.makeText(OrderList.this, getString(R.string.main_error_conexion_servidor), Toast.LENGTH_SHORT).show();
                        Logger.getLogger(OrderList.class.getName()).log(Level.SEVERE, "Fallo al establecer conexión con el servidor", t);
                    }
                });
    }

    /*
    Crea un intent para ir a la actividad de formulario de comanda ya creada para marcarla como
    completada. La comanda es seleccionada en el RecycleView.
     */
    private void modificarComanda(FactoryOrder order) {
        Intent intent = new Intent(this.getBaseContext(), CreatedOrderForm.class);
        intent.putExtra("comanda", order);
        startActivity(intent);
    }
}