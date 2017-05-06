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
        if(currentPlan!=-1){
            Plan p = plans.get(currentPlan);

            return new Plan.Option(p.getStrengthIn(), p.getStrengthOut(), p.getCurrentDuration());
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
        public Plan(int in, int out, int duration){
            options = new ArrayList<>();
            options.add(new Option(in, out, duration*1000));
        }
        public Plan addOption(int in, int out, int duration){
            options.add(new Option(in, out, duration*1000));
            return this;
        }

        public int getStrengthIn(){
            return options.get(currentOption).in;
        }

        public int getStrengthOut(){
            return options.get(currentOption).out;
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
            private int out;
            private int in;
            private long duration;
            public Option(int in, int out, long duration){
                this.in = in;
                this.out = out;
                this.duration = duration;
            }
            public long getDuration(){
                return duration;
            }

            public long getIn(){
                return in;
            }

            public long getOut(){
                return out;
            }

            @Override
            public String toString() {

                return "in: " + in + ", out: " + out + ", time: " + duration;
            }
        }


    }
}
