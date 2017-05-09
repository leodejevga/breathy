package com.apps.philipps.source;

import android.support.annotation.Nullable;

import com.apps.philipps.source.interfaces.IObserver;

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

    }

    public static PlanManager getInstance(){
        return manager;
    }

    public static void addPlan(Plan plan){
        if(plans == null) {
            plans = new ArrayList<>();
            BreathData.addObserver(manager);
        }
        plans.add(plan);
    }

    public static boolean startPlan(int id){
        if(currentPlan!=-1) {
            currentPlan = id;
            plans.get(id).startPlan();
            return true;
        }
        return false;
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


    public static class Plan{
        private List<Option> options;
        private long currentTime;
        private int currentOption;
        private boolean running;
        private long delta;

        public Plan(){
            options = new ArrayList<>();
        }
        public Plan(float in, float out, float frequency, int duration){
            options = new ArrayList<>();
            options.add(new Option(in, out, frequency, duration*1000));
        }
        public Plan addOption(float in, float out, float frequency, int duration){
            options.add(new Option(in, out, frequency, duration*1000));
            return this;
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
        public String toString() {
            String result = "";
            for (Option o : options) {
                result += o + "\n";
            }
            return result;
        }

        public static class Option{
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
            public String toString() {

                return "in: " + in + ", out: " + out + ", frequency: " + frequency + ", time: " + duration;
            }
        }


    }
}
