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
 * Created by leode on 28.01.2017.
 */

public class Options<K,V> {
    private ArrayList<Pair<K, V>> options;

    public Options(){
        options = new ArrayList<>();
    }
    public void set(K parameter, V value){
        for (Pair<K, V> e : options) {
            if(e.first.equals(parameter)) {
                options.remove(e);
                break;
            }
        }
        options.add(new Pair<K,V>(parameter,value));
    }
    public boolean set(int id, V value){
        if(id>=size())
            return false;
        options.set(id, new Pair<K, V>(options.get(id).first, value));
        return true;
    }
    public void removeParameter(K parameter){
        for (Pair<K, V> e : options) {
            if(e.first.equals(parameter)) {
                options.remove(e);
                return;
            }
        }
    }

    public int size(){
        return options.size();
    }

    public V getVlaue(int id){
        return options.get(id).second;
    }
    public K getParameter(int id){
        return options.get(id).first;
    }
    public Pair<K,V> getEntry(int id){
        return options.get(id);
    }
}
