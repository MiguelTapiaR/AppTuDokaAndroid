package com.panteranegra.tudoka.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.panteranegra.tudoka.R;

import java.util.ArrayList;

import static com.panteranegra.tudoka.utils.ImageUtils.compressBitmap;

public class NumerosRemisionAdapters extends ArrayAdapter {
    private Context context;
    private ArrayList<String> datos;

    public NumerosRemisionAdapters(Context context, ArrayList<String> datos){
        super(context, R.layout.renglon_numero_remision, datos);

        this.context = context;
        this.datos = datos;
    }

    @Override
    public View getView (final int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = LayoutInflater.from(context);
        View item = inflater.inflate(R.layout.renglon_numero_remision,null);

       TextView nombre = (TextView) item.findViewById(R.id.tv_numero_remision);
        nombre.setText(datos.get(position));
        Button eliminar = item.findViewById(R.id.btn_eliminar_numero_remision);
        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datos.remove(position);
            }
        });


        return item;

    }
}
