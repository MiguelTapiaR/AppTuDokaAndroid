package com.panteranegra.tudoka;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.panteranegra.tudoka.Model.ReporteEnvio;

public class MostrarDatosEnvioActivity extends AppCompatActivity {

    private Button tomarFotosBtn;
    private TextView nombreCliente, numeroCliente, nombreProyecto, numeroProyecto;
    private ReporteEnvio reporte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_datos_envio);

        //elementos de layout
        tomarFotosBtn = findViewById(R.id.empezarTomarFotos);
        nombreCliente = findViewById(R.id.nombre_cliente);
        numeroCliente = findViewById(R.id.numero_cliente);
        nombreProyecto = findViewById(R.id.nombre_proyecto);
        numeroProyecto = findViewById(R.id.numero_proyecto);

        reporte = (ReporteEnvio) getIntent().getExtras().getSerializable("reporte");

        nombreCliente.setText(reporte.getCliente().getNombre());
        numeroCliente.setText(reporte.getCliente().getNumero());

        nombreProyecto.setText(reporte.getProyecto().getNombre());
        numeroProyecto.setText(reporte.getProyecto().getNumero());

        tomarFotosBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AgregarItemEnvioActivity.class);
                intent.putExtra("reporte", reporte);
                startActivity(intent);
            }
        });
    }
}
