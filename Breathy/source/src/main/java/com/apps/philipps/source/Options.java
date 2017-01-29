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
 * @param <K> the type of the Option
 * @param <V> the type of the Value of the Option
 */
public class Options<K,V> {
    private ArrayList<Pair<K, V>> options;

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
    public void set(K parameter, V value){
        for (Pair<K, V> e : options) {
            if(e.first.equals(parameter)) {
                options.remove(e);
                break;
            }
        }

        options.add(new Pair<K,V>(parameter,value));
    }

    /**
     * Set the Option at index <code>id</code> with the given <code>value</code>.
     *
     * @param id    the id of the option
     * @param value the value of the found parameter
     * @return true if the option was found
     */
    public boolean set(int id, V value){
        if(id>=size())
            return false;
        options.set(id, new Pair<K, V>(options.get(id).first, value));
        return true;
    }

    /**
     * Remove parameter from options.
     *
     * @param parameter the parameter to remove
     */
    public void removeParameter(K parameter){
        for (Pair<K, V> e : options) {
            if(e.first.equals(parameter)) {
                options.remove(e);
                return;
            }
        }
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
    public K getParameter(int id){
        return options.get(id).first;
    }

    /**
     * Get option by id
     *
     * @param id id of the option
     * @return the option
     */
    public Pair<K,V> getEntry(int id){
        return options.get(id);
    }
}
