package com.panteranegra.tudoka;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.panteranegra.tudoka.Adapters.NumerosRemisionAdapters;
import com.panteranegra.tudoka.Model.ReporteDevolucion;
import com.panteranegra.tudoka.Model.ReporteEnvio;
import com.panteranegra.tudoka.utils.DatosTransporteEnvioActivity;

public class NumerosDevolucionActivity extends AppCompatActivity {

    ReporteDevolucion reporte;
    NumerosRemisionAdapters adapter;
    ListView lista;
    private static final String TAG = "DocSnippets";

    EditText numeroRemisionET;
    Button agregarRemisionBTN, finalizarReporteBTN;
    String pais;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_numeros_devolucion);

        reporte = (ReporteDevolucion) getIntent().getExtras().getSerializable("reporte");

        pais = getIntent().getExtras().getString("pais");
        numeroRemisionET = findViewById(R.id.et_numero_remision);

        lista = findViewById(R.id.lista_numeros_remision);
        adapter = new NumerosRemisionAdapters(getApplicationContext(),reporte.getAlNumerosRemision());
        lista.setAdapter(adapter);

        agregarRemisionBTN = findViewById(R.id.btn_agregar_numero_remision);
        agregarRemisionBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reporte.getAlNumerosRemision().add( numeroRemisionET.getText().toString());
                adapter.notifyDataSetChanged();
                numeroRemisionET.setText("");
            }
        });

        finalizarReporteBTN = findViewById(R.id.btn_finalizar_reporte);
        finalizarReporteBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DatosTransporteDevolucionActivity.class);
                //para pasar el modelo
                intent.putExtra("reporte", reporte);
                intent.putExtra("pais", pais);

                startActivity(intent);

            }
        });


    }

}
