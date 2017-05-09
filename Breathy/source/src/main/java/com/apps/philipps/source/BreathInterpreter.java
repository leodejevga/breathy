package com.apps.philipps.source;

import android.hardware.fingerprint.FingerprintManager;
//import java.time.*;

import com.apps.philipps.source.interfaces.IObserver;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Jevgenij Huebert on 20.03.2017. Project Breathy
 */

public class BreathInterpreter{

    private static List<IObserver> observer = new ArrayList<>();


    public enum Status {
        In,
        Out,
        None
    }
    public static int strength=0; // ist jetzt as a Prozent definiert!
    public static Status status = Status.None; //TODO: wie oft schnelligkeit
    public static void addObserver(IObserver o) {
        observer.add(o);
    }

    public static boolean removeObserver(IObserver o) {
        return observer.remove(o);
    }

    public static void setStatus() {
        int norm = AppState.breathyNormState;

        int i = 0;
        int previ = 0;

        int none = 0;
        int in = 0;
        int out = 0;
        int m_in = 0;
        int m_out = 0;
        int m_none = 0;

        int mm = 0; // wird die Zeit pro Minute zeigen!

        int temp =0;
        int strength_in=0;
        int strength_out=0;
        int percent_in =0;
        int percent_out =0;

        int tolerance = 10; // was als none bewertet wird!
        int index = 0;  // Anfang Index von Array!
        int range = 10; // jedes mal wird von DataBreath.get gelesen!

        boolean f = true;
        /*LocalDateTime time1;

*/
        Calendar cal ;
        int minute1 = 0;
        int second1 = 0;
        Integer[] data = BreathData.get(index,range);
        while (i < range) {
            if (f == true) {
                cal = Calendar.getInstance();
                minute1 = cal.get(Calendar.MINUTE);
                second1 = cal.get(Calendar.SECOND);
                f = false;
            }
            previ = i;
            // Die none Daten werden hier herausgefunden!
            while (i<data.length && data[i] >= norm - tolerance && data[i] <= norm + tolerance) {
                i++;
            }
            if (i > previ) {
                none++;
                previ = i;
            }
            // Die Daten des Ausatmens wird hier herausgefunden und bewertet!
            while (i<data.length && data[i] >= 1 && data[i] <= norm - tolerance) {
                percent_out += data[i];
                i++;
            }
            temp = 0;
            if (i > previ) {
                out++;
                previ = i;
                percent_out = (int) (percent_out/i);
                strength_out = ((percent_out*100)/(norm-tolerance)) ;
            }

            // Die Daten des Einatmens wird hier herausgefunden und bewertet!
            while (i < data.length && data[i] > norm+tolerance && data[i] <= 1023) {
                percent_in += data[i];
                i++;
            }
            if (i > previ){
                in++;
                percent_in = (int) (percent_in/i);
                strength_in = ((percent_in*100)/(1023-norm-tolerance));
            }

            Calendar cal2 = Calendar.getInstance();
            int minute2 = cal2.get(Calendar.MINUTE);
            int second2 = cal2.get(Calendar.SECOND);
            // Hier wird jedes mal nach einer Minute Zeit auch gespeichert!
            if (minute2 == minute1 + 1) {
                // wenn diese Kondition durchgeführt wird ist mm++ !
                // das heißt wird zeigen, wie viel mal in einer Minute ein bzw. ausgeatmet wird !
                if (second1 == second2) {
                    mm++;
                    m_in = in;
                    m_out = out;
                    m_none = none;
                    BreathInfo t2 = new BreathInfo(in, out, none, m_in, m_out, m_none, mm ,strength_in,strength_out);
                    f = true;
                    m_in = 0;
                    m_out = 0;
                    m_none = 0;
                }
            } else  {BreathInfo t1 = new BreathInfo(in, out, none,mm,strength_in,strength_out);}

        }
    }

}
