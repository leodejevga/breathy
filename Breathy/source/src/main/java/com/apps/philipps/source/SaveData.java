package com.apps.philipps.source;

/**
 * Created by Jevgenij Huebert on 29.01.2017. Project Breathy
 */

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;

/**
 * This Class must be instatiate from Backend Classes and save the specific data on the hard drive
 */
public class SaveData<T extends Serializable> {
    private Context context;

    public SaveData(Context context) {
        this.context = context;
    }

    public boolean writeObject(String key, T object) {
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(key, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(object);
            oos.close();
            fos.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public T readObject(String key) {
        FileInputStream fis = null;
        try {
            fis = context.openFileInput(key);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Object object = ois.readObject();
            return (T) object;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object readFile(@NonNull String name) {
        Object result = null;
        File file = new File(AppState.BREATHY_STORAGE + name);
        try {
            ObjectInputStream fileInputStream = new ObjectInputStream(new FileInputStream(file));
            result = fileInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void writeFile(@NonNull String name, Serializable serializable) {
        try {
            File file = new File(AppState.BREATHY_STORAGE + name);
            File folder = new File(AppState.BREATHY_STORAGE);
            if (!folder.exists())
                folder.mkdir();
            ObjectOutputStream fileOutputStream = new ObjectOutputStream(new FileOutputStream(file));
            fileOutputStream.writeObject(serializable);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void savePlanManager(Activity activity) {
        try {
            if (AppState.verifyStoragePermissions(activity)) {
                File file = new File(AppState.PLAN_STORAGE);
                File folder = new File(AppState.BREATHY_STORAGE);
                if (!folder.exists())
                    folder.mkdir();
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
                PlanManager.PlanManagerInstance planInstance = new PlanManager.PlanManagerInstance();
                oos.writeObject(planInstance);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadPlanManager() throws PlanManager.PlanManagerAlreadyInitialized {
        try {
            File file = new File(AppState.PLAN_STORAGE);
            if (file.exists()) {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
                PlanManager.PlanManagerInstance instance = (PlanManager.PlanManagerInstance) ois.readObject();
                PlanManager.init(instance);
            }
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        PlanManager.init();
    }

    public static void saveDataBlock(BreathData.DataBlock block) {
        try {
            File file = new File(AppState.BREATHY_STORAGE + block.getName());
            File folder = new File(AppState.BREATHY_STORAGE);
            if (!folder.exists())
                folder.mkdir();

            FileWriter bw = new FileWriter(file);

            BreathData.DataInfo i = block.info;
            PlanManager.Plan p = i.plan;
            String toWrite = BreathData.DataBlock.TAG + ":\n\n" +
                    "Information:\n" +
                    "\tgame = " + i.game.getName() + "\n" +
                    "\tfinish time = " + i.date.getTimeInMillis() + "\n" +
                    "\tPlan: " + p.getName() + "\n";

            bw.write(toWrite);
            toWrite = "";
            for (PlanManager.Plan.Option o : p) {
                toWrite += "\t\tOption = " + o.getName() + "\n" +
                        "\t\t\tin = " + o.getIn() + "\n" +
                        "\t\t\tout = " + o.getOut() + "\n" +
                        "\t\t\ttime = " + o.getDuration() + "\n" +
                        "\t\t\tfrequency = " + o.getFrequency() + "\n";
            }
            toWrite += "Data:\n";
            bw.write(toWrite);
            for (BreathData.Element e : block.ram) {
                bw.write("\t" + e.date.getTimeInMillis() + ": " + e.data + "\n");
            }
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
