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
import com.panteranegra.tudoka.Model.Actividad;
import com.panteranegra.tudoka.Model.Cliente;
import com.panteranegra.tudoka.Model.Pieza;
import com.panteranegra.tudoka.Model.Proyecto;
import com.panteranegra.tudoka.Model.ReporteCapacitacion;
import com.panteranegra.tudoka.Model.ReporteEnvio;
import com.panteranegra.tudoka.Model.ReporteSeguimiento;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;
import java.util.List;

public class DatosSeguimientoActivity extends AppCompatActivity implements IfFirebaseLoadDone {

    SearchableSpinner searchableSpinnerNomCliente, searchableSpinnerNoCliente, searchableSpinnerNomProyecto, searchableSpinnerNoProyecto;

    DatabaseReference clientesRef;
    private Button guardarBtn;
    private ReporteSeguimiento reporteEnvio;
    private ArrayList<Cliente> alCliente;
    private ArrayList<Proyecto> alProyectos;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_seguimiento);

        progress= new ProgressDialog(this);

        progress.setTitle(R.string.cargando);

        progress.show();
        reporteEnvio = new ReporteSeguimiento();
        reporteEnvio.setAlActividad(new ArrayList<Actividad>());

        alCliente = new ArrayList<>();
        alProyectos = new ArrayList<>();  alCliente.add(new Cliente("",getString(R.string.selecciona),getString(R.string.selecciona),""));
        alProyectos.add(new Proyecto("","",getString(R.string.selecciona),getString(R.string.selecciona),""));



        searchableSpinnerNomCliente = (SearchableSpinner)findViewById(R.id.searchable_spinner_nombre_item);
        searchableSpinnerNoCliente = (SearchableSpinner)findViewById(R.id.searchable_spinner_codigo_item);
        searchableSpinnerNomProyecto = (SearchableSpinner)findViewById(R.id.searchable_spinner4);
        searchableSpinnerNoProyecto = (SearchableSpinner)findViewById(R.id.searchable_spinner_numero_item);

        searchableSpinnerNomCliente.setTitle(getString(R.string.seleccionar_item));
        searchableSpinnerNoCliente.setTitle(getString(R.string.seleccionar_item));
        searchableSpinnerNomProyecto.setTitle(getString(R.string.seleccionar_item));
        searchableSpinnerNoProyecto.setTitle(getString(R.string.seleccionar_item));
        searchableSpinnerNoProyecto.setPositiveButton(getString(R.string.cerrar));
        searchableSpinnerNomProyecto.setPositiveButton(getString(R.string.cerrar));
        searchableSpinnerNomCliente.setPositiveButton(getString(R.string.cerrar));
        searchableSpinnerNoCliente.setPositiveButton(getString(R.string.cerrar));

        searchableSpinnerNomCliente.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                reporteEnvio.setCliente(alCliente.get(i));
                searchableSpinnerNoCliente.setSelection(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        searchableSpinnerNoCliente.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                reporteEnvio.setCliente(alCliente.get(i));
                searchableSpinnerNomCliente.setSelection(i);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        searchableSpinnerNomProyecto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                reporteEnvio.setProyecto(alProyectos.get(i));
                searchableSpinnerNoProyecto.setSelection(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        searchableSpinnerNoProyecto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                reporteEnvio.setProyecto(alProyectos.get(i));
                searchableSpinnerNomProyecto.setSelection(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        guardarBtn = findViewById(R.id.guardar_datos);

        guardarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(reporteEnvio.getCliente().getNombre().matches(getString(R.string.selecciona))||reporteEnvio.getProyecto().getNombre().matches(getString(R.string.selecciona))){
                    Toast.makeText(getApplicationContext(),"Selecciona un cliente y un proyecto",Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(getApplicationContext(), MostrarDatosSeguimientoActivity.class);
                    intent.putExtra("reporte", reporteEnvio);
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
