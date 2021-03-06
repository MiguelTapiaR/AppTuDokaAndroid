package com.panteranegra.tudoka;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.panteranegra.tudoka.Adapters.ItemsAdapter;
import com.panteranegra.tudoka.Model.ReporteEnvio;
import com.panteranegra.tudoka.utils.DatosTransporteEnvioActivity;

import java.util.ArrayList;


public class ResumenItemsEnvioActivity extends AppCompatActivity {
    private ArrayList items;
    ItemsAdapter adapter;

    Button continuarBtn;

    ListView contenido;
    ReporteEnvio reporte;
    String pais;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resumen_items);

        reporte = (ReporteEnvio) getIntent().getExtras().getSerializable("reporte");

        pais = getIntent().getExtras().getString("pais");
        contenido = findViewById(R.id.list_view_items);

        adapter = new ItemsAdapter(getApplicationContext(),reporte.getAlPiezas());
        contenido.setAdapter(adapter);
        Button nuevoItem = findViewById(R.id.button7);
        nuevoItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AgregarItemEnvioActivity.class);
                //para pasar el modelo
                intent.putExtra("reporte", reporte);
                intent.putExtra("pais", pais);
                startActivity(intent);
            }
        });

        continuarBtn = findViewById(R.id.button8);
        continuarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ListasCargasEnvioActivity.class);
                //para pasar el modelo
                intent.putExtra("reporte", reporte);
                intent.putExtra("pais", pais);
                startActivity(intent);
            }
        });

    }
}
