package com.apps.philipps.source;


import android.content.Context;
import android.util.Log;

import com.apps.philipps.source.interfaces.IObserver;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Jevgenij Huebert on 29.01.2017. Project Breathy
 */
public abstract class BreathData {
    //TODO: Hier werden Bluetooth Daten gesammelt und live gefiltert, sodass sie wieder als Rückmeldung fungieren können
    //Diese Klasse soll statisch agieren
    private static RAM ram;
    private static int ramSize = 5;
    private static int blockSize = 3;
    private static boolean initialized = false;

    /**
     * Initialize BreathData to perform saving the integer values in RAM and hard drive. Choose your own size of RAM
     *
     * @param context the context
     * @param ramSize the size of ram in RAM. <code>ramSize==0</code> sets no Limit to RAM
     * @return the boolean
     */
    public static boolean init(Context context, int ramSize) {
        boolean result = init(context);
        if (result) {
            BreathData.ramSize = ramSize;
            blockSize = ramSize / 2;
        }
        return result;
    }

    /**
     * Initialize BreathData to perform saving the integer values in RAM and hard drive.
     *
     * @param context the context
     * @return the boolean
     */
    public static boolean init(Context context) {
        if (!initialized) {
            DataBlock.info = new DataInfo(context);
            ram = new RAM(context);
            initialized = true;
            return true;
        }
        return false;
    }

    private static List<IObserver> observer = new ArrayList<>();

    /**
     * Sets observer.
     *
     * @param observer the observer
     */
    public static void addObserver(IObserver observer) {
        BreathData.observer.add(observer);
    }

    public static boolean removeObserver(IObserver observer) {
        return BreathData.observer.remove(observer);
    }

    /**
     * Adds a Value to ram. If the Limit of RAM size is reached, the ram will written on the hard drive.
     *
     * @param value the Value to add
     */
    public static void add(String value) {
        if (AppState.recordData) {
            for (int i : convert(value))
                ram.add(i);
        }
    }

    public static void add(int... values) {
        if (AppState.recordData) {
            for (int i : values)
                ram.add(i);
        }
    }

    private static int[] convert(String value) {
        String[] values = value.split("\r\n|\r|\n");
        int[] result = new int[values.length];
        for (int i = 0; i < result.length; i++) {
            try {
                result[i] = Integer.parseInt(values[i]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                if (i > 0)
                    result[i] = result[i - 1];
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
    public static Integer[] get(int index, int range) {
        Integer[] result = new Integer[range];
        for (int i = index; i < index + range; i++) {
            result[i] = ram.get(i);
        }
        return result;
    }

    public static String getAsString(int index, int range) {
        String result = "";
        for (int i = index; i < index + range; i++) {
            if (ram.get(i) != null)
                result += ", " + ram.get(i);
        }
        return result.length() > 1 ? result.substring(2) : "";
    }

    /**
     * Get integer.
     *
     * @param index the index of the Value
     * @return the integer at index, null if IndexOutOfRange
     */
    public static Integer get(int index) {
        return ram.get(index);
    }


    private static class RAM extends ArrayList<DataBlock> {
        /**
         * Instantiates a new Limited list.
         */
        private SaveData<Object[]> saveData = null;

        /**
         * Instantiates a new Limited list.
         */
        public RAM(Context context) {
            super();
            if (ramSize != 0) {
                block = 0L;
                saveData = new SaveData<>(context);
            }
        }

        @Override
        public boolean add(DataBlock t) {
            super.add(0, t);

            Log.d("ram", t + "");
            if (saveData != null && size() > ramSize) {
                Object[] data = subList(size() - blockSize, size() - 1).toArray();
                saveData.writeObject("DataKey_" + block, data);
                block++;
                removeRange(size() - blockSize, size() - 1);
                return true;
            }
            for (IObserver o : observer)
                if (o != null)
                    o.call(t);
            return false;
        }

        @Override
        public DataBlock get(int index) {
            if (index < size() && index >= 0)
                return super.get(index);
            else if (saveData != null && index >= size()) {
                int i = (index - size()) / blockSize + 1;
                if (i < block) {
                    Object[] block = saveData.readObject("DataKey_" + i);
                    i = (index - size()) % blockSize - 1;
                    return (Integer) block[i];
                }
            }
            return null;
        }

    }

    public static class DataInfo implements Serializable {
        private List<String> names;
        public static final String TAG = "BreathDataInfo";

        public DataInfo(Context context) {
            DataInfo save = (DataInfo) new SaveData<>(context).readObject(TAG);
            if (save == null)
                names = new ArrayList<>();
            else
                names = save.names;
        }

        public boolean contains(String name) {
            return names.contains(name);
        }

        public boolean add(String name) {
            return names.add(name);
        }

        public boolean remove(String name) {
            return names.remove(name);
        }

    }

    public static class DataBlock implements Serializable {
        private Element[] elements;
        private int index = 0;
        private static DataInfo info;
        private static final String TAG = "BreathDataBlock";

        public static String getName(int id) {
            return TAG + id;
        }

        public static boolean contains(int id) {
            return info.contains(getName(id));
        }

        public static boolean contains(String name) {
            return info.contains(name);
        }

        public boolean add(Element element) {
            if (index == elements.length)
                return false;
            elements[index++] = element;
            return true;
        }

        public static class Element {
            public Integer data;
            public Calendar date;

            public Element(Integer data, Calendar date) {
                this.data = data;
                this.date = date;
            }

            @Override
            public String toString() {
                return data + " at " + date.get(Calendar.HOUR_OF_DAY) + ":" + date.get(Calendar.MINUTE) + ":" + date.get(Calendar.SECOND);
            }
        }
    }

}
