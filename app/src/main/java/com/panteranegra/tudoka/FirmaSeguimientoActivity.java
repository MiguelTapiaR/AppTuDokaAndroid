package com.panteranegra.tudoka;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kyanogen.signatureview.SignatureView;
import com.panteranegra.tudoka.Model.Actividad;
import com.panteranegra.tudoka.Model.Auxiliares;
import com.panteranegra.tudoka.Model.MySingleton;
import com.panteranegra.tudoka.Model.ReporteSeguimiento;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FirmaSeguimientoActivity extends AppCompatActivity {

    ReporteSeguimiento reporte;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private SignatureView signatureView;
    private Button mClearButton;
    private Button mSaveButton;
    private Button continuar;

    String idReporteGenerado="";
    ProgressDialog progress;
    int flag=0;

    private FirebaseAuth mAuth;


    String userId, nombreUser, emailUser, paisUser, puestoUser;

    private static final String TAG = "DocSnippets";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getUser();
        setContentView(R.layout.activity_firma_seguimiento);

        setTitle("Firma del cliente");
        progress= new ProgressDialog(this);

        progress.setTitle(getString(R.string.guardando));
        //recibir el modelo
        reporte = (ReporteSeguimiento) getIntent().getExtras().getSerializable("reporte");


        signatureView = (SignatureView) findViewById(R.id.signature_view);
        mClearButton = (Button) findViewById(R.id.clear_button);
        mSaveButton = (Button) findViewById(R.id.guardar);
        continuar = (Button) findViewById(R.id.sin_firma);

        mClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signatureView.clearCanvas();
            }
        });

        mSaveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                progress.show();
                crearReporte();

            }
        });

        continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress.show();
                crearReporte();
            }
        });
    }







    public void getUser(){
        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        emailUser = mAuth.getCurrentUser().getEmail();

        //Init DB
        FirebaseFirestore db = FirebaseFirestore.getInstance();




        final DocumentReference docRef = db.collection("users").document(userId);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                String source = snapshot != null && snapshot.getMetadata().hasPendingWrites()
                        ? "Local" : "Server";

                if (snapshot != null && snapshot.exists()) {
                    Log.d(TAG, source + " data: " + snapshot.getData());
                    nombreUser = snapshot.getString("nombre");
                    paisUser = snapshot.getString("pais");

                } else {
                    Log.d(TAG, source + " data: null");
                }
            }
        });
    }

    //**** GUARDADO REPORTE***//

    public void crearReporte(){
        progress.setMessage("Guardando en Base de Datos");
        Long fecha = System.currentTimeMillis();

        Map<String, Object> docData = new HashMap<>();
        docData.put("cliente", reporte.getCliente().getKey());
        docData.put("fechaCreacion", fecha);
        docData.put("idUsuario", "keyUsuario");
        docData.put("pais", paisUser);
        docData.put("proyecto", reporte.getProyecto().getKey());

        // Get new Instance ID token

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("reportesSeguimiento")
                .add(docData)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        idReporteGenerado = documentReference.getId();

                        if(!signatureView.isBitmapEmpty()){
                            subirFirma();
                        }else{
                            cargaFotos();
                        }



                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });

    }

    private void subirFirma(){
        progress.setMessage("Cargando firma...");
        FirebaseStorage storage = FirebaseStorage.getInstance();

        StorageReference storageRef = storage.getReference();
        Auxiliares auxiliares = new Auxiliares();
        Bitmap bitmap = signatureView.getSignatureBitmap();

        Uri file = auxiliares.getImageUri(getApplicationContext(),bitmap);

        //String[] aNombre= file.split("/");
        final StorageReference ref = storageRef.child("imagesSeguimientopb/"+idReporteGenerado+"/firmaCliente");
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
                    reporte.setUrlFirma(downloadUri.toString());
                    cargaFotos();



                } else {
                    // Handle failures
                    // ...
                }
            }
        });

    }
    public Boolean cargaFotos(){
        progress.setMessage("Cargando fotos...");



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

        Auxiliares auxiliares = new Auxiliares();
        Bitmap bm =auxiliares.getImageResized(getApplicationContext(), Uri.fromFile(new File(actividad.getImagen())));

        Uri file = auxiliares.getImageUri(getApplicationContext(),bm);

        String[] aNombre= actividad.getImagen().split("/");
        int indexNombre=(aNombre.length)-1;
        final StorageReference ref = storageRef.child("imagesSeguimientopb/"+idReporteGenerado+"/"+aNombre[indexNombre]);
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
        db.collection("reportesSeguimiento/"+idReporteGenerado+"/actividades/")
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

        progress.setMessage(getString(R.string.generando_pdf));
        ArrayList<String> emails = new ArrayList<>();
        emails.add("r.seguimiento.tudoka@themyt.com");
        HashMap<String, Object> map = new HashMap<>();// Mapeo previo
        Auxiliares aux = new Auxiliares();
        map.put("items", aux.convertirALtoJSON(reporte.getAlActividad()));
        map.put("reporteId",idReporteGenerado );
        map.put("emails",emails );
        map.put("nombreCliente", reporte.getCliente().getNombre());
        map.put("numeroCliente", reporte.getCliente().getNumero());
        map.put("nombreProyecto", reporte.getProyecto().getNombre());
        map.put("numeroProyecto", reporte.getProyecto().getNumero());
        map.put("nombreUsuario", nombreUser);
        map.put("emailUsuario", emailUser);
        map.put("paisUsuario", paisUser);
        map.put("urlFirma", reporte.getUrlFirma());
        JSONObject jsonObject = new JSONObject(map);
        String url = "https://www.themyt.com/reportedoka/reporteSeguimientoAndroid_v4_0.php";

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
                            String reporteId= response.getString("reporte_id");
                            String tipo= response.getString("tipoReporte");
                            Toast.makeText(getApplicationContext(),respuesta, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), EnviarMailsActivity.class);
                            //para pasar el modelo
                            intent.putExtra("urlReporte", "reporte_seguimiento/"+reporteId+".pdf");
                            intent.putExtra("pais", paisUser);
                            intent.putExtra("email", emailUser);
                            intent.putExtra("nombre", nombreUser);
                            intent.putExtra("nombreProyecto", reporte.getProyecto().getNombre());
                            intent.putExtra("idReporte", reporteId);
                            intent.putExtra("tipo", tipo);



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