package com.panteranegra.tudoka.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.panteranegra.tudoka.R;

import java.util.ArrayList;

public class ItemsAdapter extends ArrayAdapter {
    private Context context;
    private ArrayList datos;

    public ItemsAdapter(Context context, ArrayList datos){
        super(context, R.layout.activity_vista_item, datos);

        this.context = context;
        this.datos = datos;
    }

    @Override
    public View getView (int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = LayoutInflater.from(context);
        View item = inflater.inflate(R.layout.activity_vista_item,null);

        ImageView imagen = (ImageView) item.findViewById(R.id.imageViewFotoItem);
        //imagen.setImageResource(datos.get(position).getDrawableImageID());

        TextView nombre = (TextView) item.findViewById(R.id.textViewNombreItem);
        nombre.setText("hola");
        TextView codigo = (TextView) item.findViewById(R.id.textViewCoodigoItem);
       // nombre.setText(datos.get(position).getCodigo());

        return item;

    }
}
