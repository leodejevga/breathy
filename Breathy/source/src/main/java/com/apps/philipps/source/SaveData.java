package com.apps.philipps.source;

/**
 * Created by Jevgenij Huebert on 29.01.2017. Project Breathy
 */

import android.content.Context;
import android.support.annotation.NonNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
        }catch (IOException e) {
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
    public static byte[] readFile(@NonNull String path){
        File file = new File(path);
        byte[] data = new byte[(int) file.length()];
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            fileInputStream.read(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    public static void writeFile(@NonNull String path, byte[] data){
        File file = new File(path);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
