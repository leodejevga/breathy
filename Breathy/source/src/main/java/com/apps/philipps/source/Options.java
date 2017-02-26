package com.apps.philipps.source;

import android.support.v4.util.Pair;
import android.support.v4.util.TimeUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Jevgenij Huebert on 28.01.2017. Project Breathy
 */

/**
 * Options provide a List of Pairs.
 *
 * @param <P> the type of the Option
 * @param <V> the type of the Value of the Option
 */
public class Options<P,V> {
    private ArrayList<Pair<P, V>> options;

    /**
     * Instantiates new Options.
     */
    public Options(){
        options = new ArrayList<>();
    }

    /**
     * If the <code>Pair</code> with the given <code>parameter</code> exists, it will be replaced by it.
     * Otherwise add a new <code>Pair</code> with <code>parameter</code> and <code>value</code>.
     *
     * @param parameter the parameter of the option
     * @param value     the value of the parameter
     */
    public void set(P parameter, V value){
        for (Pair<P, V> e : options) {
            if(e.first.equals(parameter)) {
                options.remove(e);
                break;
            }
        }

        options.add(new Pair<P,V>(parameter,value));
    }

    /**
     * Set the Option at index <code>id</code> with the given <code>value</code>.
     *
     * @param index    the id of the option
     * @param value the value of the found parameter
     * @return true if the option was found
     */
    public boolean set(int index, V value){
        if(index>=size())
            return false;
        options.set(index, new Pair<P, V>(options.get(index).first, value));
        return true;
    }

    /**
     * Remove parameter from options.
     *
     * @param parameter the parameter to remove
     */
    public boolean removeParameter(P parameter){
        for (Pair<P, V> e : options) {
            if(e.first.equals(parameter)) {
                options.remove(e);
                return true;
            }
        }
        return false;
    }

    /**
     * Size of the options list.
     *
     * @return Amount of options
     */
    public int size(){
        return options.size();
    }

    /**
     * Get value of the option by id
     *
     * @param id id of the option
     * @return Value of the parameter
     */
    public V getValue(int id){
        return options.get(id).second;
    }

    /**
     * Get parameter of the option by id.
     *
     * @param id the id of the option
     * @return the parameter of the option
     */
    public P getParameter(int id){
        return options.get(id).first;
    }

    /**
     * Get option by id
     *
     * @param id id of the option
     * @return the option
     */
    public Pair<P,V> getEntry(int id){
        return options.get(id);
    }
}
