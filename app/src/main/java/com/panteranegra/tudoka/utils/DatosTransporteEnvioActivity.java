package com.panteranegra.tudoka.utils;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.panteranegra.tudoka.AgregarItemDevolucionActivity;
import com.panteranegra.tudoka.DatosTransporteEnvioActivity;
import com.panteranegra.tudoka.DrawImageActivity;
import com.panteranegra.tudoka.Model.ReporteDevolucion;
import com.panteranegra.tudoka.R;

import java.io.File;

import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import pl.aprilapps.easyphotopicker.MediaFile;
import pl.aprilapps.easyphotopicker.MediaSource;

import static com.panteranegra.tudoka.utils.ImageUtils.compressBitmap;

public class DatosTransporteEnvioActivity extends AppCompatActivity {

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
    private EasyImage easy_image;
    int image_code = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reporte = new ReporteDevolucion();
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        setContentView(R.layout.activity_datos_transporte_envio);


        if (ContextCompat.checkSelfPermission(DatosTransporteEnvioActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(DatosTransporteEnvioActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(DatosTransporteEnvioActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_CALL_PHONE);
            } else {

                ActivityCompat.requestPermissions(DatosTransporteEnvioActivity.this,
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

        // Inicializar selector de imágenes o toma de fotos
        easy_image = new EasyImage.Builder(getBaseContext())
                .setCopyImagesToPublicGalleryFolder(true)
                .setFolderName("FotosDoka")
                .allowMultiple(false)
                .build();

        imagen_licencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!new File(reporte.getFotoLicencia()).exists())
                    return;

                Intent intent = new Intent(getApplicationContext(), DrawImageActivity.class);
                intent.putExtra("path", reporte.getFotoLicencia());
                startActivity(intent);
            }
        });

        //Añadir el listener al Boton foto licencia
        btn_tomar_foto_licencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Para evitar que se pueda intentar abrir varias veces el selector, revisamos si no hay algún otro abierto
                if (image_code != 0)
                    return;

