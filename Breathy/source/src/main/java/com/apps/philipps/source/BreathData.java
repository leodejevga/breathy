package com.apps.philipps.source;


import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jevgenij Huebert on 29.01.2017. Project Breathy
 */
public class BreathData {
    public Context activity;
    public BreathData(Context context){
        this.activity = context;
    }
    //TODO: Hier werden Bluetooth Daten gesammelt und live gefiltert, sodass sie wieder als Rückmeldung fungieren können
    //Diese Klasse soll statisch agieren
    private static LimitedList Data;
    private static int ramSize = 67108864; //256 MB
    private static boolean initialized = false;

    /**
     * Initialize BreathData to perform saving the integer values in RAM and hard drive. Choose your own size of RAM
     *
     * @param ramSize the size of Data in RAM. <code>ramSize==0</code> sets no Limit to RAM
     * @return the boolean
     */
    public static boolean init(int ramSize){
        boolean result = init();
        if(result) BreathData.ramSize = ramSize;
        return result;
    }

    /**
     * Initialize BreathData to perform saving the integer values in RAM and hard drive.
     *
     * @return the boolean
     */
    public static boolean init(){
        if(!initialized){
            Data = new LimitedList();
            initialized = true;
            return true;
        }
        return false;
    }

    /**
     * Adds a Value to Data. If the Limit of RAM size is reached, the Data will written on the hard drive.
     *
     * @param value the Value to add
     */
    public static void add(Integer value){
        Data.add(value);
    }

    private static class LimitedList extends ArrayList<Integer>{
        /**
         * Instantiates a new Limited list.
         */
        private static SaveData<Integer> saveData=null;

        /**
         * Instantiates a new Limited list.
         */
        public LimitedList(){
            super();
            if(ramSize!=0) {
                //TODO save bluetooth data
                //saveData = new SaveData<>("BluetoothData");
            }
        }

        @Override
        public boolean add(Integer t) {
            super.add(t);
            if(saveData!=null && size()>ramSize) {
                //TODO save bluetooth data
                //saveData.write(remove(size()-1));
                return true;
            }
            return false;
        }

        @Override
        public Integer get(int index) {
            if(index<size() && index>=0)
                return super.get(index);
            else if(saveData!=null && size()==ramSize && index>=ramSize)
                //TODO read bluetooth data
                //return saveData.read(index);
                return 1;
            else return null;
        }
    }

    /**
     * Get list of values from <code>index</code> to <code>index+range</code>
     *
     * @param index the index start to read
     * @param range the range of values to get
     * @return the list of values at index with size of range
     */
    public static List<Integer> get(int index, int range){
        List<Integer> result = new ArrayList<>();
        for (int i=index; i<index+range; i++){
            result.add(Data.get(i));
        }
        return result;
    }

    /**
     * Get integer.
     *
     * @param index the index of the Value
     * @return the integer at index, null if IndexOutOfRange
     */
    public static Integer get(int index){
        return Data.get(index);
    }

}
