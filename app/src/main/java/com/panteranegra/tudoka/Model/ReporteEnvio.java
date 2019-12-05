package com.panteranegra.tudoka.Model;


import java.io.Serializable;
import java.util.ArrayList;

public class ReporteEnvio implements Serializable {
    private Cliente cliente;
    private Proyecto proyecto;
    private String fotoLicencia;
    private String fotoPlacaDelantera;
    private String fotoPlacaTrasera;
    private String fotoTractoLateral1;
    private String fotoTractoLateral2;
    private String fotoTractoParteTrasera;
    private String fotoDocumentoCliente;
    private String urlFotoLicencia;
    private String urlFotoPlacaDelantera;
    private String urlFotoPlacaTrasera;
    private String urlFotoTractoLateral1;
    private String urlFotoTractoLateral2;
    private String urlFotoTractoParteTrasera;
    private String urlFotoDocumentoCliente;
    private ArrayList<Pieza> alPiezas;
    private ArrayList<String> alListasCarga;
    private ArrayList<String> alURLListasCarga;
    private ArrayList<String> alNumerosRemision;

    public ReporteEnvio() {
    }


    public ArrayList<Pieza> getAlPiezas() {
        return alPiezas;
    }

    public void setAlPiezas(ArrayList<Pieza> alPiezas) {
        this.alPiezas = alPiezas;
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

    public ArrayList<String> getAlListasCarga() {
        return alListasCarga;
    }

    public void setAlListasCarga(ArrayList<String> alListasCarga) {
        this.alListasCarga = alListasCarga;
    }

    public ArrayList<String> getAlNumerosRemision() {
        return alNumerosRemision;
    }

    public void setAlNumerosRemision(ArrayList<String> alNumerosRemision) {
        this.alNumerosRemision = alNumerosRemision;
    }

    public String getUrlFotoLicencia() {
        return urlFotoLicencia;
    }

    public void setUrlFotoLicencia(String urlFotoLicencia) {
        this.urlFotoLicencia = urlFotoLicencia;
    }

    public String getUrlFotoPlacaDelantera() {
        return urlFotoPlacaDelantera;
    }

    public void setUrlFotoPlacaDelantera(String urlFotoPlacaDelantera) {
        this.urlFotoPlacaDelantera = urlFotoPlacaDelantera;
    }

    public String getUrlFotoPlacaTrasera() {
        return urlFotoPlacaTrasera;
    }

    public void setUrlFotoPlacaTrasera(String urlFotoPlacaTrasera) {
        this.urlFotoPlacaTrasera = urlFotoPlacaTrasera;
    }

    public String getUrlFotoTractoLateral1() {
        return urlFotoTractoLateral1;
    }

    public void setUrlFotoTractoLateral1(String urlFotoTractoLateral1) {
        this.urlFotoTractoLateral1 = urlFotoTractoLateral1;
    }

    public String getUrlFotoTractoLateral2() {
        return urlFotoTractoLateral2;
    }

    public void setUrlFotoTractoLateral2(String urlFotoTractoLateral2) {
        this.urlFotoTractoLateral2 = urlFotoTractoLateral2;
    }

    public String getUrlFotoTractoParteTrasera() {
        return urlFotoTractoParteTrasera;
    }

    public void setUrlFotoTractoParteTrasera(String urlFotoTractoParteTrasera) {
        this.urlFotoTractoParteTrasera = urlFotoTractoParteTrasera;
    }

    public String getUrlFotoDocumentoCliente() {
        return urlFotoDocumentoCliente;
    }

    public void setUrlFotoDocumentoCliente(String urlFotoDocumentoCliente) {
        this.urlFotoDocumentoCliente = urlFotoDocumentoCliente;
    }

    public ArrayList<String> getAlURLListasCarga() {
        return alURLListasCarga;
    }

    public void setAlURLListasCarga(ArrayList<String> alURLListasCarga) {
        this.alURLListasCarga = alURLListasCarga;
    }

}
