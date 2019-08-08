package com.panteranegra.tudoka.Model;

import android.net.Uri;

import java.io.Serializable;

public class ReporteDevolucion implements Serializable {
    private Cliente cliente;
    private Proyecto proyecto;
    private Uri fotoLicencia;
    private Uri fotoPlacaDelantera;
    private Uri fotoPlacaTrasera;
    private Uri fotoTractoLateral1;
    private Uri fotoTractoLateral2;
    private Uri fotoTractoParteTrasera;
    private Uri fotoDocumentoCliente;
    private Uri fotoItem;

    public Uri getFotoItem() {
        return fotoItem;
    }

    public void setFotoItem(Uri fotoItem) {
        this.fotoItem = fotoItem;
    }

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

    public Uri getFotoLicencia() {
        return fotoLicencia;
    }

    public void setFotoLicencia(Uri fotoLicencia) {
        this.fotoLicencia = fotoLicencia;
    }


    public void setFotoPlacaDelantera(Uri fotoPlacaDelantera) {
        this.fotoPlacaDelantera = fotoPlacaDelantera; }


    public Uri getFotoPlacaDelantera() {
        return fotoPlacaDelantera;
    }

    public Uri getFotoPlacaTrasera() {
        return fotoPlacaTrasera;
    }

    public void setFotoPlacaTrasera(Uri fotoPlacaTrasera) {
        this.fotoPlacaTrasera = fotoPlacaTrasera;
    }

    public Uri getFotoTractoLateral1() {
        return fotoTractoLateral1;
    }

    public void setFotoTractoLateral1(Uri fotoTractoLateral1) {
        this.fotoTractoLateral1 = fotoTractoLateral1;
    }

    public Uri getFotoTractoLateral2() {
        return fotoTractoLateral2;
    }

    public void setFotoTractoLateral2(Uri fotoTractoLateral2) {
        this.fotoTractoLateral2 = fotoTractoLateral2;
    }

    public Uri getFotoTractoParteTrasera() {
        return fotoTractoParteTrasera;
    }

    public void setFotoTractoParteTrasera(Uri fotoTractoParteTrasera) {
        this.fotoTractoParteTrasera = fotoTractoParteTrasera;
    }

    public Uri getFotoDocumentoCliente() {
        return fotoDocumentoCliente;
    }

    public void setFotoDocumentoCliente(Uri fotoDocumentoCliente) {
        this.fotoDocumentoCliente = fotoDocumentoCliente;
    }
}
