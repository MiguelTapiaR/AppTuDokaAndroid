package com.panteranegra.tudoka.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.panteranegra.tudoka.Model.Pieza;
import com.panteranegra.tudoka.R;

import java.util.ArrayList;

import pl.aprilapps.easyphotopicker.MediaFile;

import static com.panteranegra.tudoka.utils.ImageUtils.compressBitmap;

public class ItemsAdapter extends ArrayAdapter {
    private Context context;
    private ArrayList<Pieza> datos;

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

        Bitmap bMap = BitmapFactory.decodeFile(datos.get(position).getFotoItemResumen());
        //imagen.setImageResource(datos.get(position).getDrawableImageID());
        imagen.setImageBitmap(compressBitmap(bMap, 480, (480*bMap.getHeight()/bMap.getWidth())));

        TextView nombre = (TextView) item.findViewById(R.id.textViewNombreItem);
        nombre.setText(datos.get(position).getDescripcion());
        TextView codigo = (TextView) item.findViewById(R.id.textViewCoodigoItem);
        codigo.setText(datos.get(position).getCodigo());
        TextView unidades = (TextView) item.findViewById(R.id.textViewUnidadesItem);
        unidades.setText(""+datos.get(position).getUnidades());

        return item;

    }
}
