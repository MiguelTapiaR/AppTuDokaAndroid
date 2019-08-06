package com.panteranegra.tudoka.Model;

import android.net.Uri;

public class ReporteDevolucion {
    public ReporteDevolucion() {
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Proyecto getProyecto() {
        return proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }



    private Cliente cliente;
    private Proyecto proyecto;

    public Uri getFotoLicencia() {
        return fotoLicencia;
    }

    public void setFotoLicencia(Uri fotoLicencia) {
        this.fotoLicencia = fotoLicencia;
    }

    private Uri fotoLicencia;
   /* private  fotoPlaca UIImage
    private  fotoTracto UIImage
    private  fotoDocumentoDevolucion UIImage
    private  urlfotoLicencia String
    private  urlfotoPlaca String
    private  urlfotoTracto String
    private  urlfotoDocumentoDevolucion String
    private  idUsuario String
    private  pais String*/
}
