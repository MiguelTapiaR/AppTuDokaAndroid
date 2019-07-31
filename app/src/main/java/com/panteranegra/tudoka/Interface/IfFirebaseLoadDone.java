package com.panteranegra.tudoka.Interface;

import com.panteranegra.tudoka.Model.Cliente;

import java.util.List;

public interface IfFirebaseLoadDone {
    void onFirebaseLoadSuccess(List<Cliente> clienteList);
    void onFirebaseLoadFailed(String message);
}
