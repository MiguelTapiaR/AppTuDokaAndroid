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
    Button btn_tomar_foto_placa_delantera;
    Button btn_tomar_foto_placa_trasera;
    Button btn_tomar_foto_tracto_lateral_1;
    Button btn_tomar_foto_tracto_lateral_2;
    Button btn_tomar_foto_tracto_parte_trasera;
    Button btn_tomar_foto_documento_cliente;
    ImageView imagen_licencia;
    ImageView imagen_placa_delanera;
    ImageView imagen_placa_trasera;
    ImageView imagen_tracto_lateral1;
    ImageView imagen_tracto_lateral2;
    ImageView imagen_tracto_parte_trasera;
    ImageView imagen_documento_cliente;
    final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 0;
    ReporteDevolucion reporte;
    private Button btn_continuar;

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
        imagen_placa_delanera = (ImageView) this.findViewById(R.id.imagePlacaDelantera);
        imagen_placa_trasera = (ImageView) this.findViewById(R.id.imagePlacaTrasera);
        imagen_tracto_lateral1 = (ImageView) this.findViewById(R.id.imageTractoLateral1);
        imagen_tracto_lateral2 = (ImageView) this.findViewById(R.id.imageTractoLateral2);
        imagen_tracto_parte_trasera = (ImageView) this.findViewById(R.id.imageTractoParteTrasera);
        imagen_documento_cliente = (ImageView) this.findViewById(R.id.imageDocumentoCliente);
        btn_tomar_foto_licencia = (Button) this.findViewById(R.id.fotoLicenciaBtn);
        btn_tomar_foto_placa_delantera = (Button) this.findViewById(R.id.fotoPlacaDelanteraBtn);
        btn_tomar_foto_placa_trasera = (Button) this.findViewById(R.id.fotoPlacaTraseraBtn);
        btn_tomar_foto_tracto_lateral_1 = (Button) this.findViewById(R.id.fotoTractoLateral1Btn);
        btn_tomar_foto_tracto_lateral_2 = (Button) this.findViewById(R.id.fotoTractoLateral2Btn);
        btn_tomar_foto_tracto_parte_trasera = (Button) this.findViewById(R.id.fotoTractoTraseraBtn);
        btn_tomar_foto_documento_cliente = (Button) this.findViewById(R.id.fotoDocumentoClienteBtn);
        btn_continuar = findViewById(R.id.datosTransporteContinuarBtn);

        //Añadir el listener al Boton foto licencia
        btn_tomar_foto_licencia.setOnClickListener(new View.OnClickListener() {
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

                reporte.setFotoLicencia(uriSavedImage);

                //Le decimos al intent que queremos grabar la imagen
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);

                //Lanzamos la aplicacion de la camara con retorno (forResult)
                startActivityForResult(cameraIntent, 1);
            }
        });

        //Añadir el listener al Boton foto placa delantera
        btn_tomar_foto_placa_delantera.setOnClickListener(new View.OnClickListener() {
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

                reporte.setFotoPlacaDelantera(uriSavedImage);

                //Le decimos al intent que queremos grabar la imagen
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);

                //Lanzamos la aplicacion de la camara con retorno (forResult)
                startActivityForResult(cameraIntent, 2);
            }
        });

        //Añadir el listener al Boton foto placa trasera
        btn_tomar_foto_placa_trasera.setOnClickListener(new View.OnClickListener() {
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

                reporte.setFotoPlacaTrasera(uriSavedImage);

                //Le decimos al intent que queremos grabar la imagen
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);

                //Lanzamos la aplicacion de la camara con retorno (forResult)
                startActivityForResult(cameraIntent, 3);
            }
        });

        //Añadir el listener al Boton tracto lateral 1
        btn_tomar_foto_tracto_lateral_1.setOnClickListener(new View.OnClickListener() {
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

                reporte.setFotoTractoLateral1(uriSavedImage);

                //Le decimos al intent que queremos grabar la imagen
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);

                //Lanzamos la aplicacion de la camara con retorno (forResult)
                startActivityForResult(cameraIntent, 4);
            }
        });

        //Añadir el listener al Boton tracto lateral 2
        btn_tomar_foto_tracto_lateral_2.setOnClickListener(new View.OnClickListener() {
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

                reporte.setFotoTractoLateral2(uriSavedImage);

                //Le decimos al intent que queremos grabar la imagen
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);

                //Lanzamos la aplicacion de la camara con retorno (forResult)
                startActivityForResult(cameraIntent, 5);
            }
        });

        //Añadir el listener al Boton tracto parte Trasera
        btn_tomar_foto_tracto_parte_trasera.setOnClickListener(new View.OnClickListener() {
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

                reporte.setFotoTractoParteTrasera(uriSavedImage);

                //Le decimos al intent que queremos grabar la imagen
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);

                //Lanzamos la aplicacion de la camara con retorno (forResult)
                startActivityForResult(cameraIntent, 6);
            }
        });

        //Añadir el listener al Boton Documento Cliente
        btn_tomar_foto_documento_cliente.setOnClickListener(new View.OnClickListener() {
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

                reporte.setFotoDocumentoCliente(uriSavedImage);

                //Le decimos al intent que queremos grabar la imagen
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);

                //Lanzamos la aplicacion de la camara con retorno (forResult)
                startActivityForResult(cameraIntent, 7);
            }
        });

        // Añadir listener al boton Continuar
        btn_continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AgregarItemDevolucionActivity.class);
                //para pasar el modelo
                intent.putExtra("reporte",reporte);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // Comprobar que la foto se realizo
        if ( resultCode == RESULT_OK) {
            Bitmap bMap;
                switch (requestCode){
                    case 1:   // Creamos un bitmap con la imagen recientemente almacenada en la memmoria
                                bMap = BitmapFactory.decodeFile(
                                reporte.getFotoLicencia().getEncodedPath() );

                                //Añadimos el bitmap al imageView para mostrarlo por pantalla
                                imagen_licencia.setImageBitmap(bMap);
                                break;
                    case 2:     // Creamos un bitmap con la imagen recientemente almacenada en la memmoria
                                bMap = BitmapFactory.decodeFile(
                                reporte.getFotoPlacaDelantera().getEncodedPath() );

                                 //Añadimos el bitmap al imageView para mostrarlo por pantalla
                                 imagen_placa_delanera.setImageBitmap(bMap);
                                 break;
                    case 3:     // Creamos un bitmap con la imagen recientemente almacenada en la memmoria
                        bMap = BitmapFactory.decodeFile(
                                reporte.getFotoPlacaTrasera().getEncodedPath() );

                                //Añadimos el bitmap al imageView para mostrarlo por pantalla
                                imagen_placa_trasera.setImageBitmap(bMap);
                                break;
                    case 4:     // Creamos un bitmap con la imagen recientemente almacenada en la memmoria
                        bMap = BitmapFactory.decodeFile(
                                reporte.getFotoTractoLateral1().getEncodedPath() );

                                //Añadimos el bitmap al imageView para mostrarlo por pantalla
                                imagen_tracto_lateral1.setImageBitmap(bMap);
                                break;
                    case 5:     // Creamos un bitmap con la imagen recientemente almacenada en la memmoria
                        bMap = BitmapFactory.decodeFile(
                                reporte.getFotoTractoLateral2().getEncodedPath() );

                                //Añadimos el bitmap al imageView para mostrarlo por pantalla
                                imagen_tracto_lateral2.setImageBitmap(bMap);
                                break;
                    case 6:     // Creamos un bitmap con la imagen recientemente almacenada en la memmoria
                                bMap = BitmapFactory.decodeFile(
                                reporte.getFotoTractoParteTrasera().getEncodedPath() );

                                 //Añadimos el bitmap al imageView para mostrarlo por pantalla
                                imagen_tracto_parte_trasera.setImageBitmap(bMap);
                                break;
                    case 7:     // Creamos un bitmap con la imagen recientemente almacenada en la memmoria
                        bMap = BitmapFactory.decodeFile(
                                reporte.getFotoDocumentoCliente().getEncodedPath() );

                                //Añadimos el bitmap al imageView para mostrarlo por pantalla
                                imagen_documento_cliente.setImageBitmap(bMap);
                                 break;
                }

        }


    }


}
