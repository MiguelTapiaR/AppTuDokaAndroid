package com.panteranegra.tudoka;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MostrarActivity extends AppCompatActivity {

    private Button tomarFotosBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar);
        tomarFotosBtn = findViewById(R.id.empezarTomarFotos);

        tomarFotosBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DatosTransporteDevolucionActivity.class);
                startActivity(intent);
            }
        });
    }
}
