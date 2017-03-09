package com.apps.philipps.source;

/**
 * Created by Jevgenij Huebert on 22.02.2017. Project Breathy
 */
public class AppState {
    public static boolean inGame = false;
    public static boolean recordData = false;
    public static boolean btAsked=false;

    public static AppState.BtState btState = BtState.Disabled;
    public static Framelimit framelimit = Framelimit.Unlimited;

    public enum BtState{
        Disabled,
        Enabled,
        Connected
    }

    public enum Framelimit{
        Movie(24),
        Thirty(30),
        Sixty(60),
        HundredTwenty(120),
        Unlimited(1000);

        int frameLimit;
        Framelimit(int frames){
            frameLimit = frames;
        }
        public int getLimit(){
            return frameLimit;
        }
    }
}
