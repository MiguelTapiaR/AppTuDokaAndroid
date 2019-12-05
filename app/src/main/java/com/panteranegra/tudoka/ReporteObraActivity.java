package com.panteranegra.tudoka;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ReporteObraActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporte_obra);

        Button capacitacion = findViewById(R.id.btn_reporte_capacitacion);

        capacitacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DatosCapacitacionActivity.class);

                startActivity(intent);
            }
        });
        Button seguimiento = findViewById(R.id.btn_reporte_seguimiento);
        seguimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DatosSeguimientoActivity.class);

                startActivity(intent);
            }
        });

    }
}
