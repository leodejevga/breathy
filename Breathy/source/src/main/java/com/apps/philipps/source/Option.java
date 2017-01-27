package com.apps.philipps.source;

/**
 * Created by leode on 27.01.2017.
 */

public class Option {

    private final Object parameter;
    private Object value;
    public final long id;

    public Option(Object parameter, Object value, long id){
        this.id = id;
        this.parameter = parameter;
        this.value = value;
    }

    public boolean setValue(Object value) {
        this.value = value;
        return true;
    }
}
