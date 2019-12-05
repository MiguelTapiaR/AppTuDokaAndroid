package com.panteranegra.tudoka;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.panteranegra.tudoka.Adapters.ItemsAdapter;
import com.panteranegra.tudoka.Adapters.RenglonListasCargaAdapter;
import com.panteranegra.tudoka.Model.ReporteEnvio;
import com.panteranegra.tudoka.utils.DatosTransporteEnvioActivity;

import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import pl.aprilapps.easyphotopicker.MediaFile;
import pl.aprilapps.easyphotopicker.MediaSource;

import static com.panteranegra.tudoka.utils.ImageUtils.compressBitmap;

public class ListasCargasEnvioActivity extends AppCompatActivity {

    private EasyImage easy_image;

    ReporteEnvio reporte;

    ListView lista;
    RenglonListasCargaAdapter adapter;

    Button btnContinuar, btnNuevaListaCarga;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listas_carga_envio);

        reporte = (ReporteEnvio) getIntent().getExtras().getSerializable("reporte");

        easy_image = new EasyImage.Builder(getBaseContext())
                .setCopyImagesToPublicGalleryFolder(true)
                .setFolderName("FotosDoka")
                .allowMultiple(false)
                .build();

        lista = findViewById(R.id.lista_lista_carga);
        adapter = new RenglonListasCargaAdapter(getApplicationContext(),reporte.getAlListasCarga());
        lista.setAdapter(adapter);


        btnContinuar = findViewById(R.id.btn_continuar_lista_carga);
        btnContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DatosTransporteEnvioActivity.class);
                //para pasar el modelo
                intent.putExtra("reporte", reporte);
                startActivity(intent);
            }
        });

        btnNuevaListaCarga = findViewById(R.id.btn_nueva_lista_carga);

        btnNuevaListaCarga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                easy_image.openChooser(ListasCargasEnvioActivity.this);
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
//                    piezaSeleccionada.setFotoItemResumen(mediaFile.getFile().getAbsolutePath());
                    reporte.getAlListasCarga().add(mediaFile.getFile().getAbsolutePath());
                    adapter.notifyDataSetChanged();
                    // Creamos un bitmap con la imagen recientemente almacenada en la memmoria y lo añadimos al imageView para mostrarlo por pantalla
                    Bitmap bMap = BitmapFactory.decodeFile(mediaFile.getFile().getAbsolutePath());
//                    imageViewFotoItem.setImageBitmap(compressBitmap(bMap, 480, (480*bMap.getHeight()/bMap.getWidth())));
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