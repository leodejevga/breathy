package com.apps.philipps.source;

/**
 * Created by Jevgenij Huebert on 29.01.2017. Project Breathy
 */

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Objects;

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

    public static Object readFile(@NonNull String path) {
        Object result = null;
        File file = new File(AppState.DATA_STORAGE + File.separator + path);
        try {
            ObjectInputStream fileInputStream = new ObjectInputStream(new FileInputStream(file));
            result = fileInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void writeFile(@NonNull String path, Serializable serializable) {
        File file = new File(AppState.DATA_STORAGE + File.separator + path);
        try {
            ObjectOutputStream fileOutputStream = new ObjectOutputStream(new FileOutputStream(file));
            fileOutputStream.writeObject(serializable);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void savePlanManager(Activity activity) {
        try {
            File file = new File(AppState.PLAN_STORAGE);
            if (AppState.verifyStoragePermissions(activity)) {
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
                PlanManager.PlanManagerInstance planManagerInstanceForTesting = new PlanManager.PlanManagerInstance();
                oos.writeObject(planManagerInstanceForTesting);
                loadPlanManager();
                if (planManagerInstanceForTesting.compareTo(new PlanManager.PlanManagerInstance()) == 0) {
                    Toast.makeText(activity, "Plans are saved correctly", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(activity, "An error has been occurred", Toast.LENGTH_LONG).show();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadPlanManager() {
        try {
            File file = new File(AppState.PLAN_STORAGE);
            if (file.exists()) {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
                PlanManager.PlanManagerInstance instance = (PlanManager.PlanManagerInstance) ois.readObject();
                PlanManager.init(instance);
            }
        } catch (ClassNotFoundException | IOException | PlanManager.PlanManagerAlreadyInitialized e) {
            e.printStackTrace();
        }
    }

}
