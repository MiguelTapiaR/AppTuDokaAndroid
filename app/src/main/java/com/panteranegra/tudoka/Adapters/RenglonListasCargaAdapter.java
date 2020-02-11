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

public class RenglonListasCargaAdapter extends ArrayAdapter {
    private Context context;
    private ArrayList<String> datos;

    public RenglonListasCargaAdapter(Context context, ArrayList<String> datos){
        super(context, R.layout.activity_vista_item, datos);

        this.context = context;
        this.datos = datos;
    }

    @Override
    public View getView (final int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = LayoutInflater.from(context);
        View item = inflater.inflate(R.layout.renglon_listas_carga,null);


        ImageView imagen = (ImageView) item.findViewById(R.id.imagen_lista_carga);
        Bitmap bMap = BitmapFactory.decodeFile(datos.get(position));
        //imagen.setImageResource(datos.get(position).getDrawableImageID());
        imagen.setImageBitmap(compressBitmap(bMap, 480, (480*bMap.getHeight()/bMap.getWidth())));
//        TextView nombre = (TextView) item.findViewById(R.id.textViewNombreItem);
//        nombre.setText("hola");
        Button eliminar = item.findViewById(R.id.btn_eliminar_lista_carga);
        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datos.remove(position);
                notifyDataSetChanged();
            }
        });


        return item;

    }
}
