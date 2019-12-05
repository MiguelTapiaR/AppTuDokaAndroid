package com.panteranegra.tudoka.Model;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Auxiliares {

    private static String TAG = "Rafa";

    private static final int DEFAULT_MIN_WIDTH_QUALITY = 400;
    private static final String TEMP_IMAGE_NAME = "tempImage";

    public static int minWidthQuality = DEFAULT_MIN_WIDTH_QUALITY;

    public String convertirALtoJSON(ArrayList<Actividad> al){
        JSONObject JSONactividades = new JSONObject();
        int cont =0;
        for (Actividad actividad:al
             ) {

            try {
                JSONObject JSONTemp = new JSONObject();
                JSONTemp.put("descripcion", actividad.desripcion);
                JSONTemp.put("urlFoto", actividad.urlFoto);
                JSONactividades.put("Nombre"+cont,JSONTemp);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            cont++;
        }
        return JSONactividades.toString();
    }
    public String convertirALItemstoJSON(ArrayList<Pieza> al){
        JSONObject JSONactividades = new JSONObject();
        int cont =0;
        for (Pieza actividad:al
        ) {

            try {
                JSONObject JSONTemp = new JSONObject();
                JSONTemp.put("nombre", actividad.getDescripcion());
                JSONTemp.put("codigo", actividad.getCodigo());
                JSONTemp.put("unidades", actividad.getUnidades());
                JSONTemp.put("descripcionDano", actividad.getTipoDano());
                JSONTemp.put("urlFoto", actividad.getUrl());
                JSONactividades.put("Nombre"+cont,JSONTemp);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            cont++;
        }
        return JSONactividades.toString();
    }
    public String convertirALEmailstoJSON(ArrayList<String> al){
        JSONObject JSONactividades = new JSONObject();
        int cont =0;
        for (String string:al
             ) {

            try {
                JSONObject JSONTemp = new JSONObject();
                JSONTemp.put("descripcion", string);

                JSONactividades.put("mail"+cont,JSONTemp);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            cont++;
        }
        return JSONactividades.toString();
    }

    public  Bitmap getImageResized(Context context, Uri selectedImage) {
        Bitmap bm = null;
        int[] sampleSizes = new int[]{5, 3, 2, 1};
        int i = 0;
        do {
            bm = decodeBitmap(context, selectedImage, sampleSizes[i]);
            Log.d(TAG, "resizer: new bitmap width = " + bm.getWidth());
            i++;
        } while (bm.getWidth() < minWidthQuality && i < sampleSizes.length);
        return bm;
    }


    private static Bitmap decodeBitmap(Context context, Uri theUri, int sampleSize) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = sampleSize;

        AssetFileDescriptor fileDescriptor = null;
        try {
            fileDescriptor = context.getContentResolver().openAssetFileDescriptor(theUri, "r");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Bitmap actuallyUsableBitmap = BitmapFactory.decodeFileDescriptor(
                fileDescriptor.getFileDescriptor(), null, options);

        Log.d(TAG, options.inSampleSize + " sample method bitmap ... " +
                actuallyUsableBitmap.getWidth() + " " + actuallyUsableBitmap.getHeight());

        return actuallyUsableBitmap;
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
}
