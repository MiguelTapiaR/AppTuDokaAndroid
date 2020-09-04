package com.panteranegra.tudoka;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
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


    EditText etEmail1,etEmail2,etEmail3;
    Button btnEnviar, btnSalir;
    ProgressDialog progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enviar_reporte);

        urlReporte = (String) getIntent().getStringExtra("urlReporte");
        progress= new ProgressDialog(this);

        etEmail1 = findViewById(R.id.et_email1);
        etEmail2 = findViewById(R.id.et_email2);
        etEmail3 = findViewById(R.id.et_email3);

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


    public void enviarPDF(ArrayList<String> alMails){
        Toast.makeText(getApplicationContext(),getText(R.string.exito_guardar), Toast.LENGTH_SHORT).show();
        progress.setMessage("Enviando ...");
//        ArrayList<String> emails = new ArrayList<>();
//        emails.add("rmontoya@themyt.com");
        HashMap<String, Object> map = new HashMap<>();// Mapeo previo
        Auxiliares aux = new Auxiliares();
        map.put("emails", aux.convertirALEmailstoJSON(alMails));
        map.put("urlReporte", urlReporte);

        JSONObject jsonObject = new JSONObject(map);
        String url = "https://www.themyt.com/reportedoka/enviar_reporte.php";

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
                            etEmail1.setText("");
                            etEmail2.setText("");
                            etEmail3.setText("");




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