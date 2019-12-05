package com.panteranegra.tudoka;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class MenuActivity extends AppCompatActivity {

    private Button envioBtn;
    private Button devolucionBtn, danoBTN,obra, logout;
    private String cadena;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

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

                startActivity(intent);
            }
        });
        devolucionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DatosDevolucionActivity.class);

                startActivity(intent);
            }
        });
        danoBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DatosDano.class);

                startActivity(intent);
            }
        });
        obra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ReporteObraActivity.class);

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
}
