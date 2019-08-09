package com.panteranegra.tudoka;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.panteranegra.tudoka.Adapters.ItemsAdapter;

import java.util.ArrayList;


public class ResumenItemsActivity extends AppCompatActivity {
    private ArrayList items;
    ItemsAdapter adapter;

    ListView contenido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resumen_items);
        contenido = findViewById(R.id.list_view_items);
        items= new ArrayList();
        items.add("hola");
        items.add("hola");items.add("hola");items.add("hola");items.add("hola");items.add("hola");items.add("hola");items.add("hola");
        adapter = new ItemsAdapter(getApplicationContext(),items);
        contenido.setAdapter(adapter);
    }
}
