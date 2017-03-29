package com.apps.philipps.source;

import com.apps.philipps.source.interfaces.IObserver;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jevgenij Huebert on 20.03.2017. Project Breathy
 */

public class BreathInterpreter {

    private static List<IObserver> observer = new ArrayList<>();

    public enum Status {
        In,
        Out,
        None
    }

    public static int strength = 0;
    public static Status status = Status.None;

    public static void addObserver(IObserver o) {
        observer.add(o);
    }

    public static boolean removeObserver(IObserver o) {
        return observer.remove(o);
    }

    public static void setStatus() {
        int norm = AppState.breathyNormState; //TODO: Der breathyNormState zeigt den Druck an, ohne, dass jemand reinpustet. Dieser soll benutzt werden um strength richtig zu identifizieren
        Integer[] data = BreathData.get(0, 10); //TODO: Diese Daten sollen analysiert werden und der status und strength entsprechend gesetzt werden.

        for (IObserver o : observer)
            o.call(status, strength);
    }

}
