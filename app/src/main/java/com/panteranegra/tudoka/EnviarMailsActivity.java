package com.panteranegra.tudoka;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.panteranegra.tudoka.Adapters.RenglonListasCargaAdapter;
import com.panteranegra.tudoka.Model.Auxiliares;
import com.panteranegra.tudoka.Model.MySingleton;
import com.panteranegra.tudoka.Model.ReporteCapacitacion;
import com.panteranegra.tudoka.Model.ReporteEnvio;
import com.panteranegra.tudoka.utils.DatosTransporteEnvioActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import pl.aprilapps.easyphotopicker.EasyImage;

public class EnviarMailsActivity extends AppCompatActivity {

    private EasyImage easy_image;

    String urlReporte, tipoReporte;

    private static final String TAG = "DocSnippets";

    EditText etEmail1,etEmail2,etEmail3,etEmail4,etEmail5,etContacto;
    Button btnEnviar, btnSalir, btnVerReporte;
    ProgressDialog progress;
    String paisUser, nombreUser, emailUser, puestoUser,userId, nombreContacto, nombreProyecto,idReporte;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enviar_reporte);

        getUser();
        nombreContacto = "";

        urlReporte = (String) getIntent().getStringExtra("urlReporte");
        paisUser = (String) getIntent().getStringExtra("pais");
        nombreUser = (String) getIntent().getStringExtra("nombre");
        emailUser = (String) getIntent().getStringExtra("email");
        tipoReporte = (String) getIntent().getStringExtra("tipo");
        nombreProyecto = (String) getIntent().getStringExtra("nombreProyecto");
        idReporte = (String) getIntent().getStringExtra("idReporte");
        progress= new ProgressDialog(this);

        etEmail1 = findViewById(R.id.et_email1);
        etEmail2 = findViewById(R.id.et_email2);
        etEmail3 = findViewById(R.id.et_email3);
        etEmail4 = findViewById(R.id.et_email4);
        etEmail5 = findViewById(R.id.et_email5);
        etContacto = findViewById(R.id.et_contacto);

        btnVerReporte = findViewById(R.id.btn_ver_reporte);
        btnVerReporte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://themyt.com/reportedoka/"+urlReporte));
                startActivity(browserIntent);
            }
        });
        btnEnviar = findViewById(R.id.btn_enviar_correos);
        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ArrayList<String> alEmails = new ArrayList<>();

                if(!etEmail1.getText().toString().matches("")){
                    alEmails.add(etEmail1.getText().toString());
                }
                if(!etEmail2.getText().toString().matches("")){
                    alEmails.add(etEmail2.getText().toString());
                }
                if(!etEmail3.getText().toString().matches("")){
                    alEmails.add(etEmail3.getText().toString());
                }

                if(!etEmail4.getText().toString().matches("")){
                    alEmails.add(etEmail4.getText().toString());
                }

                if(!etEmail5.getText().toString().matches("")){
                    alEmails.add(etEmail5.getText().toString());
                }

                if(!etContacto.getText().toString().matches("")){
                    nombreContacto = etContacto.getText().toString();
                }
                if(alEmails.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Escribe al menos un email", Toast.LENGTH_SHORT).show();
                }else{
                    progress.show();
                    enviarPDF(alEmails);
                }
            }
        });

        btnSalir = findViewById(R.id.btn_salir);
        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                //para pasar el modelo

                startActivity(intent);
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
                    puestoUser = snapshot.getString("puesto");
                } else {
                    Log.d(TAG, source + " data: null");
                }
            }
        });
    }

    public void enviarPDF(ArrayList<String> alMails){

        progress.setMessage("Enviando ...");
//        ArrayList<String> emails = new ArrayList<>();
//        emails.add("rmontoya@themyt.com");
        HashMap<String, Object> map = new HashMap<>();// Mapeo previo
        Auxiliares aux = new Auxiliares();
        map.put("emails", aux.convertirALEmailstoJSON(alMails));
        map.put("urlReporte", urlReporte);

        map.put("paisUsuario", paisUser);
        map.put("nombreUsuario", nombreUser);
        map.put("emailUsuario", emailUser);
        map.put("puestoUsuario", puestoUser);
        map.put("nombreContacto", nombreContacto);
        map.put("nombreProyecto", nombreProyecto);
        map.put("idReporte", idReporte);
        map.put("tipo", tipoReporte);
        JSONObject jsonObject = new JSONObject(map);
        String url = "https://www.themyt.com/reportedoka/enviar_reporte_v4_0.php";


        Log.d(TAG, map.toString() );
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
                            Toast.makeText(getApplicationContext(),respuesta, Toast.LENGTH_LONG).show();
                            if(estado ==1){
                                etEmail1.setText("");
                                etEmail2.setText("");
                                etEmail3.setText("");
                            }





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

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq);

    }
}