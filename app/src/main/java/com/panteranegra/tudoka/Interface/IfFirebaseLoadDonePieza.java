package com.panteranegra.tudoka.Interface;

import com.panteranegra.tudoka.Model.Pieza;

import java.util.List;

public interface IfFirebaseLoadDonePieza {
    void onFirebaseLoadSuccess(List<Pieza> piezaList);

    void onFirebaseLoadFailed(String message);

    void onFirebaseLoadSucces(List<Pieza> piezaList);
}
