package com.panteranegra.tudoka;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.panteranegra.tudoka.Interface.IfFirebaseLoadDone;
import com.panteranegra.tudoka.Model.Cliente;
import com.panteranegra.tudoka.Model.Pieza;
import com.panteranegra.tudoka.Model.Proyecto;
import com.panteranegra.tudoka.Model.ReporteDano;

import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;
import java.util.List;

public class DatosDano extends AppCompatActivity  implements IfFirebaseLoadDone {

    SearchableSpinner searchableSpinnerNomCliente, searchableSpinnerNoCliente, searchableSpinnerNomProyecto, searchableSpinnerNoProyecto;

    DatabaseReference clientesRef;
    private Button guardarBtn;
    private ReporteDano reporte;
    private ArrayList<Cliente> alCliente;
    private ArrayList<Proyecto> alProyectos;
    ProgressDialog progress;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_dano);

        reporte = new ReporteDano();
        reporte.setAlPiezas(new ArrayList<Pieza>());

        progress= new ProgressDialog(this);

        progress.setTitle("Cargando");

        progress.show();

        alCliente = new ArrayList<>();
        alProyectos = new ArrayList<>();

        alCliente.add(new Cliente("","Selecciona...","Selecciona...",""));
        alProyectos.add(new Proyecto("","","Selecciona...","Selecciona...",""));


        searchableSpinnerNomCliente = (SearchableSpinner)findViewById(R.id.searchable_spinner_nombre_item);
        searchableSpinnerNoCliente = (SearchableSpinner)findViewById(R.id.searchable_spinner_codigo_item);
        searchableSpinnerNomProyecto = (SearchableSpinner)findViewById(R.id.searchable_spinner4);
        searchableSpinnerNoProyecto = (SearchableSpinner)findViewById(R.id.searchable_spinner_numero_item);

        searchableSpinnerNomCliente.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                reporte.setCliente(alCliente.get(i));
                searchableSpinnerNoCliente.setSelection(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        searchableSpinnerNoCliente.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                reporte.setCliente(alCliente.get(i));
                searchableSpinnerNomCliente.setSelection(i);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        searchableSpinnerNomProyecto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                reporte.setProyecto(alProyectos.get(i));
                searchableSpinnerNoProyecto.setSelection(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        searchableSpinnerNoProyecto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                reporte.setProyecto(alProyectos.get(i));
                searchableSpinnerNomProyecto.setSelection(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        guardarBtn = findViewById(R.id.guardar_datos);

        guardarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {if(reporte.getCliente().getNombre().matches("Selecciona...")||reporte.getProyecto().getNombre().matches("Selecciona...")){
                Toast.makeText(getApplicationContext(),"Selecciona un cliente y un proyecto",Toast.LENGTH_SHORT).show();
            }else {
                Intent intent = new Intent(getApplicationContext(), MostrarDatosDanoActivity.class);
                intent.putExtra("reporte", reporte);
                startActivity(intent);
            }
            }
        });

        //Init DB
        FirebaseFirestore db = FirebaseFirestore.getInstance();


        db.collection("clients").orderBy("nombreBusqueda")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("hola2", document.getId() + " => " + document.getData());

                                alCliente.add(new Cliente(document.getId(),document.getString("nombre"),document.getString("numero"),document.getString("pais")));
                            }
                            onFirebaseLoadSuccess(alCliente);
                        } else {
                            //TODO TOast avisando error
                        }
                    }
                });
        db.collection("projects").orderBy("nombreBusqueda")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {


                                alProyectos.add(new Proyecto(document.getString("cliente"),document.getId(),document.getString("nombre"),document.getString("numero"), document.getString("pais")));
                            }
                            onFirebaseLoadSuccessProyecto(alProyectos);
                        } else {
                            //TODO TOast avisando error
                            Log.w("hola2", "Error getting documents.", task.getException());
                        }
                    }
                });


    }

    @Override
    public void onFirebaseLoadSuccess(List<Cliente> clienteList) {
        List<String> nombresClientes = new ArrayList<>();
        List<String> numerosClientes = new ArrayList<>();
        for (Cliente clienteSeleccionado : clienteList){
            nombresClientes.add(clienteSeleccionado.getNombre());
            numerosClientes.add(clienteSeleccionado.getNumero());

        }


        // Create adapter and set for spinner
        ArrayAdapter<String> adapterNomCliente = new ArrayAdapter<>( this, android.R.layout.simple_expandable_list_item_1,nombresClientes);
        searchableSpinnerNomCliente.setAdapter(adapterNomCliente);
        ArrayAdapter<String> adapterNoCliente = new ArrayAdapter<>( this, android.R.layout.simple_expandable_list_item_1,numerosClientes);
        searchableSpinnerNoCliente.setAdapter(adapterNoCliente);

    }

    @Override
    public void onFirebaseLoadSuccessProyecto(List<Proyecto> proyectoList) {
        List<String> nombresProyecto = new ArrayList<>();
        List<String> numerosProyecto = new ArrayList<>();
        for (Proyecto proyectoSeleccionado : proyectoList){
            nombresProyecto.add(proyectoSeleccionado.getNombre());
            numerosProyecto.add(proyectoSeleccionado.getNumero());

        }
        ArrayAdapter<String> adapterNomProyecto = new ArrayAdapter<>( this, android.R.layout.simple_expandable_list_item_1,nombresProyecto);
        searchableSpinnerNomProyecto.setAdapter(adapterNomProyecto);
        ArrayAdapter<String> adapterNoProyecto = new ArrayAdapter<>( this, android.R.layout.simple_expandable_list_item_1,numerosProyecto);
        searchableSpinnerNoProyecto.setAdapter(adapterNoProyecto);
        progress.dismiss();

    }

    @Override
    public void onFirebaseLoadFailed(String message) {

    }


}
