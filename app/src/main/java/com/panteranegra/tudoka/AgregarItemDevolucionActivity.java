package com.panteranegra.tudoka;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.panteranegra.tudoka.Interface.IfFirebaseLoadDonePieza;
import com.panteranegra.tudoka.Model.Cliente;
import com.panteranegra.tudoka.Model.Pieza;
import com.panteranegra.tudoka.Model.Proyecto;
import com.panteranegra.tudoka.Model.ReporteDevolucion;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import pl.aprilapps.easyphotopicker.MediaFile;
import pl.aprilapps.easyphotopicker.MediaSource;

import static com.panteranegra.tudoka.utils.ImageUtils.compressBitmap;

public class AgregarItemDevolucionActivity extends AppCompatActivity implements IfFirebaseLoadDonePieza {

    Button btn_tomar_foto_item;
    Button btn_continuar;
    ImageView imagen_item;
    ReporteDevolucion reporte;
    SearchableSpinner searchableSpinnerNomItem, searchableSpinnerCodigoItem, searchableSpinnerUnidadesItem;
    private Pieza piezaSeleccionada;
    private ArrayList<Pieza> alPieza;
    private EasyImage easy_image;
    //TextInputEditText numUnidadesDevueltas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_item_devolucion);
        alPieza = new ArrayList<>();
        searchableSpinnerNomItem = (SearchableSpinner)findViewById(R.id.searchable_spinner_nombre_item);
        searchableSpinnerCodigoItem = (SearchableSpinner)findViewById(R.id.searchable_spinner_codigo_item);
        searchableSpinnerUnidadesItem = (SearchableSpinner)findViewById(R.id.searchable_spinner_numero_item);
        searchableSpinnerNomItem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int posicion, long l) {

                piezaSeleccionada = alPieza.get(posicion);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        searchableSpinnerCodigoItem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int posicion, long l) {
                piezaSeleccionada = alPieza.get(posicion);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        String  newString;

        // Inicializar selector de imágenes o toma de fotos
        easy_image = new EasyImage.Builder(getBaseContext())
                .setCopyImagesToPublicGalleryFolder(true)
                .setFolderName("FotosDoka")
                .allowMultiple(false)
                .build();

        //recibir el modelo
        reporte = (ReporteDevolucion) getIntent().getExtras().getSerializable("reporte");

       // Intent intent = new Intent(getApplicationContext(), ResumenItemsActivity.class);
        //startActivity(intent);

        // Relacioinar con el xml

        btn_tomar_foto_item = (Button) this.findViewById(R.id.fotoItemBtn);
        imagen_item = (ImageView) this.findViewById(R.id.imageViewFotoItem);
        btn_continuar = (Button) this.findViewById(R.id.continuar_btn);
        //numUnidadesDevueltas = findViewById(R.id.textInputNumPiezas);

        imagen_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!new File(piezaSeleccionada.getFotoItemResumen()).exists())
                    return;

                Intent intent = new Intent(getApplicationContext(), DrawImageActivity.class);
                intent.putExtra("path", piezaSeleccionada.getFotoItemResumen());
                startActivity(intent);
            }
        });

        // Añadir el listener al boton foto item
        btn_tomar_foto_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Abrimos el dialogo para escoger entre tomar una foto o elegir una existente
                easy_image.openChooser(AgregarItemDevolucionActivity.this);
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


                                alPieza.add(new Pieza("","tuerca","",""));

                            }
                            onFirebaseLoadSuccess(alPieza);
                        } else {
                            Log.w("hola2", "Error getting documents.", task.getException());
                        }
                    }
                });

        // Añadir listener al boton Continuar
        btn_continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ResumenItemsActivity.class);
                startActivity(intent);
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
            // Regresa el archivo seleccionado  o foto tomada
            @Override
            public void onMediaFilesPicked(MediaFile[] imageFiles, MediaSource source) {
                try {
                    // Como solamente pedimos un archivo, tomamos el primero del arreglo
                    MediaFile mediaFile = imageFiles[0];
                    piezaSeleccionada.setFotoItemResumen(mediaFile.getFile().getAbsolutePath());

                    // Creamos un bitmap con la imagen recientemente almacenada en la memmoria y lo añadimos al imageView para mostrarlo por pantalla
                    Bitmap bMap = BitmapFactory.decodeFile(mediaFile.getFile().getAbsolutePath());
                    imagen_item.setImageBitmap(compressBitmap(bMap, 480, (480*bMap.getHeight()/bMap.getWidth())));
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