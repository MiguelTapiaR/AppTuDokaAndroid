package com.panteranegra.tudoka;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.panteranegra.tudoka.Model.ReporteDevolucion;
import com.panteranegra.tudoka.Model.ReporteEnvio;

public class MostrarDatosDevolucionActivity extends AppCompatActivity {

    ReporteDevolucion reporte;
    TextView nombreClienteTV, numeroClienteTV, nombreProyecto, numeroProyecto;
    Button continuarBTN;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_datos_devolucion);

        reporte = (ReporteDevolucion) getIntent().getExtras().getSerializable("reporte");

        nombreClienteTV= findViewById(R.id.tv_nombre_cliente_devolucion);
        nombreClienteTV.setText(reporte.getCliente().getNombre());

        numeroClienteTV = findViewById(R.id.tv_numero_cliente_devolucion);
        numeroClienteTV.setText(reporte.getCliente().getNumero());

        nombreProyecto= findViewById(R.id.tv_nombre_proyecto_devolucion);
        nombreProyecto.setText(reporte.getProyecto().getNombre());

        numeroProyecto = findViewById(R.id.tv_numero_proyecto_devolucion);
        numeroProyecto.setText(reporte.getProyecto().getNumero());
        continuarBTN = findViewById(R.id.btn_continuar_mostrar_datos_devolucion);
        continuarBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DocumentosClienteActivity.class);
                intent.putExtra("reporte", reporte);
                startActivity(intent);
            }
        });



    }
}
