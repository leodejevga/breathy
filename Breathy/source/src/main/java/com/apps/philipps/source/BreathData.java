package com.apps.philipps.source;


import android.content.Context;
import android.util.Log;

import com.apps.philipps.source.interfaces.IObserver;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jevgenij Huebert on 29.01.2017. Project Breathy
 */
public class BreathData {
    //TODO: Hier werden Bluetooth Daten gesammelt und live gefiltert, sodass sie wieder als Rückmeldung fungieren können
    //Diese Klasse soll statisch agieren
    private static LimitedList Data;
    private static int ramSize = 67108864; //256 MB
    private static int blockSize = 0;
    private static boolean initialized = false;

    /**
     * Initialize BreathData to perform saving the integer values in RAM and hard drive. Choose your own size of RAM
     *
     * @param ramSize the size of Data in RAM. <code>ramSize==0</code> sets no Limit to RAM
     * @return the boolean
     */
    public static boolean init(Context context, int ramSize){
        boolean result = init(context);
        if(result){
            BreathData.ramSize = ramSize;
            blockSize = ramSize/2;
        }
        return result;
    }

    /**
     * Initialize BreathData to perform saving the integer values in RAM and hard drive.
     *
     * @return the boolean
     */
    public static boolean init(Context context){
        if(!initialized){
            blockSize = ramSize/2;
            Data = new LimitedList(context);
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
    private static List<IObserver> observer = new ArrayList<>();
    public static void setObserver(IObserver observer) {
        BreathData.observer.add(observer);
    }

    public static void add(String value){
        if(AppState.recordData){
            Log.d("Message", " " + value);
            for(int i : convert(value))
                Data.add(i);
        }
    }
    private static int[] convert(String value){
        String[] values = value.split("\r\n|\r|\n");
        int[] result = new int[values.length];
        for (int i=0; i<result.length; i++) {
            try {
                result[i] = Integer.parseInt(values[i]);
            }
            catch (NumberFormatException e){
                e.printStackTrace();
                if(i>0)
                    result[i] = result[i-1];
                Log.d("Format Exception", "" + values[i]);
            }
        }
        return result;
    }

    /**
     * Get list of values from <code>index</code> to <code>index+range</code>
     *
     * @param index the index start to read
     * @param range the range of values to get
     * @return the list of values at index with size of range
     */
    public static Integer[] get(int index, int range){
        Integer[] result = new Integer[range];
        for (int i=index; i<index+range; i++){
            result[i] = Data.get(i);
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


    private static class LimitedList extends ArrayList<Integer>{
        /**
         * Instantiates a new Limited list.
         */
        private long block;
        private SaveData<Object[]> saveData=null;

        /**
         * Instantiates a new Limited list.
         */
        public LimitedList(Context context){
            super();
            if(ramSize!=0) {
                block = 0L;
                saveData = new SaveData<>(context);
            }
        }

        @Override
        public boolean add(Integer t) {
            super.add(0,t);
            if(saveData!=null && size()>ramSize) {
                Object[] data = subList(size()-blockSize, size()-1).toArray();
                saveData.writeObject("DataKey_" + block, data);
                block++;
                removeRange(size()-blockSize, size()-1);
                return true;
            }
            for(IObserver o : observer)
                o.call(t);
            return false;
        }

        @Override
        public Integer get(int index) {
            if(index<size() && index>=0)
                return super.get(index);
            else if(saveData!=null && index>=size()) {
                //TODO read bluetooth data
                int i = (index - size()) / blockSize + 1;
                if (i < block) {
                    Object[] block = saveData.readObject("DataKey_" + i);
                    i = (index - size()) % blockSize-1;
                    return (Integer) block[i];
                }
            }
            return null;
        }
    }

}
