package com.apps.philipps.source.abstracts;

import android.app.Activity;
import android.content.Context;

import com.apps.philipps.source.interfaces.IOptions;
import com.apps.philipps.source.Option;

import java.util.List;

/**
 * Created by leode on 27.01.2017.
 */

public abstract class AbstractOptions extends GameObject implements IOptions {
    protected List<Option> options;

    public List<Option> getOptions() {
        return options;
    }

    public boolean setOption(int id, Object value) {
        for (Option o : options) {
            if(o.id == id)
                return o.setValue(value);
        }
        return false;
    }

    @Override
    public boolean end() {
        if(activity!=null)
            activity.finish();
        else return false;
        return true;
    }

}
