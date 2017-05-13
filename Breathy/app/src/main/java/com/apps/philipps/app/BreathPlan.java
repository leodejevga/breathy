package com.apps.philipps.app;

/**
 * Created by Var on 13.05.2017.
 */

public class BreathPlan {
    public static final int INTENSITY_VERY_HIGH = 100;
    public static final int INTENSITY_HIGH = 80;
    public static final int INTENSITY_MEDIUM = 60;
    public static final int INTENSITY_LOW = 40;
    public static final int INTENSITY_VERY_LOW = 20;


    private int planId;
    private String name;
    private boolean breatheIn;
    private int intensity;
    private int breathsPerMinute;
    private int minutesPerExercise;
    private boolean activated;

    public BreathPlan(int planId, String name, boolean inhalate, int intensity, int breathsPerMinute, int minutesPerExercise, boolean activated){
        this.planId = planId;
        this.name = name;
        this.breatheIn = inhalate;
        this.intensity = intensity;
        this.breathsPerMinute = breathsPerMinute;
        this.minutesPerExercise = minutesPerExercise;
        this.activated = activated;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isBreatheIn() {
        return breatheIn;
    }

    public void setBreatheIn(boolean breatheIn) {
        this.breatheIn = breatheIn;
    }

    public int getIntensity() {
        return intensity;
    }

    public void setIntensity(int intensity) {
        this.intensity = intensity;
    }

    public int getBreathsPerMinute() {
        return breathsPerMinute;
    }

    public void setBreathsPerMinute(int breathsPerMinute) {
        this.breathsPerMinute = breathsPerMinute;
    }

    public int getMinutesPerExercise() {
        return minutesPerExercise;
    }

    public void setMinutesPerExercise(int minutesPerExercise) {
        this.minutesPerExercise = minutesPerExercise;
    }

    public int getPlanId() {
        return planId;
    }

    public void setPlanId(int planId) {
        this.planId = planId;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }
}
