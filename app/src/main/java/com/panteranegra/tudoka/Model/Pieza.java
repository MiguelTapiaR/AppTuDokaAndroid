package com.panteranegra.tudoka.Model;

import android.net.Uri;

public class Pieza {
    private Uri fotoItemResumen;
    private String id, descripcion, codigo, pais;


    public Pieza(){

    }


    public Pieza(String id, String descripcion, String codigo, String pais){

        this.id = id;
        this.descripcion = descripcion;
        this.codigo = codigo;
        this.pais = pais;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public Uri getFotoItemResumen() {
        return fotoItemResumen;
    }

    public void setFotoItemResumen(Uri fotoItemResumen) {
        this.fotoItemResumen = fotoItemResumen;
    }
}
