package com.panteranegra.tudoka;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class AgregarItemDevolucionActivity extends AppCompatActivity implements IfFirebaseLoadDonePieza {

    Button btn_tomar_foto_item;
    Button btn_continuar;
    ImageView imagen_item;
    ReporteDevolucion reporte;
    SearchableSpinner searchableSpinnerNomItem, searchableSpinnerCodigoItem, searchableSpinnerUnidadesItem;
    Pieza pieza;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_item_devolucion);
        searchableSpinnerNomItem = (SearchableSpinner)findViewById(R.id.searchable_spinner_nombre_item);
        searchableSpinnerCodigoItem = (SearchableSpinner)findViewById(R.id.searchable_spinner_codigo_item);
        searchableSpinnerUnidadesItem = (SearchableSpinner)findViewById(R.id.searchable_spinner_numero_item);
        String  newString;


        //recibir el modelo
        reporte = (ReporteDevolucion) getIntent().getExtras().getSerializable("reporte");

       // Intent intent = new Intent(getApplicationContext(), ResumenItemsActivity.class);
        //startActivity(intent);

        // Relacioinar con el xml

        btn_tomar_foto_item = (Button) this.findViewById(R.id.fotoItemBtn);
        imagen_item = (ImageView) this.findViewById(R.id.imageViewFotoItem);
        btn_continuar = (Button) this.findViewById(R.id.continuar_btn);

        // Añadir el listener al boton foto item
        btn_tomar_foto_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // creamos el Intent para llamar a la camara
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

                //crear carpeta en la memoria del terminal
                File imagenesDoka = new File(Environment.getExternalStorageDirectory(), "FotosDoka");
                imagenesDoka.mkdirs();

                Long tsLong = System.currentTimeMillis()/1000;
                String ts = tsLong.toString();

                //añadir el nombre de la imagen
                File image = new File(imagenesDoka, ts+".jpg");
                Uri uriSavedImage = Uri.fromFile(image);

                reporte.setFotoItem(uriSavedImage);

                //Le decimos al intent que queremos grabar la imagen
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);

                //Lanzamos la aplicacion de la camara con retorno (forResult)
                startActivityForResult(cameraIntent, 1);
            }
        });


        //Init DB
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final ArrayList<Pieza> alPieza = new ArrayList<>();
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
        // Comprobar que la foto se realizo
        if ( resultCode == RESULT_OK) {
            Bitmap bMap;
            switch (requestCode) {
                case 1:   // Creamos un bitmap con la imagen recientemente almacenada en la memmoria
                    bMap = BitmapFactory.decodeFile(
                            reporte.getFotoItem().getEncodedPath());

                    //Añadimos el bitmap al imageView para mostrarlo por pantalla
                    imagen_item.setImageBitmap(bMap);
                    break;
            }
        }}

    @Override
    public void onFirebaseLoadSucces(List<Pieza> piezaList) {

    }

}