package com.panteranegra.tudoka;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.panteranegra.tudoka.Interface.IfFirebaseLoadDonePieza;
import com.panteranegra.tudoka.Model.Pieza;
import com.panteranegra.tudoka.Model.ReporteDevolucion;
import com.panteranegra.tudoka.Model.ReporteEnvio;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AgregarItemEnvioActivity extends AppCompatActivity implements IfFirebaseLoadDonePieza {

    Button fotoItemBtn, continuarBtn;
    EditText unidadesET;
    ImageView imageViewFotoItem;
    ReporteEnvio reporte;
    SearchableSpinner searchableSpinnerNomItem, searchableSpinnerCodigoItem, searchableSpinnerUnidadesItem;
    private ArrayList<Pieza> alPieza;
    private Pieza piezaSeleccionada;
    final int MY_PERMISSIONS_REQUEST = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_item_envio);

        searchableSpinnerNomItem = (SearchableSpinner)findViewById(R.id.searchable_spinner_nombre_item_envio);
        searchableSpinnerCodigoItem = (SearchableSpinner)findViewById(R.id.searchable_spinner_codigo_item_envio);


        unidadesET = findViewById(R.id.unidades_envio);


        alPieza = new ArrayList<>();

        //recibir el modelo
        reporte = (ReporteEnvio) getIntent().getExtras().getSerializable("reporte");



        // Relacioinar con el xml

        fotoItemBtn = (Button) this.findViewById(R.id.foto_item_envio_btn);
        imageViewFotoItem = (ImageView) this.findViewById(R.id.imagen_foto_envio);


        if (ContextCompat.checkSelfPermission(AgregarItemEnvioActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(AgregarItemEnvioActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(AgregarItemEnvioActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST);
            } else {

                ActivityCompat.requestPermissions(AgregarItemEnvioActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST);
            }
        } else {
            // Permission has already been granted
            // Añadir el listener al boton foto item

        }


        fotoItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // creamos el Intent para llamar a la camara
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                //crear carpeta en la memoria del terminal
                File imagenesDoka = new File(Environment.getExternalStorageDirectory(), "FotosDoka");
                imagenesDoka.mkdirs();

                Long tsLong = System.currentTimeMillis()/1000;
                String ts = tsLong.toString();

                //añadir el nombre de la imagen
                File image = new File(imagenesDoka, ts+".jpg");
                Uri myImagesdir = Uri.parse("content://" + image );
                //Uri uriSavedImage = Uri.fromFile(image);

                piezaSeleccionada.setFotoItemResumen(myImagesdir.toString());

                //Le decimos al intent que queremos grabar la imagen
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, myImagesdir);
                cameraIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);


                //Lanzamos la aplicacion de la camara con retorno (forResult)
                startActivityForResult(cameraIntent, 1);
            }
        });
        continuarBtn = findViewById(R.id.continuar_envio_btn);
        continuarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (unidadesET.getText().toString() != ""){
                    piezaSeleccionada.setUnidades(Integer.parseInt(unidadesET.getText().toString()));
                    reporte.getAlPiezas().add(piezaSeleccionada);
                    Intent intent = new Intent(getApplicationContext(), AgregarItemEnvioActivity.class);
                    intent.putExtra("reporte", reporte);
                    startActivity(intent);
                }else{
                    //todo mandar error
                }

            }
        });

        searchableSpinnerNomItem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                piezaSeleccionada = alPieza.get(i);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        //Init DB
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("material")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("hola2", document.getId() + " => " + document.getData());

                                alPieza.add(new Pieza(document.getId(),document.getString("descripcion"),document.getString("codigo"),document.getString("pais")));
                            }
                            onFirebaseLoadSuccess(alPieza);
                        } else {
                            Log.w("hola2", "Error getting documents.", task.getException());
                        }
                    }
                });



    }

    @Override
    public void onFirebaseLoadSuccess(List<Pieza> piezaList) {
        List<String> nombresPiezas = new ArrayList<>();
        List<String> codigosPiezas = new ArrayList<>();
        for (Pieza piezaSeleccionada : piezaList){
            nombresPiezas.add(piezaSeleccionada.getDescripcion());
            codigosPiezas.add(piezaSeleccionada.getCodigo());

        }


        // Create adapter and set for spinner
        ArrayAdapter<String> adapterNomPieza = new ArrayAdapter<>( this, android.R.layout.simple_expandable_list_item_1,nombresPiezas);
        searchableSpinnerNomItem.setAdapter(adapterNomPieza);
        ArrayAdapter<String> adapterCodigoPieza = new ArrayAdapter<>( this, android.R.layout.simple_expandable_list_item_1,codigosPiezas);
        searchableSpinnerCodigoItem.setAdapter(adapterCodigoPieza);

    }


    @Override
    public void onFirebaseLoadFailed(String message) {

    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // Comprobar que la foto se realizo
        if ( resultCode == RESULT_OK) {
            Bitmap bMap;
            switch (requestCode) {
                case 1:   // Creamos un bitmap con la imagen recientemente almacenada en la memmoria
                    bMap = BitmapFactory.decodeFile(
                            piezaSeleccionada.getFotoItemResumen());
                    imageViewFotoItem.setImageURI(Uri.parse(piezaSeleccionada.getFotoItemResumen()));
                    imageViewFotoItem.setImageBitmap(bMap);

                    //Añadimos el bitmap al imageView para mostrarlo por pantalla

                    break;
            }
        }}

    @Override
    public void onFirebaseLoadSucces(List<Pieza> piezaList) {

    }


}