                // Guardamos la imagen a obtener
                image_code = 1;
                // Abrimos el dialogo para escoger entre tomar una foto o elegir una existente
                easy_image.openChooser(DatosTransporteEnvioActivity.this);
            }
        });

        imagen_placa_delanera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!new File(reporte.getFotoPlacaDelantera()).exists())
                    return;

                Intent intent = new Intent(getApplicationContext(), DrawImageActivity.class);
                intent.putExtra("path", reporte.getFotoPlacaDelantera());
                startActivity(intent);
            }
        });

        //Añadir el listener al Boton foto placa delantera
        btn_tomar_foto_placa_delantera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Para evitar que se pueda intentar abrir varias veces el selector, revisamos si no hay algún otro abierto
                if (image_code != 0)
                    return;

                // Guardamos la imagen a obtener
                image_code = 2;
                // Abrimos el dialogo para escoger entre tomar una foto o elegir una existente
                easy_image.openChooser(DatosTransporteEnvioActivity.this);
            }
        });

        imagen_placa_trasera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!new File(reporte.getFotoPlacaTrasera()).exists())
                    return;

                Intent intent = new Intent(getApplicationContext(), DrawImageActivity.class);
                intent.putExtra("path", reporte.getFotoPlacaTrasera());
                startActivity(intent);
            }
        });

        //Añadir el listener al Boton foto placa trasera
        btn_tomar_foto_placa_trasera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Para evitar que se pueda intentar abrir varias veces el selector, revisamos si no hay algún otro abierto
                if (image_code != 0)
                    return;

                // Guardamos la imagen a obtener
                image_code = 3;
                // Abrimos el dialogo para escoger entre tomar una foto o elegir una existente
                easy_image.openChooser(DatosTransporteEnvioActivity.this);
            }
        });

        imagen_tracto_lateral1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!new File(reporte.getFotoTractoLateral1()).exists())
                    return;

                Intent intent = new Intent(getApplicationContext(), DrawImageActivity.class);
                intent.putExtra("path", reporte.getFotoTractoLateral1());
                startActivity(intent);
            }
        });

        //Añadir el listener al Boton tracto lateral 1
        btn_tomar_foto_tracto_lateral_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Para evitar que se pueda intentar abrir varias veces el selector, revisamos si no hay algún otro abierto
                if (image_code != 0)
                    return;

                // Guardamos la imagen a obtener
                image_code = 4;
                // Abrimos el dialogo para escoger entre tomar una foto o elegir una existente
                easy_image.openChooser(DatosTransporteEnvioActivity.this);
            }
        });

        imagen_tracto_lateral2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!new File(reporte.getFotoTractoLateral2()).exists())
                    return;

                Intent intent = new Intent(getApplicationContext(), DrawImageActivity.class);
                intent.putExtra("path", reporte.getFotoTractoLateral2());
                startActivity(intent);
            }
        });

        //Añadir el listener al Boton tracto lateral 2
        btn_tomar_foto_tracto_lateral_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Para evitar que se pueda intentar abrir varias veces el selector, revisamos si no hay algún otro abierto
                if (image_code != 0)
                    return;

                // Guardamos la imagen a obtener
                image_code = 5;
                // Abrimos el dialogo para escoger entre tomar una foto o elegir una existente
                easy_image.openChooser(DatosTransporteEnvioActivity.this);
            }
        });

        imagen_tracto_parte_trasera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!new File(reporte.getFotoTractoParteTrasera()).exists())
                    return;

                Intent intent = new Intent(getApplicationContext(), DrawImageActivity.class);
                intent.putExtra("path", reporte.getFotoTractoParteTrasera());
                startActivity(intent);
            }
        });

        //Añadir el listener al Boton tracto parte Trasera
        btn_tomar_foto_tracto_parte_trasera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Para evitar que se pueda intentar abrir varias veces el selector, revisamos si no hay algún otro abierto
                if (image_code != 0)
                    return;

                // Guardamos la imagen a obtener
                image_code = 6;
                // Abrimos el dialogo para escoger entre tomar una foto o elegir una existente
                easy_image.openChooser(DatosTransporteEnvioActivity.this);
            }
        });

        imagen_documento_cliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!new File(reporte.getFotoDocumentoCliente()).exists())
                    return;

                Intent intent = new Intent(getApplicationContext(), DrawImageActivity.class);
                intent.putExtra("path", reporte.getFotoDocumentoCliente());
                startActivity(intent);
            }
        });


        //Añadir el listener al Boton Documento Cliente
        btn_tomar_foto_documento_cliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Para evitar que se pueda intentar abrir varias veces el selector, revisamos si no hay algún otro abierto
                if (image_code != 0)
                    return;

                // Guardamos la imagen a obtener
                image_code = 7;
                // Abrimos el dialogo para escoger entre tomar una foto o elegir una existente
                easy_image.openChooser(DatosTransporteEnvioActivity.this);
            }
        });

        // Añadir listener al boton Continuar
        btn_continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AgregarItemDevolucionActivity.class);
                //para pasar el modelo
                intent.putExtra("reporte", reporte);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        easy_image.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onMediaFilesPicked(MediaFile[] imageFiles, MediaSource source) {
                try {
                    // Como solamente pedimos un archivo, tomamos el primero del arreglo
                    MediaFile mediaFile = imageFiles[0];
                    // Creamos un bitmap con la imagen recientemente almacenada en la memmoria
                    Bitmap bMap = BitmapFactory.decodeFile(mediaFile.getFile().getAbsolutePath());
                    // Verificamos que foto es la que se tomó
                    switch (image_code) {
                        case 1:
                            reporte.setFotoLicencia(mediaFile.getFile().getAbsolutePath());
                            // Añadimos el bitmap al imageView para mostrarlo por pantalla
                            imagen_licencia.setImageBitmap(compressBitmap(bMap, 480, (480*bMap.getHeight()/bMap.getWidth())));
                            break;
                        case 2:
                            reporte.setFotoPlacaDelantera(mediaFile.getFile().getAbsolutePath());
                            // Añadimos el bitmap al imageView para mostrarlo por pantalla
                            imagen_placa_delanera.setImageBitmap(compressBitmap(bMap, 480, (480*bMap.getHeight()/bMap.getWidth())));
                            break;
                        case 3:
                            reporte.setFotoPlacaTrasera(mediaFile.getFile().getAbsolutePath());
                            // Añadimos el bitmap al imageView para mostrarlo por pantalla
                            imagen_placa_trasera.setImageBitmap(compressBitmap(bMap, 480, (480*bMap.getHeight()/bMap.getWidth())));
                            break;
                        case 4:
                            reporte.setFotoTractoLateral1(mediaFile.getFile().getAbsolutePath());
                            // Añadimos el bitmap al imageView para mostrarlo por pantalla
                            imagen_tracto_lateral1.setImageBitmap(compressBitmap(bMap, 480, (480*bMap.getHeight()/bMap.getWidth())));
                            break;
                        case 5:
                            reporte.setFotoTractoLateral2(mediaFile.getFile().getAbsolutePath());
                            // Añadimos el bitmap al imageView para mostrarlo por pantalla
                            imagen_tracto_lateral2.setImageBitmap(compressBitmap(bMap, 480, (480*bMap.getHeight()/bMap.getWidth())));
                            break;
                        case 6:
                            reporte.setFotoTractoParteTrasera(mediaFile.getFile().getAbsolutePath());
                            // Añadimos el bitmap al imageView para mostrarlo por pantalla
                            imagen_tracto_parte_trasera.setImageBitmap(compressBitmap(bMap, 480, (480*bMap.getHeight()/bMap.getWidth())));
                            break;
                        case 7:
                            reporte.setFotoDocumentoCliente(mediaFile.getFile().getAbsolutePath());
                            // Añadimos el bitmap al imageView para mostrarlo por pantalla
                            imagen_documento_cliente.setImageBitmap(compressBitmap(bMap, 480, (480*bMap.getHeight()/bMap.getWidth())));
                            break;
                        default:
                            break;
                    }

                    // Permitimos que alguien más abra el selector
                    image_code = 0;
                } catch (Exception ex){
                    // Permitimos que alguien más abra el selector
                    image_code = 0;
                    ex.printStackTrace();
                }
            }

            @Override
            public void onImagePickerError(@NonNull Throwable error, @NonNull MediaSource source) {
                error.printStackTrace();
                // Permitimos que alguien más abra el selector
                image_code = 0;
            }

            @Override
            public void onCanceled(@NonNull MediaSource source) {
                // Permitimos que alguien más abra el selector
                image_code = 0;
            }
        });
    }


}
