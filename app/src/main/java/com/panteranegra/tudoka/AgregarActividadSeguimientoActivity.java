package com.panteranegra.tudoka;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.panteranegra.tudoka.Model.Actividad;
import com.panteranegra.tudoka.Model.ReporteCapacitacion;
import com.panteranegra.tudoka.Model.ReporteSeguimiento;

import java.io.File;
import java.util.ArrayList;

import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import pl.aprilapps.easyphotopicker.MediaFile;
import pl.aprilapps.easyphotopicker.MediaSource;

import static com.panteranegra.tudoka.utils.ImageUtils.compressBitmap;

public class AgregarActividadSeguimientoActivity extends AppCompatActivity {

    Button fotoItemBtn, continuarBtn;
    EditText descripcion;
    ImageView imageViewFotoItem;
    ReporteSeguimiento reporte;
    private ArrayList<Actividad> alActividad;
    private Actividad actividad;
    final int MY_PERMISSIONS_REQUEST = 0;
    private EasyImage easy_image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_actividad_capacitacion);


        actividad = new Actividad();
        descripcion = findViewById(R.id.descripcion_capacitacion);


        alActividad = new ArrayList<>();

        //recibir el modelo
        reporte = (ReporteSeguimiento) getIntent().getExtras().getSerializable("reporte");



        // Relacioinar con el xml

        fotoItemBtn = (Button) this.findViewById(R.id.btn_nueva_foto_capacitacion);
        imageViewFotoItem = (ImageView) this.findViewById(R.id.imagen_foto_envio);


        if (ContextCompat.checkSelfPermission(AgregarActividadSeguimientoActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(AgregarActividadSeguimientoActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(AgregarActividadSeguimientoActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST);
            } else {

                ActivityCompat.requestPermissions(AgregarActividadSeguimientoActivity.this,
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
                if (!new File(actividad.getImagen()).exists())
                    return;

                Intent intent = new Intent(getApplicationContext(), DrawImageActivity.class);
                intent.putExtra("path", actividad.getImagen());
                startActivity(intent);
            }
        });

        fotoItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Abrimos el dialogo para escoger entre tomar una foto o elegir una existente
                easy_image.openChooser(AgregarActividadSeguimientoActivity.this);
            }
        });
        continuarBtn = findViewById(R.id.btn_continuar_actividad_capacitacion);
        continuarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (descripcion.getText().toString() != ""){
                    actividad.setDesripcion(descripcion.getText().toString());
                    reporte.getAlActividad().add(actividad);
                    Intent intent = new Intent(getApplicationContext(), ResumenActividadesSeguimientoActivity.class);
                    intent.putExtra("reporte", reporte);
                    startActivity(intent);
                }else{
                    //todo mandar error
                }

            }
        });





    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        easy_image.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onMediaFilesPicked(MediaFile[] imageFiles, MediaSource source) {
                try {
                    // Como solamente pedimos un archivo, tomamos el primero del arreglo
                    MediaFile mediaFile = imageFiles[0];
                    actividad.setImagen(mediaFile.getFile().getAbsolutePath());

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



}