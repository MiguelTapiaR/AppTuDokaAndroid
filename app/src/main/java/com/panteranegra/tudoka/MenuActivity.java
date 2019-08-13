package com.panteranegra.tudoka;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends AppCompatActivity {

    private Button envioBtn;
    private Button devolucionBtn;
    private String cadena;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // Botones
        envioBtn = findViewById(R.id.reporte_envio);
        devolucionBtn = findViewById(R.id.reporte_devolucion);


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
                Intent intent = new Intent(getApplicationContext(), DatosActivity.class);

                startActivity(intent);
            }
        });
    }
}
