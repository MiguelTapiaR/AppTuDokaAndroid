package com.panteranegra.tudoka.Model;

import java.io.Serializable;
import java.util.ArrayList;

public class ReporteSeguimiento implements Serializable {
    private Cliente cliente;
    private Proyecto proyecto;
    private String urlFirma;

    private ArrayList<Actividad> alActividad;

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

    public ArrayList<Actividad> getAlActividad() {
        return alActividad;
    }

    public void setAlActividad(ArrayList<Actividad> alActividad) {
        this.alActividad = alActividad;
    }

    public String getUrlFirma() {
        return urlFirma;
    }

    public void setUrlFirma(String urlFirma) {
        this.urlFirma = urlFirma;
    }
}
