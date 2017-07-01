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

    private static RAM ram;
    private static int ramSize = 6;
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
                ram.addData(i);
        }
    }

    public static void add(int... values) {
        if (AppState.recordData) {
            for (int i : values)
                ram.addData(i);
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
    public static Element[] get(int index, int range) {
        Element[] result = new Element[range];
        for (int i = index; i < index + range; i++)
            result[i - index] = ram.getData(i);
        return result;
    }

    /**
     * Get stream of data as String from index with the amount of range
     *
     * @param index start of stream
     * @param range amount of elements
     * @return stream of data as String
     */
    public static String getAsString(int index, int range) {
        String result = "";
        Element[] elements = get(index, range);
        for (Element element : elements)
            result += ", " + element;
        return result.length() > 1 ? result.substring(2) : "";
    }

    /**
     * Get integer.
     *
     * @param index the index of the Value
     * @return the integer at index, null if IndexOutOfRange
     */
    public static DataBlock get(int index) {
        return ram.get(index);
    }

    public static void saveRest() {
        ram.saveAll();
        DataBlock.info.save();
    }


    private static class RAM extends ArrayList<DataBlock> {
        /**
         * Instantiates a new Limited list.
         */
        private SaveData<DataBlock> saveData = null;

        /**
         * Instantiates a new Limited list.
         */
        public RAM(Context context) {
            super();
            if (ramSize != 0) {
                saveData = new SaveData<>(context);
            }
        }

        @Override
        public boolean add(DataBlock t) {
            super.add(0, t);

            if (size() > ramSize) {
                DataBlock toSave = get(size() - 1);
                saveData.writeObject(toSave.getName(), toSave);
                return true;
            }
            return false;

        }

        @Override
        public DataBlock get(int index) {
            index = DataBlock.info.names.size() - 1 - index;
            if (index < 0)
                return null;

            if (index < size())
                return super.get(index);

            String name = DataBlock.getName(index);
            if (DataBlock.contains(name))
                return saveData.readObject(name);
            return null;
        }

        public boolean addData(int i) {
            Element save = new Element(i, Calendar.getInstance());
            if (get(0).add(save))
                return false;
            add(0, new DataBlock(save));
            return true;
        }

        public Element getData(int i) {
            if (i < blockSize) {
                Element result = get(0).get(i);
                if (result == null)
                    i -= blockSize - get(0).index;
                else return result;
            }
            DataBlock block = get(i / blockSize + 1);
            if (block == null)
                return null;
            return block.get(i % blockSize);
        }

        public void saveAll() {
            for (DataBlock block : this)
                saveData.writeObject(block.getName(), block);
        }
    }

    public static class DataInfo implements Serializable {
        private List<String> names;
        public static final String TAG = "BreathDataInfo";
        public SaveData<DataInfo> save;

        public DataInfo(Context context) {
            this.save = new SaveData<>(context);
            DataInfo save = this.save.readObject(TAG);
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

        public void save() {
            save.writeObject(TAG, this);
        }
    }

    public static class DataBlock implements Serializable {
        private Element[] elements;
        private int index = 0;
        private int id = 0;
        private static DataInfo info;
        private static final String TAG = "BreathDataBlock";


        public DataBlock(Element element) {
            this.id = info.names.size();
            info.add(getName());
            elements = new Element[blockSize];
            elements[index++] = element;
        }

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

        public Element get(int i) {
            if (i < index)
                return elements[i];
            return null;
        }

        public String getName() {
            return TAG + id;
        }

        @Override
        public String toString() {
            return getName() + ": #" + index;
        }
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
