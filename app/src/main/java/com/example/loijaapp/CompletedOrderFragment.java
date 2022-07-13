package com.example.loijaapp;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loijaapp.model.FactoryOrder;
import com.example.loijaapp.model.MyUser;
import com.example.loijaapp.retrofit.FactoryOrderAPI;
import com.example.loijaapp.retrofit.RetrofitService;
import com.example.loijaapp.utils.CurrentSession;
import com.example.loijaapp.utils.Tags;

import java.sql.Date;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CompletedOrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

/*
Fragment que contiene el panel para marcar una comanda como completada.
 */
public class CompletedOrderFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private SharedPreferences sharedPreferences;
    private CurrentSession currentSession;
    private Button btnCompleted, btnBack;
    private TextView textCompletedBy, textFinished;
    private EditText editNotes;
    private FactoryOrder order;

    public CompletedOrderFragment() {
        // Required empty public constructor
    }

    /*
    Método que crea una nueva instancia del fragment con la comanda enviada como parámetro.
     */
    public static CompletedOrderFragment newInstance(FactoryOrder order) {
        CompletedOrderFragment fragment = new CompletedOrderFragment();
        Bundle args = new Bundle();
        args.putSerializable("comanda", order);
        fragment.setArguments(args);
        return fragment;
    }

    /*
    Crea el fragment en la actividad. Carga la comanda recibida en el Bundle.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        sharedPreferences = this.getActivity().getSharedPreferences(Tags.PREFERENCES,
                MODE_PRIVATE);
        currentSession = CurrentSession.build(sharedPreferences);

        Bundle datos = getArguments();
        if (datos != null)
            order = (FactoryOrder) datos.getSerializable("comanda");

    }

    /*
    Carga la interfaz XML asociada al fragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_completed_order, container, false);

        btnBack = view.findViewById(R.id.fragment_btnBack);
        btnCompleted = view.findViewById(R.id.fragment_btnComfirm);
        editNotes = view.findViewById(R.id.fragment_editNotes);
        textCompletedBy = view.findViewById(R.id.fragment_textCompletedBy);
        textFinished = view.findViewById(R.id.fragment_textFinished);

        textFinished.setText(obtenerFechaActual().toString());
        textCompletedBy.setText(currentSession.getUsername());

        btnBack.setOnClickListener(this);
        btnCompleted.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fragment_btnBack:
                /*MaterialButton btnMarkCompleted = getActivity().findViewById(R.id.coForm_btnMarkCompleted);
                View fragment = getActivity().findViewById(R.id.coForm_fragmentContainer);
                fragment.setEnabled(false);
                fragment.setVisibility(View.INVISIBLE);
                btnMarkCompleted.setEnabled(true);*/
                getActivity().finish();
                break;
            case R.id.fragment_btnComfirm:
                confirmarComanda();
                break;
        }
    }

    /*
    Método que envia la peticion HTTP con la comanda marcada como completada y con los datos de
    fecha de finalizacion, usuario que la ha finalizado y las notas escritas.
     */
    public void confirmarComanda() {
        if (order != null) {
            FactoryOrderAPI orderAPI = RetrofitService.getInstance().create(FactoryOrderAPI.class);
            MyUser completedBy = new MyUser(null, null, textCompletedBy.getText().toString(), null);
            order.setCompleted(true);
            order.setCompletedBy(completedBy);
            order.setFinished(obtenerFechaActual());
            order.setNotes(editNotes.getText().toString());
            orderAPI.completed(currentSession.getCookie(), order)
                    .enqueue(new Callback<FactoryOrder>() {
                        @Override
                        public void onResponse(Call<FactoryOrder> call, Response<FactoryOrder> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(getActivity().getBaseContext(), "Comanda completada con éxito", Toast.LENGTH_SHORT).show();
                                getActivity().onBackPressed();
                            }
                        }

                        @Override
                        public void onFailure(Call<FactoryOrder> call, Throwable t) {
                            Toast.makeText(getActivity().getBaseContext(), getString(R.string.main_error_conexion_servidor), Toast.LENGTH_SHORT).show();
                            Logger.getLogger(CompletedOrderFragment.class.getName()).log(Level.SEVERE, "Fallo al establecer conexión con el servidor", t);
                        }
                    });
        }

    }

    /*
    Obtiene los datos de la fecha actual del sistema.
     */
    public Date obtenerFechaActual() {
        return new Date(Calendar.getInstance().getTime().getTime());
    }
}