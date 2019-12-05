package com.panteranegra.tudoka;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends AppCompatActivity {

    private Button envioBtn;
    private Button devolucionBtn, danoBTN,obra;
    private String cadena;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // Botones
        envioBtn = findViewById(R.id.reporte_envio);
        devolucionBtn = findViewById(R.id.reporte_devolucion);
        danoBTN = findViewById(R.id.reporte_dano);
        obra = findViewById(R.id.reporte_obra);


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
    }
}
