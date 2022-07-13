package com.example.loijaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.loijaapp.model.FactoryOrder;
import com.example.loijaapp.model.Material;
import com.example.loijaapp.model.MyUser;
import com.example.loijaapp.model.Product;
import com.example.loijaapp.retrofit.FactoryOrderAPI;
import com.example.loijaapp.retrofit.MaterialAPI;
import com.example.loijaapp.retrofit.ProductAPI;
import com.example.loijaapp.retrofit.RetrofitService;
import com.example.loijaapp.utils.CurrentSession;
import com.example.loijaapp.utils.DatePickerFragment;
import com.example.loijaapp.utils.Tags;
import com.google.android.material.button.MaterialButton;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*
Actividad con el formulario para crear una nueva comanda.
 */
public class NewOrderForm extends AppCompatActivity {

    private CurrentSession currentSession;
    private SharedPreferences sharedPreferences;
    private Spinner spinnerProductos, spinnerMateriales;
    private EditText editCantidad, editDeadline;
    private MaterialButton btnSaveOrder;
    private List<Product> productos;
    private List<Material> materiales;
    private ArrayAdapter arrayProductos, arrayMateriales;
    private ProductAPI productAPI;
    private MaterialAPI materialAPI;
    private FactoryOrderAPI orderAPI;

    /*
    Crea la actividad. La actividad llama a obtenerProductos() para cargar los datos de los
    productos en el Spinner de seleccion.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order_form);

        sharedPreferences = this.getSharedPreferences(Tags.PREFERENCES,
                MODE_PRIVATE);
        currentSession = CurrentSession.build(sharedPreferences);

        if (productAPI == null)
            productAPI = RetrofitService.getInstance().create(ProductAPI.class);

        spinnerMateriales = findViewById(R.id.oForm_spinnerMateriales);
        spinnerProductos = findViewById(R.id.oForm_spinnerProductos);
        btnSaveOrder = findViewById(R.id.oForm_btnSaveOrder);
        editCantidad = findViewById(R.id.oForm_editCantidad);
        editDeadline = findViewById(R.id.oForm_editDeadline);

        editDeadline.setInputType(InputType.TYPE_NULL);
        editCantidad.setInputType(InputType.TYPE_CLASS_NUMBER);
        editDeadline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        obtenerListadoProductos();

        btnSaveOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardarComanda();
            }
        });

    }

    /*
    Muestra un fragment con un selector de fecha.
     */
    private void showDatePickerDialog() {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 because January is zero
                final String selectedDate = day + " / " + (month+1) + " / " + year;
                editDeadline.setText(selectedDate);
            }
        });

        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    /*
    Envia la peticion HTTP para obtener los productos almacenados en la base de datos.
     */
    private void obtenerListadoProductos() {
        productAPI.listProducts(currentSession.getCookie())
                .enqueue(new Callback<List<Product>>() {
                    @Override
                    public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                        if (response.isSuccessful()) {
                            productos = response.body();
                            montarSpinnerProductos();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Product>> call, Throwable t) {
                        Toast.makeText(getBaseContext(), getString(R.string.main_error_conexion_servidor),
                                Toast.LENGTH_SHORT).show();
                        Logger.getLogger(this.getClass().getName()).log(Level.SEVERE,
                                "Fallo al conectar con el servidor", t);
                    }
                });
    }

    /*
    Método que monta el array en el spinner de selección de producto base.
    Una vez obtenidos los productos, se monta el spinner con los datos de los productos. Cada vez
    que se produce una selección, se llama al método obtenerListadoMateriales(position) para recibir
    los materiales pertenecientes al producto obtenido.
     */
    private void montarSpinnerProductos() {
        List<String> spinnerOpciones = new ArrayList<>();
        for (Product producto : productos) {
            spinnerOpciones.add(producto.getName());
        }

        arrayProductos = new ArrayAdapter(getBaseContext(), android.R.layout.simple_list_item_1,
                spinnerOpciones);
        spinnerProductos.setAdapter(arrayProductos);
        spinnerProductos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                obtenerListadoMateriales(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    /*
    Envia la peticion HTTP para recibir los materiales que pertenecen al producto seleccionado.
    Una vez obtenidos los datos, se llama a montarSpinnerMateriales() para montar el spinner de
    selección de materiales.
     */
    private void obtenerListadoMateriales(int position) {
        Product producto = productos.get(position);
        if (materialAPI == null)
            materialAPI = RetrofitService.getInstance().create(MaterialAPI.class);

        materialAPI.listMaterialsByProduct(currentSession.getCookie(), producto)
                .enqueue(new Callback<List<Material>>() {
                    @Override
                    public void onResponse(Call<List<Material>> call, Response<List<Material>> response) {
                        if (response.isSuccessful()) {
                            materiales = response.body();
                            montarSpinnerMateriales();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Material>> call, Throwable t) {
                        Toast.makeText(getBaseContext(), getString(R.string.main_error_conexion_servidor),
                                Toast.LENGTH_SHORT).show();
                        Logger.getLogger(this.getClass().getName()).log(Level.SEVERE,
                                "Fallo al conectar con el servidor", t);
                    }
                });

    }

    /*
    Método que monta el array en el spinner de selección de material a fabricar.
     */
    private void montarSpinnerMateriales() {
        List<String> spinnerOpciones = new ArrayList<>();
        for (Material material : materiales) {
            spinnerOpciones.add(material.getName());
        }

        arrayMateriales = new ArrayAdapter(getBaseContext(), android.R.layout.simple_list_item_1,
                spinnerOpciones);
        spinnerMateriales.setAdapter(arrayMateriales);
        spinnerMateriales.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    /*
    Envia la peticion HTTP para guardar la nueva comanda en el servidor.
    Recibe de las vistas las variables necesarias para crear una nueva comanda sin completar.
     */
    private void guardarComanda() {
        try {
            Material material = materiales.get(spinnerMateriales.getSelectedItemPosition());
            int amount = Integer.valueOf(editCantidad.getText().toString());
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd / MM / yyyy");
            Date deadline = new Date(dateFormat.parse(editDeadline.getText().toString()).getTime());
            System.out.println(deadline);
            MyUser createdBy = new MyUser(null, null, currentSession.getUsername(), null);
            FactoryOrder comanda = new FactoryOrder(createdBy, material, amount, deadline);
            System.out.println(comanda.getCreated());

            if (orderAPI == null)
                orderAPI = RetrofitService.getInstance().create(FactoryOrderAPI.class);

            orderAPI.save(currentSession.getCookie(), comanda)
                    .enqueue(new Callback<FactoryOrder>() {
                        @Override
                        public void onResponse(Call<FactoryOrder> call, Response<FactoryOrder> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(NewOrderForm.this, "Comanda añadida", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                            else {
                                Toast.makeText(NewOrderForm.this, "Fallo al añadir", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<FactoryOrder> call, Throwable t) {
                            Toast.makeText(getBaseContext(), getString(R.string.main_error_conexion_servidor),
                                    Toast.LENGTH_SHORT).show();
                            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE,
                                    "Fallo al conectar con el servidor", t);
                        }
                    });
        } catch (ParseException | NumberFormatException e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

}