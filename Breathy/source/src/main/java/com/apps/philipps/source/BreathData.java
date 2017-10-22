package com.apps.philipps.source;


import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.provider.ContactsContract;
import android.util.Log;

import com.apps.philipps.source.interfaces.IObserver;

import java.io.File;
import java.io.FilenameFilter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Jevgenij Huebert on 29.01.2017. Project Breathy
 */
public abstract class BreathData {

    private static List<IObserver> observer = new ArrayList<>();
    private static RAM ramTemp;
    private static RAM ramGame;
    private static int blockSize = 500;
    private static boolean initialized = false;

    /**
     * Initialize BreathData to perform saving the integer values in RAM and hard drive.
     *
     * @return the boolean
     */
    public static boolean init() {
        if (!initialized) {
            ramTemp = new RAM(500);
            ramGame = new RAM();
            initialized = true;
            return true;
        }
        return false;
    }


    /**
     * Sets observer.
     *
     * @param observer the observer
     */
    public static void addObserver(IObserver observer) {
        if (!BreathData.observer.contains(observer))
            BreathData.observer.add(observer);
    }

    public static boolean removeObserver(IObserver observer) {
        return BreathData.observer.remove(observer);
    }


    public static void add(Element... elements) {
        for (Element element : elements) {
            if (AppState.recordData)
                ramGame.add(element);
            ramTemp.add(element);
        }
    }

    /**
     * Get list of values from <code>index</code> to <code>index+range</code>
     *
     * @param index the index position to read
     * @param range the range of values to get
     * @return the list of values at index with size of range
     */
    public static Element[] get(int index, int range) {
        Element[] result = new Element[range];
        for (int i = index; i < index + range; i++) {
            result[i - index] = ramTemp.get(i);
            if (result[i - index] == null)
                return result;
        }
        return result;
    }

    /**
     * Get stream of data as String from index with the amount of range
     *
     * @param index position of stream
     * @param range amount of elements
     * @return stream of data as String
     */
    public static String getAsString(int index, int range) {
        String result = "";
        Element[] elements = get(index, range);
        for (Element element : elements)
            if (element != null)
                result += ", " + element.data;
        return result.length() > 1 ? result.substring(2) : "";
    }

    /**
     * Get integer.
     *
     * @param index the index of the Value
     * @return the integer at index, null if IndexOutOfRange
     */
    public static Element get(int index) {
        Element result = ramTemp.get(index);
        if (result == null)
            return ramGame.get(index);
        return result;
    }

    public static void save(Class gameClass) {
        ramGame.save(gameClass);
    }


    private static class RAM extends ArrayList<Element> implements Serializable {
        private int ramSize;

        /**
         * Instantiates a new Limited list.
         */
        public RAM() {
            this(-1);
        }

        public RAM(int ramSize) {
            super();
            this.ramSize = ramSize;

        }

        @Override
        public synchronized boolean add(Element element) {
            add(0, element);
            return true;
        }

        @Override
        public synchronized void add(int index, Element t) {
            super.add(index, t);

            if (size() > ramSize && ramSize >= 0) {
                remove(size() - 1);
            }

        }

        @Override
        public Element get(int index) {
            if (index >= size())
                return null;
            return super.get(index);
        }

        public synchronized void save(Class gameClass) {
            DataBlock dataBlock = new DataBlock(this, gameClass);
            SaveData.writeFile(dataBlock.getName(), dataBlock);
            clear();
        }
    }

    public static class DataInfo implements Serializable {
        public static final String TAG = "BreathDataInfo";
        public Calendar date;
        public PlanManager.Plan plan;

        @Override
        public String toString() {
            return plan.getName() + "_" + date.get(Calendar.HOUR_OF_DAY) + ":" + date.get(Calendar.MINUTE) + ":" + date.get(Calendar.SECOND) + ":" + date.get(Calendar.MILLISECOND);
        }

        public DataInfo() {
            date = Calendar.getInstance();
            plan = PlanManager.getCurrentPlan();
        }
    }

    public static class DataBlock implements Serializable {
        public RAM ram;
        public Class game;
        public DataInfo info;
        private static final String TAG = DataBlock.class.getSimpleName();

        public DataBlock(RAM ram, Class game) {
            this.game = game;
            this.ram = ram;
            this.info = new DataInfo();
        }

        public static String[] getNames(final Class gameClass) {
            File f = new File(AppState.DATA_STORAGE);
            return f.list(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.contains(TAG + "_" + gameClass.getSimpleName());
                }
            });
        }

        public String getName() {
            return TAG + "_" + game.getSimpleName() + "_" + info;
        }

        @Override
        public String toString() {
            return getName();
        }
    }

    public static class Element implements Serializable {
        public double data;
        public Calendar date;

        public Element(Integer data) {
            this(data.doubleValue());
        }

        public Element(double data) {
            this.data = data;
            this.date = Calendar.getInstance();
        }

        @Override
        public String toString() {
            return data + " at " + date.get(Calendar.HOUR_OF_DAY) + ":" + date.get(Calendar.MINUTE) + ":" + date.get(Calendar.SECOND) + ":" + date.get(Calendar.MILLISECOND);
        }
    }

}
