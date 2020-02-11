package com.panteranegra.tudoka;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.panteranegra.tudoka.Adapters.ActividadesAdapter;
import com.panteranegra.tudoka.Adapters.ItemsAdapter;
import com.panteranegra.tudoka.Model.Actividad;
import com.panteranegra.tudoka.Model.Auxiliares;
import com.panteranegra.tudoka.Model.MySingleton;
import com.panteranegra.tudoka.Model.ReporteCapacitacion;
import com.panteranegra.tudoka.Model.ReporteDano;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ResumenActividadesCapacitacionActivity extends AppCompatActivity {

    ActividadesAdapter adapter;

    private static final String TAG = "DocSnippets";
    Button continuarBtn;

    ListView contenido;
    ReporteCapacitacion reporte;
    String idReporteGenerado="";
    ProgressDialog progress;

    int flag=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resumen_actividad_capacitacion);




        reporte = (ReporteCapacitacion) getIntent().getExtras().getSerializable("reporte");
         progress= new ProgressDialog(this);

        progress.setTitle("Guardando");



        contenido = findViewById(R.id.list_view_items);

        adapter = new ActividadesAdapter(getApplicationContext(),reporte.getAlActividad());
        contenido.setAdapter(adapter);

        Button nuevoItemBTN = findViewById(R.id.button7);
        nuevoItemBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AgregarActividadCapacitacionActivity.class);
//                //para pasar el modelo
                intent.putExtra("reporte", reporte);
                startActivity(intent);
            }
        });

        continuarBtn = findViewById(R.id.button8);
        continuarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), ListasCargasEnvioActivity.class);
//                //para pasar el modelo
//                intent.putExtra("reporte", reporte);
//                startActivity(intent);
                progress.show();
//                continuarBtn.setEnabled(false);

                crearReporte();
            }
        });
    }

    public void crearReporte(){
        progress.setMessage("Guardando en Base de Datos");
        Long fecha = System.currentTimeMillis();

        Map<String, Object> docData = new HashMap<>();
        docData.put("cliente", reporte.getCliente().getKey());
        docData.put("fechaCreacion", fecha);
        docData.put("idUsuario", "keyUsuario");
        docData.put("pais", "MX");
        docData.put("proyecto", reporte.getProyecto().getKey());

        // Get new Instance ID token

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("reportesCapacitacion")
                .add(docData)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        idReporteGenerado = documentReference.getId();
                        cargaFotos();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });

    }
    public Boolean cargaFotos(){
        for(Actividad actividad: reporte.getAlActividad()){
            subirFotos(actividad);
        }
        return true;
    }

    public void subirFotos(final Actividad actividad){



        FirebaseStorage storage = FirebaseStorage.getInstance();

        StorageReference storageRef = storage.getReference();
        final String urlDescarga="";
        // Get the data from an ImageView as bytes



        Uri file = Uri.fromFile(new File(actividad.getImagen()));

        String[] aNombre= actividad.getImagen().split("/");
        int indexNombre=(aNombre.length)-1;
        final StorageReference ref = storageRef.child("images/"+aNombre[indexNombre]);
        UploadTask uploadTask = ref.putFile(file);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL

                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    actividad.setUrlDescarga(downloadUri.toString());

                    actualizarBD(actividad);


                } else {
                    // Handle failures
                    // ...
                }
            }
        });

    }
    private void actualizarBD(Actividad actividad){
        Map<String, Object> docData = new HashMap<>();
        docData.put("descripcion", actividad.getDesripcion());
        docData.put("foto", actividad.getUrlDescarga());
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("reportesCapacitacion/"+idReporteGenerado+"/actividades/")
                .add(docData)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());

                        flag++;
                        if(flag==reporte.getAlActividad().size()){
                            crearPDF();
                        }



                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                        Toast.makeText(getApplicationContext(),"Error", Toast.LENGTH_SHORT).show();

                    }
                });
    }


    public void crearPDF(){

        progress.setMessage("Generando pdf");
        ArrayList<String> emails = new ArrayList<>();
        emails.add("rmontoya@themyt.com");
        HashMap<String, Object> map = new HashMap<>();// Mapeo previo
        Auxiliares aux = new Auxiliares();
        map.put("items", aux.convertirALtoJSON(reporte.getAlActividad()));
        map.put("reporteId",idReporteGenerado );
        map.put("emails",emails );
        map.put("nombreCliente", reporte.getCliente().getNombre());
        map.put("numeroCliente", reporte.getCliente().getNumero());
        map.put("nombreProyecto", reporte.getProyecto().getNombre());
        map.put("numeroProyecto", reporte.getProyecto().getNumero());
        map.put("nombreUsuario", "Nombre usuario");
        map.put("emailUsuario", "emailusuario");
        map.put("nombreCurso", reporte.getNombreCurso());
        JSONObject jsonObject = new JSONObject(map);
        String url = "https://www.themyt.com/reportedoka/reporteCapacitacionAndroid.php";

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, jsonObject,
                new Response.Listener<JSONObject>() {

                    int estado=0;
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            progress.dismiss();

                            estado= response.getInt("estado");

                            String respuesta= response.getString("respuesta");
                            Toast.makeText(getApplicationContext(),respuesta, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), EnviarMailsActivity.class);
                            //para pasar el modelo
                            intent.putExtra("urlReporte", "reportecapacitacion/"+idReporteGenerado+".pdf");
                            startActivity(intent);



                            //progressDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                /*progressDialog.dismiss();
                toastPersonalizado.crearToast("Error de conexión, revisa tu conexión a internet e intenta nuevamente", null);*/
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("User-agent", "My useragent");
                return headers;
            }


        };
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(60000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq);

    }

}