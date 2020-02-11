package com.panteranegra.tudoka;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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
import com.panteranegra.tudoka.Adapters.NumerosRemisionAdapters;
import com.panteranegra.tudoka.Model.Actividad;
import com.panteranegra.tudoka.Model.Auxiliares;
import com.panteranegra.tudoka.Model.MySingleton;
import com.panteranegra.tudoka.Model.Pieza;
import com.panteranegra.tudoka.Model.ReporteEnvio;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class NumerosRemisionActivity extends AppCompatActivity {
    ReporteEnvio reporte;
    NumerosRemisionAdapters adapter;
    ListView lista;
    private static final String TAG = "DocSnippets";

    final String rutaBD = "reportesEnvio/";

    String idReporteGenerado="";
    ProgressDialog progress;

    int flag=0;
    int flagDocumentosCarga=0;
    int flagItems =0;
    int flagRemisiones =0;

    EditText numeroRemisionET;
    Button agregarRemisionBTN, finalizarReporteBTN;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_numeros_revision);

        reporte = (ReporteEnvio) getIntent().getExtras().getSerializable("reporte");

        progress= new ProgressDialog(this);

        progress.setTitle("Guardando");

        numeroRemisionET = findViewById(R.id.et_numero_remision);

        lista = findViewById(R.id.lista_numeros_remision);
        adapter = new NumerosRemisionAdapters(getApplicationContext(),reporte.getAlNumerosRemision());
        lista.setAdapter(adapter);

        agregarRemisionBTN = findViewById(R.id.btn_agregar_numero_remision);
        agregarRemisionBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reporte.getAlNumerosRemision().add( numeroRemisionET.getText().toString());
                adapter.notifyDataSetChanged();
                numeroRemisionET.setText("");
            }
        });

        finalizarReporteBTN = findViewById(R.id.btn_finalizar_reporte);
        finalizarReporteBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progress.show();
                crearReporte();
            }
        });


    }

    public void crearReporte(){
        Long fecha = System.currentTimeMillis();

        Map<String, Object> docData = new HashMap<>();
        docData.put("cliente", reporte.getCliente().getKey());
        docData.put("fechaCreacion", fecha);
        docData.put("idUsuario", "keyUsuario");
        docData.put("pais", "MX");
        docData.put("proyecto", reporte.getProyecto().getKey());

                        // Get new Instance ID token

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(rutaBD)
                .add(docData)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        idReporteGenerado = documentReference.getId();
                        cargarFotosTransporte();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });

    }
    public void cargarFotosTransporte(){
        progress.setMessage("Subiendo fotos transporte");
        subirFotosTransporte(reporte.getFotoLicencia(),0);
        subirFotosTransporte(reporte.getFotoPlacaDelantera(),1);
        subirFotosTransporte(reporte.getFotoPlacaTrasera(),2);
        subirFotosTransporte(reporte.getFotoTractoLateral1(),3);
        subirFotosTransporte(reporte.getFotoTractoLateral2(),4);
        subirFotosTransporte(reporte.getFotoTractoParteTrasera(),5);
    }

    public void subirFotosTransporte(String url, final int nombre){
        final int flagTransporte =0;



        FirebaseStorage storage = FirebaseStorage.getInstance();

        StorageReference storageRef = storage.getReference();

        // Get the data from an ImageView as bytes




        Auxiliares auxiliares = new Auxiliares();
        Bitmap bm =auxiliares.getImageResized(getApplicationContext(), Uri.fromFile(new File(url)));

        Uri file = auxiliares.getImageUri(getApplicationContext(),bm);

        String[] aNombre= url.split("/");
        int indexNombre=(aNombre.length)-1;
        final StorageReference ref = storageRef.child("enviospruebas/"+aNombre[indexNombre]);
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

                    switch (nombre){
                        case 0:
                            reporte.setUrlFotoLicencia(downloadUri.toString());
                            actualizarBDTransporte(downloadUri.toString(), "fotoLicencia");
                            break;
                        case 1:
                            reporte.setUrlFotoPlacaDelantera(downloadUri.toString());
                            actualizarBDTransporte(downloadUri.toString(), "fotoPlacaDelantera");
                            break;
                        case 2:
                            reporte.setUrlFotoPlacaTrasera(downloadUri.toString());
                            actualizarBDTransporte(downloadUri.toString(), "fotoPlacaTrasera");
                            break;
                        case 3:
                            reporte.setUrlFotoTractoLateral1(downloadUri.toString());
                            actualizarBDTransporte(downloadUri.toString(), "fotoTractoLateral1");
                            break;
                        case 4:
                            reporte.setUrlFotoTractoLateral2(downloadUri.toString());
                            actualizarBDTransporte(downloadUri.toString(), "fotoTractoLateral2");
                            break;
                        case 5:
                            reporte.setUrlFotoTractoParteTrasera(downloadUri.toString());
                            actualizarBDTransporte(downloadUri.toString(), "fotoTractoTrasera");

                            break;
                    }






                } else {
                    // Handle failures
                    // ...
                }
            }
        });

    }


    private void actualizarBDTransporte(String url, String nombre){
        Map<String, Object> docData = new HashMap<>();
        docData.put(nombre, url);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(rutaBD).document(idReporteGenerado)
                .update(docData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                        flag++;
                        if(flag==1){
                            Toast.makeText(getApplicationContext(),"Fotos transporte subidas", Toast.LENGTH_SHORT).show();
                            cargaFotosDocumentosCarga();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });


    }
    public Boolean cargaFotosDocumentosCarga(){
        progress.setMessage("Subiendo documentos de carga");

        if(reporte.getAlListasCarga().isEmpty()){
            cargaItems();
        }else{

            for(String string: reporte.getAlListasCarga()){
                subirFotosDocumentosCarga(string);
            }
        }

        return true;
    }

    public void subirFotosDocumentosCarga(final String actividad){



        FirebaseStorage storage = FirebaseStorage.getInstance();

        StorageReference storageRef = storage.getReference();

        // Get the data from an ImageView as bytes



        Auxiliares auxiliares = new Auxiliares();
        Bitmap bm =auxiliares.getImageResized(getApplicationContext(), Uri.fromFile(new File(actividad)));

        Uri file = auxiliares.getImageUri(getApplicationContext(),bm);


        String[] aNombre= actividad.split("/");
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

                    reporte.getAlURLListasCarga().add(downloadUri.toString());

                    actualizarBDDocumentosCarga(downloadUri.toString());


                } else {
                    // Handle failures
                    // ...
                }
            }
        });

    }
    private void actualizarBDDocumentosCarga(String actividad){
        Map<String, Object> docData = new HashMap<>();
        docData.put("foto", actividad);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(rutaBD+idReporteGenerado+"/documentosCarga/")
                .add(docData)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());

                        flagDocumentosCarga++;
                        if(flagDocumentosCarga==reporte.getAlListasCarga().size()){
                            cargaItems();
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


    public Boolean cargaItems(){
        progress.setMessage("Subiendo items");
        if(reporte.getAlPiezas().isEmpty()){
            cargaRemisiones();
        }else{

            for(Pieza pieza: reporte.getAlPiezas()){
                subirItems(pieza);
            }
        }
        return true;
    }

    public void subirItems(final Pieza actividad){



        FirebaseStorage storage = FirebaseStorage.getInstance();

        StorageReference storageRef = storage.getReference();

        // Get the data from an ImageView as bytes


        Auxiliares auxiliares = new Auxiliares();
        Bitmap bm =auxiliares.getImageResized(getApplicationContext(), Uri.fromFile(new File(actividad.getFotoItemResumen())));

        Uri file = auxiliares.getImageUri(getApplicationContext(),bm);


        String[] aNombre= actividad.getFotoItemResumen().split("/");
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

                    actividad.setUrl(downloadUri.toString());

                    actualizarBDItems(actividad);


                } else {
                    // Handle failures
                    // ...
                }
            }
        });

    }
    private void actualizarBDItems(Pieza actividad){
        Map<String, Object> docData = new HashMap<>();
        docData.put("foto", actividad.getUrl());
        docData.put("unidades", actividad.getUnidades());
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(rutaBD+idReporteGenerado+"/items/")
                .add(docData)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());

                        flagItems++;
                        if(flagItems==reporte.getAlPiezas().size()){
                            cargaRemisiones();
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


    public Boolean cargaRemisiones(){
        progress.setMessage("Subiendo remisiones");
        if(reporte.getAlNumerosRemision().isEmpty()){
            crearPDF();
        }else{

            for(String string: reporte.getAlNumerosRemision()){
                actualizarBDRemisiones(string);
            }
        }
        return true;
    }


    private void actualizarBDRemisiones(String actividad){
        Map<String, Object> docData = new HashMap<>();
        docData.put("remision", actividad);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(rutaBD+idReporteGenerado+"/remisiones/")
                .add(docData)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());

                        flagRemisiones++;
                        if(flagRemisiones==reporte.getAlNumerosRemision().size()){
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
//        map.put("items", aux.convertirALtoJSON(reporte.getAlActividad()));
        map.put("reporteId",idReporteGenerado );
        map.put("emails",emails );
        map.put("nombreCliente", reporte.getCliente().getNombre());
        map.put("numeroCliente", reporte.getCliente().getNumero());
        map.put("nombreProyecto", reporte.getProyecto().getNombre());
        map.put("numeroProyecto", reporte.getProyecto().getNumero());
        map.put("nombreUsuario", "Nombre usuario");
        map.put("emailUsuario", "emailusuario");

        //transporte
        map.put("urlFotoLicencia",reporte.getUrlFotoLicencia());
        map.put("urlFotoPlacaDelantera",reporte.getUrlFotoPlacaDelantera());
        map.put("urlFotoPlacaTrasera",reporte.getUrlFotoPlacaTrasera());
        map.put("urlFotoTractoLateral1",reporte.getUrlFotoTractoLateral1());
        map.put("urlFotoTractoLateral2",reporte.getUrlFotoTractoLateral2());
        map.put("urlFotoTractoTrasera",reporte.getUrlFotoTractoParteTrasera());

        //lista carga
        map.put("urListaCarga", aux.convertirALEmailstoJSON(reporte.getAlURLListasCarga()));

        //remisiones
        map.put("remisiones", aux.convertirALEmailstoJSON(reporte.getAlNumerosRemision()));

        //items
        map.put("items",aux.convertirALItemstoJSON(reporte.getAlPiezas()));
        JSONObject jsonObject = new JSONObject(map);
        String url = "https://www.themyt.com/reportedoka/reporte_envioAndroid.php";

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
                            intent.putExtra("urlReporte", "pdfs/"+idReporteGenerado+".pdf");
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
                Log.w(TAG, "Error adding document", error);
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
