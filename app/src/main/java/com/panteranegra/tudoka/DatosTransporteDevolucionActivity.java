package com.panteranegra.tudoka;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.panteranegra.tudoka.Model.ReporteDevolucion;

import java.io.File;

public class DatosTransporteDevolucionActivity extends AppCompatActivity {

    Button btn_tomar_foto_licencia;
    ImageView imagen_licencia;
    final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 0;
    ReporteDevolucion reporte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reporte = new ReporteDevolucion();
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        setContentView(R.layout.activity_datos_transporte_devolucion);

        if (ContextCompat.checkSelfPermission(DatosTransporteDevolucionActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(DatosTransporteDevolucionActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(DatosTransporteDevolucionActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_CALL_PHONE);
            } else {

                ActivityCompat.requestPermissions(DatosTransporteDevolucionActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_CALL_PHONE);
            }
        } else {
            // Permission has already been granted
        }

        //Relacionar con el XML
        imagen_licencia = (ImageView) this.findViewById(R.id.imageLicencia);
        btn_tomar_foto_licencia = (Button) this.findViewById(R.id.fotoLicenciaBtn);

        //Añadir el listener Boton
        btn_tomar_foto_licencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // creamos el Intent para llamara a la camara
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

                //crear carpeta en la memoria del terminal
                File imagenesDoka = new File(Environment.getExternalStorageDirectory(), "FotosDoka");
                imagenesDoka.mkdirs();

                Long tsLong = System.currentTimeMillis()/1000;
                String ts = tsLong.toString();

                //añadir el nombre de la imagen
                File image = new File(imagenesDoka, ts+".jpg");
                Uri uriSavedImage = Uri.fromFile(image);

                reporte.setFotoLicencia(uriSavedImage);

                //Le decimos al intent que queremos grabar la imagen
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);

                //Lanzamos la aplicacion de la camara con retorno (forResult)
                startActivityForResult(cameraIntent, 1);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // Comprobar que la foto se realizo
        if (requestCode == 1 && resultCode == RESULT_OK) {
            // Creamos un bitmap con la imagen recientemente almacenada en la memmoria
            Bitmap bMap = BitmapFactory.decodeFile(
                    reporte.getFotoLicencia().getEncodedPath() );

            //Añadimos el bitmap al imageView para mostrarlo por pantalla
            imagen_licencia.setImageBitmap(bMap);
        }
    }


}
