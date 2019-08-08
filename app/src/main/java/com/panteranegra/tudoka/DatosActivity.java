package com.panteranegra.tudoka;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.panteranegra.tudoka.Interface.IfFirebaseLoadDone;
import com.panteranegra.tudoka.Model.Cliente;
import com.panteranegra.tudoka.Model.Proyecto;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;
import java.util.List;

public class DatosActivity extends AppCompatActivity implements IfFirebaseLoadDone {

    SearchableSpinner searchableSpinnerNomCliente, searchableSpinnerNoCliente, searchableSpinnerNomProyecto, searchableSpinnerNoProyecto;

    DatabaseReference clientesRef;
    private Button guardarBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos);
        searchableSpinnerNomCliente = (SearchableSpinner)findViewById(R.id.searchable_spinner_nombre_item);
        searchableSpinnerNoCliente = (SearchableSpinner)findViewById(R.id.searchable_spinner_codigo_item);
        searchableSpinnerNomProyecto = (SearchableSpinner)findViewById(R.id.searchable_spinner4);
        searchableSpinnerNoProyecto = (SearchableSpinner)findViewById(R.id.searchable_spinner_numero_item);
        guardarBtn = findViewById(R.id.guardar_datos);

        guardarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MostrarActivity.class);
                startActivity(intent);
            }
        });

        //Init DB
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final ArrayList<Cliente> alCliente = new ArrayList<>();
        final ArrayList<Proyecto> alProjects = new ArrayList<>();
        db.collection("clients")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("hola2", document.getId() + " => " + document.getData());

                                alCliente.add(new Cliente("","Rafa","",""));
                            }
                            onFirebaseLoadSuccess(alCliente);
                        } else {
                            Log.w("hola2", "Error getting documents.", task.getException());
                        }
                    }
                });
        db.collection("projects")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("hola2", document.getId() + " => " + document.getData());

                                alProjects.add(new Proyecto("","Rafa","","", ""));
                            }
                            onFirebaseLoadSuccessProyecto(alProjects);
                        } else {
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

    }

    @Override
    public void onFirebaseLoadFailed(String message) {

    }


}
