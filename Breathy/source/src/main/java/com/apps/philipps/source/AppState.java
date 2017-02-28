package com.apps.philipps.source;

/**
 * Created by Jevgenij Huebert on 22.02.2017. Project Breathy
 */
public class AppState {
    public static boolean inGame = false;
    public static boolean recordData = false;
    public static boolean btAsked=false;

    public static AppState.BtState btState = BtState.Disabled;

    public enum BtState{
        Disabled,
        Enabled,
        Connected
    }
}
