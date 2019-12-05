package com.panteranegra.tudoka.Model;

import java.io.Serializable;
import java.util.ArrayList;

public class ReporteCapacitacion implements Serializable {
    private Cliente cliente;
    private Proyecto proyecto;
    private String nombreCurso;

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

    public String getNombreCurso() {
        return nombreCurso;
    }

    public void setNombreCurso(String nombreCurso) {
        this.nombreCurso = nombreCurso;
    }
}
