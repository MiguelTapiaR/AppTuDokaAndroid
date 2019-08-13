package com.panteranegra.tudoka.Model;



import java.io.Serializable;
import java.util.ArrayList;

public class ReporteDevolucion implements Serializable {
    private Cliente cliente;
    private Proyecto proyecto;
    private String fotoLicencia;
    private String fotoPlacaDelantera;
    private String fotoPlacaTrasera;
    private String fotoTractoLateral1;
    private String fotoTractoLateral2;
    private String fotoTractoParteTrasera;
    private String fotoDocumentoCliente;
    private  ArrayList<Pieza> alPiezaDevolucion;



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

    public String getFotoLicencia() {
        return fotoLicencia;
    }

    public void setFotoLicencia(String fotoLicencia) {
        this.fotoLicencia = fotoLicencia;
    }


    public void setFotoPlacaDelantera(String fotoPlacaDelantera) {
        this.fotoPlacaDelantera = fotoPlacaDelantera; }


    public String getFotoPlacaDelantera() {
        return fotoPlacaDelantera;
    }

    public String getFotoPlacaTrasera() {
        return fotoPlacaTrasera;
    }

    public void setFotoPlacaTrasera(String fotoPlacaTrasera) {
        this.fotoPlacaTrasera = fotoPlacaTrasera;
    }

    public String getFotoTractoLateral1() {
        return fotoTractoLateral1;
    }

    public void setFotoTractoLateral1(String fotoTractoLateral1) {
        this.fotoTractoLateral1 = fotoTractoLateral1;
    }

    public String getFotoTractoLateral2() {
        return fotoTractoLateral2;
    }

    public void setFotoTractoLateral2(String fotoTractoLateral2) {
        this.fotoTractoLateral2 = fotoTractoLateral2;
    }

    public String getFotoTractoParteTrasera() {
        return fotoTractoParteTrasera;
    }

    public void setFotoTractoParteTrasera(String fotoTractoParteTrasera) {
        this.fotoTractoParteTrasera = fotoTractoParteTrasera;
    }

    public String getFotoDocumentoCliente() {
        return fotoDocumentoCliente;
    }

    public void setFotoDocumentoCliente(String fotoDocumentoCliente) {
        this.fotoDocumentoCliente = fotoDocumentoCliente;
    }
}
