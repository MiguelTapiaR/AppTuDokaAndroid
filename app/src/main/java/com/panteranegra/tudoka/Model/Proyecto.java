package com.panteranegra.tudoka.Model;

import java.io.Serializable;

public class Proyecto implements Serializable {
    public String getKeyCliente() {
        return keyCliente;
    }

    public void setKeyCliente(String keyCliente) {
        this.keyCliente = keyCliente;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    private String keyCliente, key, nombre, numero, pais;

    public Proyecto(String keyCliente, String key, String nombre, String numero, String pais) {
        this.keyCliente = keyCliente;
        this.key = key;
        this.nombre = nombre;
        this.numero = numero;
        this.pais = pais;
    }
}
