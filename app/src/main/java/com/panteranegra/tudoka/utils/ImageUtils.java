package com.panteranegra.tudoka.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

public class ImageUtils {
    public static Bitmap compressBitmap(
            Bitmap bitmap,
            int width,
            int height
    ) {
        BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
        bmpFactoryOptions.inJustDecodeBounds = true;

        bitmap = Bitmap.createScaledBitmap(bitmap, width, height,
                true);

        bmpFactoryOptions.inJustDecodeBounds = false;

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);

        return BitmapFactory.decodeByteArray(stream.toByteArray(), 0, stream.toByteArray().length);

    }
}

