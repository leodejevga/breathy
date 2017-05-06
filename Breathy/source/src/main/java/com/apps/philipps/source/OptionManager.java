package com.apps.philipps.source;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Jevgenij Huebert on 28.01.2017. Project Breathy
 */

/**
 * OptionManager provide a List of OptionManager.
 *
 * @param <P> the type of the Option
 * @param <V> the type of the Value of the Option
 */
public class OptionManager<P extends Serializable,V extends Serializable> implements Serializable{
    private ArrayList<Option<P, V>> options;

    public ArrayList<Option<P, V>> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<Option<P, V>> options) {
        this.options = options;
    }

    /**

     * Instantiates new OptionManager.
     */
    public OptionManager(){
        options = new ArrayList<>();
    }

    public void add(Option option){
        options.add(option);
    }
    public void add(P parameter, V value){
        options.add(new Option<P, V>(parameter, value));
    }
    public void add(P parameter, V value, int price){
        options.add(new Option<P, V>(parameter, value, price));
    }

    /**
     * If the <code>Option</code> with the given <code>parameter</code> exists, it will be replaced by it.
     * Otherwise add a new <code>Option</code> with <code>parameter</code> and <code>value</code>.
     *
     * @param parameter the parameter of the option
     * @param value     the value of the parameter
     */
    public void set(P parameter, V value){
        for (Option<P, V> e : options) {
            if(e.Parameter.equals(parameter)) {
                options.remove(e);
                break;
            }
        }

        options.add(new Option<P,V>(parameter,value));
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
        options.set(index, new Option<P, V>(options.get(index).Parameter, value));
        return true;
    }

    /**
     * Remove parameter from options.
     *
     * @param parameter the parameter to remove
     */
    public boolean removeParameter(P parameter){
        for (Option<P, V> e : options) {
            if(e.Parameter.equals(parameter)) {
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
     * @param index id of the option
     * @return Value of the parameter
     */
    public V getValue(int index){
        if (index>=size())
            return null;
        return options.get(index).Value;
    }

    /**
     * Get parameter of the option by id.
     *
     * @param index the id of the option
     * @return the parameter of the option
     */
    public P getParameter(int index){
        if (index>=size())
            return null;
        return options.get(index).Parameter;
    }

    /**
     * Get option by id
     *
     * @param id id of the option
     * @return the option
     */
    public Option<P,V> getOption(int id){
        return options.get(id);
    }

    public static class Option<P,V> implements Serializable{
        public P Parameter;

        public P getParameter() {
            return Parameter;
        }

        public void setParameter(P parameter) {
            Parameter = parameter;
        }

        public V getValue() {
            return Value;
        }

        public void setValue(V value) {
            Value = value;
        }

        public int getPrice() {
            return Price;
        }

        public void setPrice(int price) {
            Price = price;
        }

        public V Value;
        public int Price;
        public Option(P parameter, V value, int cost){
            this.Parameter = parameter;
            this.Value = value;
            this.Price = cost;
        }
        public Option(P parameter, V value){
            this.Parameter = parameter;
            this.Value = value;
            this.Price = 0;
        }
    }
}
