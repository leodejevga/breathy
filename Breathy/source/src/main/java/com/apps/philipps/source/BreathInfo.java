package com.apps.philipps.source;

/**
 * Created by Mohsen on 09.05.2017.
 */

public class BreathInfo {
    public int in;
    public int out;
    public int none;
    public int minute;
    public int m_in;
    public int m_out;
    public int m_none;
    public int strength_in =0;
    public int strength_out=0;

    public BreathInfo(int in,int out,int none, int minute, int strength_in, int strength_out)
    {
        this.in=in;
        this.out=out;
        this.none=none;
        this.minute=minute;
        this.strength_in=strength_in;
        this.strength_out= strength_out;

    }
    public BreathInfo(int in, int out, int none, int m_in, int m_out, int m_none , int minute, int strength_in, int strength_out)
    {
        this.in=in;
        this.out=out;
        this.none=none;
        this.m_in=m_in;
        this.m_out=m_out;
        this.m_none=m_none;
        this.minute=minute;
        this.strength_in=strength_in;
        this.strength_out= strength_out;
    }

}
