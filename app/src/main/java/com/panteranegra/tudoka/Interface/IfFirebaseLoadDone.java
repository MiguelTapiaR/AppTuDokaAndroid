package com.panteranegra.tudoka.Interface;

import com.panteranegra.tudoka.Model.Cliente;
import com.panteranegra.tudoka.Model.Proyecto;

import java.util.List;

public interface IfFirebaseLoadDone {
    void onFirebaseLoadSuccess(List<Cliente> clienteList);
    void onFirebaseLoadSuccessProyecto(List<Proyecto> proyectoList);
    void onFirebaseLoadFailed(String message);
}
