package com.apps.philipps.source;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.apps.philipps.source.interfaces.IObserver;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jevgenij Huebert on 06.05.2017. Project Breathy
 */

public class PlanManager implements IObserver {

    private static List<Plan> plans;
    private static PlanManager manager = new PlanManager();
    private static int currentPlan;

    private PlanManager(){
        currentPlan = -1;
        plans = new ArrayList<>();
        BreathData.addObserver(manager);
    }

    public static PlanManager getInstance(){
        return manager;
    }

    public static void addPlan(Plan plan){
        plans.add(plan);
    }
    public static void removePlan(Plan plan){
        plans.remove(plan);
    }

    public static List<Plan> getPlans(){
        List<Plan> result = new ArrayList<>();
        for (Plan p :
                plans) {
            try{
                result.add((Plan)p.clone());
            }
            catch (CloneNotSupportedException e){
                return null;
            }
        }
        return result;
    }

    public static boolean setActive(int id){
        if(id>=0 && id<plans.size())
            currentPlan = id;
        else return false;
        return true;
    }
    public static boolean setActive(Plan plan){
        int id = plans.indexOf(plan);
        if(id!=-1)
            currentPlan = id;
        else return false;
        return true;
    }

    public static boolean startPlan(){
        if(currentPlan!=-1) {
            plans.get(currentPlan).startPlan();
            return true;
        }
        return false;
    }

    public static Plan getPlan(int id){
        return plans.get(id);
    }

    public static Plan deletePlan(int id){
        return plans.remove(id);
    }

    public static Plan newPlan(float in, float out, float freq, int duration){
        return new Plan(in, out, freq, duration);
    }

    public static boolean isActive(Plan plan){
        return currentPlan == plans.indexOf(plan);
    }

    @Nullable
    public static Plan.Option getStatus(){
        if(currentPlan!=-1 && plans.get(currentPlan).getCurrentDuration()!=0){
            Plan p = plans.get(currentPlan);
            return new Plan.Option(p.getStrengthIn(), p.getStrengthOut(), p.getFrequency(), p.getCurrentDuration());
        }
        return null;
    }

    @Override
    public void call(Object... messages) {
        for (Plan plan : plans) {
            plan.update();
        }
    }


    public static class Plan implements Cloneable, Serializable {
        private List<Option> options;
        private long currentTime;
        private int currentOption;
        private boolean running;
        private long delta;
        private String name;

        public Plan(){
            options = new ArrayList<>();
        }
        public Plan(float in, float out, float frequency, int duration){
            this();
            options.add(new Option(in, out, frequency, duration*1000));
        }
        public Plan(String name, float in, float out, float frequency, int duration){
            this(in, out, frequency, duration);
            this.name = name;
        }

        public Plan addOption(float in, float out, float frequency, int duration){
            options.add(new Option(in, out, frequency, duration*1000));
            return this;
        }

        public boolean isActivated(){
            return PlanManager.isActive(this);
        }

        public String getName(){
            return name;
        }

        public float getStrengthIn(){
            return options.get(currentOption).in;
        }

        public float getStrengthOut(){
            return options.get(currentOption).out;
        }

        public float getFrequency(){
            return options.get(currentOption).frequency;
        }

        public int getCurrentDuration(){
            return (int)(currentTime/1000);
        }

        public int getId(){
            return plans.indexOf(this);
        }

        private boolean startPlan() {
            if(running)
                return false;
            else {
                running = !running;
                currentTime = options.get(0).duration;
                delta = System.currentTimeMillis();
            }
            return running;
        }

        private boolean update(){
            delta = System.currentTimeMillis() - delta;
            if(running){
                if(currentTime-delta<=0){
                    currentOption++;
                    if(currentOption == options.size()){
                        running = false;
                        currentOption = 0;
                        currentTime = 0;
                        return false;
                    }
                    else{
                        currentTime = options.get(currentOption).getDuration();
                    }
                }
                else
                    currentTime -= delta;
            }
            delta = System.currentTimeMillis();
            return true;
        }

        @Override
        protected Object clone() throws CloneNotSupportedException {
            Plan result = new Plan();
            result.currentOption = currentOption;
            result.currentTime = currentTime;
            result.delta = delta;
            result.running = running;
            result.options = new ArrayList<>();
            for (Option o :
                    options) {
                options.add((Option) o.clone());
            }
            return result;
        }

        @Override
        public String toString() {
            String result = "";
            for (Option o : options) {
                result += o + "\n";
            }
            return result;
        }

        @Override
        public int hashCode() {
            return options.size() * name.hashCode();
        }

        public static class Option implements Cloneable, Serializable {
            private float out;
            private float in;
            private float frequency;
            private long duration;
            public Option(float in, float out, float frequency, long duration){
                float max = Math.max(in, out);
                this.in = in;
                this.out = out;
                if(max>1){
                    this.in /= max;
                    this.out /= max;
                }
                this.frequency = frequency;
                this.duration = duration;
            }
            public long getDuration(){
                return duration;
            }

            public float getIn(){
                return in;
            }

            public float getOut(){
                return out;
            }

            public float getFrequency(){
                return frequency;
            }

            @Override
            protected Object clone() throws CloneNotSupportedException {
                return new Option(in, out, frequency, duration);
            }

            @Override
            public String toString() {

                return "in: " + (int)(in*100) + "%, out: " + (int)(out*100) + "%, frequency: " + (int)(frequency*60) + " per minute, time: " + duration;
            }
        }


    }
}
