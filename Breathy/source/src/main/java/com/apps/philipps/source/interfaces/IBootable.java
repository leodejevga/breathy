package com.apps.philipps.source.interfaces;

import android.app.Activity;
import android.content.Context;

/**
 * Created by leode on 27.01.2017.
 */

public interface IBootable {
    public boolean start();
    public boolean end();
    public void makeToast(String text);
}
