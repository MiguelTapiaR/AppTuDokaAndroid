package com.panteranegra.tudoka.Model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Auxiliares {

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
}
