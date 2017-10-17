package com.breathy.racing.stats;

import com.breathy.racing.GameUtil;

/**
 * Created by Jürgen on 10.10.2017.
 */

public class RaceDifficult {

    private static final double INCREASECONSTANT = 10;
    private double deltaSpeed;

    private double minInterval;
    private double meanInterval;
    private double maxInterval;

    public RaceDifficult( double deltaSpeed, double minInterval, double meanInterval, double maxInterval ) {
        this.deltaSpeed = deltaSpeed;
        this.minInterval = minInterval;
        this.meanInterval = meanInterval;
        this.maxInterval = maxInterval;
    }

    public double getDeltaSpeed() {
        return deltaSpeed;
    }

    public void setDeltaSpeed( double deltaSpeed ) {
        this.deltaSpeed = deltaSpeed;
    }

    public double getMinInterval() {
        return minInterval;
    }

    public void setMinInterval( double minInterval ) {
        this.minInterval = minInterval;
    }

    public double getMeanInterval() {
        return meanInterval;
    }

    public void setMeanInterval( double meanInterval ) {
        this.meanInterval = meanInterval;
    }

    public double getMaxInterval() {
        return maxInterval;
    }

    public void setMaxInterval( double maxInterval ) {
        this.maxInterval = maxInterval;
    }

    public void increaseSpeed(){
        deltaSpeed += INCREASECONSTANT;
    }

    public long getIntervalTime(){
        return System.currentTimeMillis() + (long) GameUtil.triangularDistribution( minInterval, maxInterval, meanInterval );
    }
}
