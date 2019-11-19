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

import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import pl.aprilapps.easyphotopicker.MediaFile;
import pl.aprilapps.easyphotopicker.MediaSource;

import static com.panteranegra.tudoka.utils.ImageUtils.compressBitmap;

public class AgregarItemEnvioActivity extends AppCompatActivity implements IfFirebaseLoadDonePieza {

    Button fotoItemBtn, continuarBtn;
    EditText unidadesET;
    ImageView imageViewFotoItem;
    ReporteEnvio reporte;
    SearchableSpinner searchableSpinnerNomItem, searchableSpinnerCodigoItem, searchableSpinnerUnidadesItem;
    private ArrayList<Pieza> alPieza;
    private Pieza piezaSeleccionada;
    final int MY_PERMISSIONS_REQUEST = 0;
    private EasyImage easy_image;

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

        easy_image = new EasyImage.Builder(getBaseContext())
                .setCopyImagesToPublicGalleryFolder(true)
                .setFolderName("FotosDoka")
                .allowMultiple(false)
                .build();

        imageViewFotoItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!new File(piezaSeleccionada.getFotoItemResumen()).exists())
                    return;

                Intent intent = new Intent(getApplicationContext(), DrawImageActivity.class);
                intent.putExtra("path", piezaSeleccionada.getFotoItemResumen());
                startActivity(intent);
            }
        });

        fotoItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Abrimos el dialogo para escoger entre tomar una foto o elegir una existente
                easy_image.openChooser(AgregarItemEnvioActivity.this);
            }
        });
        continuarBtn = findViewById(R.id.continuar_envio_btn);
        continuarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (unidadesET.getText().toString() != ""){
                    piezaSeleccionada.setUnidades(Integer.parseInt(unidadesET.getText().toString()));
                    reporte.getAlPiezas().add(piezaSeleccionada);
                    Intent intent = new Intent(getApplicationContext(), ResumenItemsActivity.class);
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
        easy_image.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onMediaFilesPicked(MediaFile[] imageFiles, MediaSource source) {
                try {
                    // Como solamente pedimos un archivo, tomamos el primero del arreglo
                    MediaFile mediaFile = imageFiles[0];
                    piezaSeleccionada.setFotoItemResumen(mediaFile.getFile().getAbsolutePath());

                    // Creamos un bitmap con la imagen recientemente almacenada en la memmoria y lo añadimos al imageView para mostrarlo por pantalla
                    Bitmap bMap = BitmapFactory.decodeFile(mediaFile.getFile().getAbsolutePath());
                    imageViewFotoItem.setImageBitmap(compressBitmap(bMap, 480, (480*bMap.getHeight()/bMap.getWidth())));
                } catch (Exception ex){
                    ex.printStackTrace();
                }
            }

            @Override
            public void onImagePickerError(@NonNull Throwable error, @NonNull MediaSource source) {
                //Some error handling
                error.printStackTrace();
            }

            @Override
            public void onCanceled(@NonNull MediaSource source) {
            }
        });
    }

    @Override
    public void onFirebaseLoadSucces(List<Pieza> piezaList) {

    }


}