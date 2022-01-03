package com.panteranegra.tudoka;

import android.app.ProgressDialog;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class MenuActivity extends AppCompatActivity {

    private Button envioBtn;
    private Button devolucionBtn, danoBTN,obra, logout;
    private String cadena;
    private FirebaseAuth mAuth;
    String userId, nombreUser, emailUser, paisUser;
    private static final String TAG = "DocSnippets";
    ProgressDialog progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        progress= new ProgressDialog(this);

        progress.setTitle(R.string.cargando);

        progress.show();

        getUser();
        // Botones
        envioBtn = findViewById(R.id.reporte_envio);
        devolucionBtn = findViewById(R.id.reporte_devolucion);
        danoBTN = findViewById(R.id.reporte_dano);
        obra = findViewById(R.id.reporte_obra);
        logout = findViewById(R.id.logout);
        mAuth = FirebaseAuth.getInstance();


        envioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DatosEnvioActivity.class);
                intent.putExtra("pais", paisUser);
                startActivity(intent);
            }
        });
        devolucionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DatosDevolucionActivity.class);
                intent.putExtra("pais", paisUser);
                startActivity(intent);
            }
        });
        danoBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DatosDano.class);
                intent.putExtra("pais", paisUser);
                startActivity(intent);
            }
        });
        obra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ReporteObraActivity.class);
                intent.putExtra("pais", paisUser);
                startActivity(intent);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);

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
                    nombreUser = snapshot.getString("nombre");
                    paisUser = snapshot.getString("pais");
                    progress.dismiss();
                } else {
                    Log.d(TAG, source + " data: null");
                }
            }
        });
    }
}
