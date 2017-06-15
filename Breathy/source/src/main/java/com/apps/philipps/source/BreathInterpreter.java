package com.apps.philipps.source;

import android.hardware.fingerprint.FingerprintManager;
//import java.time.*;

import com.apps.philipps.source.interfaces.IObserver;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Jevgenij Huebert on 20.03.2017. Project Breathy
 */

public abstract class BreathInterpreter {

    private static List<IObserver> observer = new ArrayList<>();

    public enum BreathMoment {
        In,
        Out,
        Still,
        None
    }

    public enum BreathError {
        None("", -1),
        VeryGood("Very good", 0),
        Good("Good", 1),
        Ok("Is ok", 2),
        NotOk("Not ok", 3),
        NotGood("Not good", 4),
        Bad("Bad", 5),
        VeryBad("Very bad", 6);

        public final String name;
        public final int value;

        BreathError(String name, int value) {
            this.name = name;
            this.value = value;
        }

        public Comparator<BreathError> getComperator() {
            return new Comparator<BreathError>() {
                @Override
                public int compare(BreathError o1, BreathError o2) {
                    return o1.value == o2.value ? 0 : o1.value < o2.value ? -1 : 1;
                }
            };
        }

        @Override
        public String toString() {
            return name;
        }

        public int compare(BreathError be) {
            return value == be.value ? 0 : value < be.value ? -1 : 1;
        }

        public static BreathError getErrorStatus(double strengthIn, double strengthOut, double frequenzy) {
            PlanManager.Plan.Option option = PlanManager.getCurrentOption();
            if (option == null)
                return BreathError.None;
            double fValue = Math.abs(option.getFrequency()/60 - frequenzy) / option.getFrequency();
            double iValue = Math.abs(option.getIn().value - strengthIn < 0 ? 0 : option.getIn().value - strengthIn);
            double oValue = Math.abs(option.getOut().value - strengthOut < 0 ? 0 : option.getOut().value - strengthOut);
            double min = (iValue + oValue) / 2 + fValue;
            return min < 0.1 ? VeryGood : min < 0.15 ? Good : min < 0.17 ? Ok : min < 0.2 ? NotOk : min < 0.25 ? NotGood : min < 0.3 ? Bad : VeryBad;
        }
    }

    public static void addObserver(IObserver o) {
        observer.add(o);
    }

    public static boolean removeObserver(IObserver o) {
        return observer.remove(o);
    }

    public static BreathStatus getStatus() {
        int norm = AppState.breathyNormState;
        Integer[] data = BreathData.get(0, 50);
        BreathMoment moment = BreathMoment.None;
        float in = 0;
        float out = 0;
        double frequency = 0;
        boolean readyToAdd = false;
        double mean = 0;
        int founds = 0;
        for (int i = 0; i < data.length - 1 && data[i] != null; i++) {
            int d = data[i];
            if (founds < 2) {
                if (d - norm > 0) {
                    if (moment == BreathMoment.None)
                        moment = BreathMoment.In;

                    if (in < d - norm) {
                        in = d - norm;
                    }
                } else if (d - norm < 0) {
                    if (moment == BreathMoment.None)
                        moment = BreathMoment.Out;

                    if (out < norm - d) {
                        out = norm - d;
                    }
                }
            }
            if (data[i + 1] != null && d < data[i + 1])
                readyToAdd = true;
            if (data[i + 1] != null && d > data[i + 1] && readyToAdd) {
                mean = i;
                founds++;
                readyToAdd = false;
            }

        }
        in = in / (AppState.breathyUserMax - norm);
        out = out / (norm - AppState.breathyUserMin);
        if (founds != 0)
            mean /= founds;
        if (mean != 0)
            frequency = 1 / (mean / AppState.breathyDataFrequency);

        return new BreathStatus(moment == BreathMoment.In ? in : out, frequency, moment, BreathError.getErrorStatus(in, out, frequency));
    }

    public static class BreathStatus {
        private float strength; //wie stark in prozent
        private BreathMoment moment = BreathMoment.None;
        private double frequency; //Wie oft pro sekunde
        private BreathError error = BreathError.None;

        public BreathStatus(float strength, double frequency, BreathMoment moment, BreathError error) {
            this.strength = strength;
            this.frequency = frequency;
            this.moment = moment;
            this.error = error;
        }


        public float getStrength() {
            return strength;
        }

        public BreathMoment getMoment() {
            return moment;
        }

        public double getFrequency() {
            return frequency;
        }

        public BreathError getError() {
            return error;
        }

        @Override
        public String toString() {
            return "status: " + moment + ", strength: " + (int) (strength * 100) + "%, frequency: " + (int) (frequency * 60) + " per minute, how good: " + error;
        }
    }


}