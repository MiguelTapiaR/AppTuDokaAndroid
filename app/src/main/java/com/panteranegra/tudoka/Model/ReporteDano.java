package com.panteranegra.tudoka.Model;

import java.io.Serializable;
import java.util.ArrayList;

public class ReporteDano implements Serializable {
    private Cliente cliente;
    private Proyecto proyecto;

    private ArrayList<Pieza> alPiezas;

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

    public ArrayList<Pieza> getAlPiezas() {
        return alPiezas;
    }

    public void setAlPiezas(ArrayList<Pieza> alPiezas) {
        this.alPiezas = alPiezas;
    }
}

