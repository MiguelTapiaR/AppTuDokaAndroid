package com.panteranegra.tudoka.Model;

import java.io.Serializable;

public class Actividad implements Serializable {
    String desripcion, imagen, urlFoto;

    public String getDesripcion() {
        return desripcion;
    }

    public void setDesripcion(String desripcion) {
        this.desripcion = desripcion;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getUrlDescarga() {
        return urlFoto;
    }

    public void setUrlDescarga(String urlDescarga) {
        this.urlFoto = urlDescarga;
    }
}
