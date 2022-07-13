package com.example.loijaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loijaapp.model.FactoryOrder;
import com.google.android.material.button.MaterialButton;

/*
Actividad que muestra el formulario de una comanda ya creada.
Debe recibir una comanda (FactoryOrder) desde un bundle de datos.
 */
public class CreatedOrderForm extends AppCompatActivity {

    private TextView textId, textCreated, textCreatedBy, textMaterial, textAmount, textDeadline,
        textCompleted, textFinished, textCompletedBy, textNotes;
    private MaterialButton btnMarkCompleted;
    private FactoryOrder order;
    private View fragmentContainer;

    /*
    Crea la actividad. Debe recibir una comanda (FactoryOrder) desde un bundle de datos.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_created_order_form);

        Intent intent = getIntent();
        order = (FactoryOrder) intent.getSerializableExtra("comanda");

        if (order != null) {
            inicializeComponents(order.isCompleted());
            cargarDatos();
        }
        else {
            Toast.makeText(this, "Fallo al cargar los datos", Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    /*
    Metodo que inica los compoenentes.
    Verifica si la interfaz está mostrando una comanda ya finalizada. Si ya está finalizada,
    bloquea el boton de marcar como completada para evitar abrir el fragment.
     */
    private void inicializeComponents(Boolean comandaCompletada) {
        textId = findViewById(R.id.coForm_textId);
        textCreated = findViewById(R.id.coForm_textCreated);
        textCreatedBy = findViewById(R.id.coForm_textCreatedBy);
        textMaterial = findViewById(R.id.coForm_textMaterial);
        textAmount = findViewById(R.id.coForm_textAmount);
        textDeadline = findViewById(R.id.coForm_textDeadline);
        textCompleted = findViewById(R.id.coForm_textCompleted);
        textCompletedBy = findViewById(R.id.coForm_textCompletedBy);
        textFinished = findViewById(R.id.coForm_textFinished);
        textCompletedBy = findViewById(R.id.coForm_textCompletedBy);
        textNotes = findViewById(R.id.coForm_textNotes);
        fragmentContainer = findViewById(R.id.coForm_fragmentContainer);
        fragmentContainer.setVisibility(View.INVISIBLE);

        btnMarkCompleted = findViewById(R.id.coForm_btnMarkCompleted);

        if (comandaCompletada) {
            btnMarkCompleted.setEnabled(false);
            btnMarkCompleted.setVisibility(View.INVISIBLE);
            textCompleted.setText("Finalizada");
            textCompletedBy.setText(String.valueOf(order.getCompletedBy().getUsername()));
            textFinished.setText(order.getFinished().toString());
            textNotes.setText(String.valueOf(order.getNotes()));
        }
        else {
            textCompleted.setText("Sin finalizar");
            textFinished.setEnabled(false);
            textCompletedBy.setEnabled(false);
            textNotes.setEnabled(false);
            btnMarkCompleted.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    aCompletedOrderFragment();
                }
            });
        }
    }

    /*
    Método que muestra los datos de la comanda en las vistas
     */
    private void cargarDatos() {
        textId.setText(String.valueOf(order.getId()));
        textCreated.setText(String.valueOf(order.getCreated()));
        textCreatedBy.setText(String.valueOf(order.getCreatedBy().getUsername()));
        textMaterial.setText(String.valueOf(order.getMaterial().getName()));
        textAmount.setText(String.valueOf(order.getAmount()));
        textDeadline.setText(String.valueOf(order.getDeadline()));
    }

    /*
    Método que muestra el fragment para marcar como completada la comanda seleccionada.
     */
    private void aCompletedOrderFragment() {
        CompletedOrderFragment fragment = CompletedOrderFragment.newInstance(order);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.coForm_fragmentContainer, fragment);
        transaction.commit();
        if (fragmentContainer.isShown())
            fragmentContainer.setVisibility(View.INVISIBLE);
        else {
            fragmentContainer.setVisibility(View.VISIBLE);
            btnMarkCompleted.setEnabled(false);
        }
    }
}