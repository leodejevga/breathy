package com.apps.philipps.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Jevgenij Huebert on 06.05.2017. Project Breathy
 */

public abstract class PlanManager implements Serializable {

    public static final String TAG = "Plan Manager";
    private static List<Plan> plans = new ArrayList<>();
    private static int currentPlan = -1;
    private static boolean initialized = false;

    private PlanManager() {
    }

    public static void init() throws PlanManagerAlreadyInitialized {

        if (!initialized) {
            initialized = true;
            Thread update = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        update();
                        try {
                            Thread.sleep(50);

                        } catch (InterruptedException e) {
                            Log.e(TAG, "Update Plan has been interrupter", e);
                        }
                    }
                }
            });
            update.start();
        } else
            throw new PlanManagerAlreadyInitialized();

    }
    private static boolean checkCurrentPlan(){
        return plans.size()>currentPlan && currentPlan>=0;
    }

    public static void init(PlanManagerInstance instance) throws PlanManagerAlreadyInitialized {
        if (!initialized) {
            plans = instance.plans;
            currentPlan = instance.currentPlan;
            init();
        } else
            throw new PlanManagerAlreadyInitialized();
    }

    public static void addPlan(Plan plan) {
        plans.add(plan);
    }

    public static Plan deletePlan(int id) {
        if (id >= 0 && id < plans.size()) {
            Plan p = plans.remove(id);
            return p;
        }
        return null;
    }

    public static Plan getPlan(int id) {
        if (id >= 0 && id < plans.size())
            return plans.get(id);
        return null;
    }

    public static List<Part> getParts() {
        ArrayList<Part> result = new ArrayList<>();
        for (Plan plan : plans)
            result.add(plan);
        return result;
    }

    public static boolean setActive(int id) {
        if (id >= 0 && id < plans.size()) {
            currentPlan = id;
        } else return false;
        return true;
    }

    public static boolean start() {
        if (checkCurrentPlan()) {
            plans.get(currentPlan).startPlan();
            return true;
        }
        return false;
    }


    public static void resume() {
        if (checkCurrentPlan())
            plans.get(currentPlan).resume();
    }

    public static void stop() {
        if (checkCurrentPlan())
            plans.get(currentPlan).stop();
    }

    public static void pause() {
        if (checkCurrentPlan())
            plans.get(currentPlan).pause();
    }

    public static boolean isActive() {
        if (checkCurrentPlan())
            return plans.get(currentPlan).running;
        return false;
    }

    public static boolean isActive(Plan plan) {
        return currentPlan == plans.indexOf(plan);
    }

    public static boolean isActive(int id) {
        return currentPlan == id;
    }

    @Nullable
    public static Plan getCurrentPlan() {
        if (checkCurrentPlan())
            return plans.get(currentPlan);
        return null;
    }

    public static long getDuration() {
        if (checkCurrentPlan() && plans.get(currentPlan).running) {
            return plans.get(currentPlan).getCurrentDuration();
        }
        return 0;
    }


    @Nullable
    public static String getStatus() {
        if (checkCurrentPlan() && plans.get(currentPlan).running)
            return getCurrentOption() + "\nrest time: " + getCurrentPlan().getCurrentDuration();
        return null;
    }

    private static void update() {
        if (checkCurrentPlan())
            plans.get(currentPlan).update();
    }

    public static Plan.Option getCurrentOption() {
        if (checkCurrentPlan() && plans.get(currentPlan).running)
            return plans.get(currentPlan).getOption(plans.get(currentPlan).currentOption);
        return null;
    }

    //----------------------------------------------------------------------------------------------

    public static Plan.Option getOption(int planId, int optionId) {
        if (planId >= 0 && planId < plans.size() && optionId >= 0 && optionId < plans.get(planId).options.size())
            return plans.get(planId).options.get(optionId);
        return null;
    }

    public static Plan.Option getOption(int optionId) {
        if (checkCurrentPlan() && optionId >= 0 && optionId < plans.get(currentPlan).options.size())
            return plans.get(currentPlan).options.get(optionId);
        return null;
    }

    public static void addOption(int planId, Plan.Option option) {
        if (planId >= 0 && planId < plans.size())
            plans.get(planId).addOption(option);
    }

    public static void addOption(Plan.Option option) {
        if (checkCurrentPlan())
            plans.get(currentPlan).addOption(option);
    }

    public static List<Plan.Option> getOptions(int planId) {
        if (planId >= 0 && planId < plans.size())
            return plans.get(planId).options;
        return null;
    }

    public static List<Plan> getPlans() {
        return plans;
    }


    public static class Plan implements Cloneable, Serializable, Part, Iterable<Plan.Option>, Comparable<Plan> {
        private List<Option> options;
        private long currentTime;
        private int currentOption;
        private boolean running;
        private long delta;
        private String name;
        private boolean paused = false;

        public Plan() {
            options = new ArrayList<>();

        }

        public Plan(BreathIntensity in, BreathIntensity out, int frequency, int duration) {
            this();
            Option add = new Option(in, out, frequency, duration * 1000);
            add.parent = this;
            options.add(add);
        }

        public Plan(String name, BreathIntensity in, BreathIntensity out, int frequency, int duration) {
            this(in, out, frequency, duration);
            this.name = name;
        }

        public Plan addOption(BreathIntensity in, BreathIntensity out, int frequency, int seconds) {
            return addOption("", in, out, frequency, seconds);
        }

        private Plan addOption(Option option) {
            option.parent = this;
            options.add(option);
            return this;
        }

        public Plan addOption(String name, BreathIntensity in, BreathIntensity out, int frequency, int seconds) {
            Option o = new Option(in, out, frequency, seconds * 1000);
            o.parent = this;
            o.name = name;
            options.add(o);
            return this;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Plan removeOption(int id) {
            options.remove(id);
            return this;
        }

        public Option getOption(int id) {
            if (id >= 0 && id < options.size())
                return options.get(id);
            return null;
        }

        public List<Part> getParts() {
            List<Part> result = new ArrayList<>();
            for (Option option : options)
                result.add(option);
            return result;
        }

        public boolean isActivated() {
            return PlanManager.isActive(this);
        }

        public BreathIntensity getStrengthIn() {
            return options.get(currentOption).in;
        }

        public BreathIntensity getStrengthOut() {
            return options.get(currentOption).out;
        }

        /**
         * Breaths per second
         *
         * @return breaths per second
         */
        public int getFrequency() {
            if (options.size() > currentOption && currentOption >= 0)
                return options.get(currentOption).frequency;
            return 0;
        }

        public long getCurrentDuration() {
            return currentTime;
        }

        public long getDuration() {
            long result = 0;
            for (Option o : options)
                result += o.getDuration();
            return result;
        }


        private boolean startPlan() {
            if (running)
                return false;
            else {
                paused = false;
                running = true;
                currentOption = 0;
                currentTime = options.get(currentOption).duration;
                delta = System.currentTimeMillis();
            }
            return true;
        }

        private boolean update() {
            delta = System.currentTimeMillis() - delta;
            if (running && !paused) {
                if (currentTime - delta <= 0) {
                    currentOption++;
                    if (currentOption == options.size())
                        stop();
                    else
                        currentTime = options.get(currentOption).getDuration();
                } else
                    currentTime -= delta;
            }
            delta = System.currentTimeMillis();
            return true;
        }

        private void stop() {
            running = false;
            paused = false;
            currentOption = 0;
            currentTime = 0;
        }


        private void pause() {
            paused = true;
        }

        private void resume() {
            paused = false;
        }

        public String getDescription() {
            String result = "";
            for (Option o : options) {
                result += o + "\n";
            }
            return result;
        }

        @Override
        public int getId() {
            int result = plans.indexOf(this);
            return result;
        }

        @Override
        public String getName() {
            return name;
        }


        @Override
        protected Plan clone() {
            Plan result = new Plan();
            result.currentOption = currentOption;
            result.currentTime = currentTime;
            result.delta = delta;
            result.running = running;
            result.options = new ArrayList<>();
            for (Option o :
                    options) {
                options.add(o.clone());
            }
            return result;
        }

        @Override
        public String toString() {
            return getId() + 1 + ") " + name + "\n" + getDescription();
        }

        @Override
        public Iterator<Option> iterator() {
            return options.iterator();
        }

        @Override
        public int compareTo(@NonNull Plan p) {
            if (p.currentOption == this.currentOption
                    && p.currentTime == this.currentTime
                    && p.delta == this.delta
                    && p.running == this.running) {
                for (int i = 0; i < this.options.size(); i++) {
                    int result;
                    if ((result = p.getOption(i).compareTo(this.getOption(i))) != 0)
                        return result;
                }
            }
            return 0;
        }

        public enum BreathIntensity {
            VeryHigh(1, 5, "Very high"),
            High(0.8, 4, "High"),
            Medium(0.5, 3, "Medium"),
            Low(0.4, 2, "Low"),
            VeryLow(0.2, 1, "Very low"),
            None(0, 0, "");
            public final double value;
            public final String name;
            public final int id;

            BreathIntensity(double value, int id, String name) {
                this.name = name;
                this.value = value;
                this.id = id;
            }

            public static BreathIntensity get(int id) {
                for (BreathIntensity i : values())
                    if (i.id == id)
                        return i;
                return null;
            }

            public static BreathIntensity get(double value) {
                BreathIntensity prev = None;
                for (BreathIntensity i : values())
                    if (i.value <= value && i.value > prev.value)
                        return i;
                    else
                        prev = i;
                return null;
            }

            @Override
            public String toString() {
                return id + ", " + value + ", " + name;
            }
        }

        @Override
        public int hashCode() {
            return options.size() * name.hashCode();
        }


        public static class Option implements Cloneable, Serializable, Part, Comparable<Option> {
            private BreathIntensity out;
            private BreathIntensity in;
            private int frequency;
            private long duration;
            private Plan parent;
            private String name;

            public Option(BreathIntensity in, BreathIntensity out, int frequency, long duration) {
                this.in = in;
                this.out = out;
                this.frequency = frequency;
                this.duration = duration;
            }

            public Option(String name, BreathIntensity in, BreathIntensity out, int frequency, long duration) {
                this(in, out, frequency, duration);
                this.name = name;
            }

            public long getDuration() {
                return duration;
            }

            public BreathIntensity getIn() {
                return in;
            }

            public BreathIntensity getOut() {
                return out;
            }

            public int getFrequency() {
                return frequency;
            }

            public void setDuration(int duration) {
                this.duration = duration < 60 * 60 * 1000 ? duration : this.duration;
            }

            public void setIn(BreathIntensity in) {
                this.in = in;
            }

            public void setOut(BreathIntensity out) {
                this.out = out;
            }

            public void setFrequency(int frequency) {
                this.frequency = frequency;
            }

            @Override
            public int getId() {
                return parent.options.indexOf(this);
            }

            @Override
            public String getName() {
                return name;
            }

            @Override
            protected Option clone() {
                return new Option(in, out, frequency, duration);
            }

            @Override
            public String toString() {

                return getId() + 1 + ") " + getName() + ";\nin: " + in.name + " " + in.value * 100 + "%,\nout: " + out.name + " " + out.value * 100 + "%,\nfrequency: " + frequency + " per minute,\ntime: " + duration / 1000 + " seconds";
            }

            public void setName(String name) {
                this.name = name;
            }

            @Override
            public int compareTo(@NonNull Option op) {
                if (duration > op.duration)
                    return 1;
                else if (duration < op.duration) return -1;

                if (frequency > op.frequency)
                    return 1;
                else if (frequency < op.frequency) return -1;

                if (parent == op.parent && name.equals(op.name) && in == op.in && out == op.out)
                    return 0;
                return 1;
            }
        }


    }

    public interface Part {
        int getId();

        String getName();
    }

    public static class PlanManagerInstance implements Serializable, Comparable {
        private List<Plan> plans;
        private int currentPlan = -1;

        public PlanManagerInstance() {
            plans = PlanManager.plans;
            currentPlan = PlanManager.currentPlan;
        }

        @Override
        public int compareTo(@NonNull Object o) {
            if (o instanceof PlanManagerInstance) {
                if (currentPlan != ((PlanManagerInstance) o).currentPlan)
                    return 1;
                List<Plan> plans = ((PlanManagerInstance) o).plans;
                if (this.plans.size() != plans.size()) {
                    return 2;
                } else {
                    for (int i = 0; i < plans.size(); i++) {
                        if (this.plans.get(i).compareTo(plans.get(i)) != 0)
                            return 3;
                    }
                }
                return 0;
            } else
                return -1;
        }
    }

    public static class PlanManagerAlreadyInitialized extends Exception {
        public PlanManagerAlreadyInitialized() {
            super("Plan is already initialized and cant be initialized or reinitialized");
        }
    }
}
