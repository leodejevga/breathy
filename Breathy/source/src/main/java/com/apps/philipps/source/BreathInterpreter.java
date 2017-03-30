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
    public static int strength=0; // ist jetzt as a Prozent definiert!
    public static Status status = Status.None;
    private static int in = 0;
    private static int out = 10;
    private static int none = 0;
    private static int tolerance = 10; // Toleranz von 690<700<710;

    public static void addObserver(IObserver o) {
        observer.add(o);
    }

    public static boolean removeObserver(IObserver o) {
        return observer.remove(o);
    }

    public static void setStatus() {
        int norm = AppState.breathyNormState;
        Integer[] data = BreathData.get(0,10);
        Integer[] temp= null;
        Integer[] percent = null;
        Integer sumPercent = 0;
        for (int i=0; i<=9; i++)
        {
            temp[i] = data[i] - norm;

            if (temp[i] < -10) {
                out -= 1;
                percent[i] = (Integer) ((Math.abs(temp[i])* 100)/(norm-tolerance));
            }
            else if (temp[i] > 10) {
                in += 1;
                percent[i] = (Integer) ((temp[i]*100)/(323-tolerance));
            }
            else if (temp[i] <= tolerance && temp[i] >= -tolerance) {
                none += 1;
            }
        }

        for (int i=0; i<10; i++) { sumPercent += percent[i]; }
        strength = (Integer)(sumPercent /10);

        if (out == 0){ status = Status.Out;}
        else if (in == 10){ status = Status.In;}
        else if(none == 10){ status = Status.None;}

        for (IObserver o : observer)
            o.call(status, strength);
    }




}
