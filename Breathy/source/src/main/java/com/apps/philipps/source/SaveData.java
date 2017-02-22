package com.apps.philipps.source;

/**
 * Created by Jevgenij Huebert on 29.01.2017. Project Breathy
 */

import android.content.Context;

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
    //TODO : rename the Class to "DataManager" ?

    public SaveData(Context context) {
        this.context = context;
    }

    public void writeObject(String key, T object) throws IOException {
        FileOutputStream fos = context.openFileOutput(key, Context.MODE_PRIVATE);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(object);
        oos.close();
        fos.close();
    }

    public T readObject(String key) throws IOException,
            ClassNotFoundException {
        FileInputStream fis = context.openFileInput(key);
        ObjectInputStream ois = new ObjectInputStream(fis);
        Object object = ois.readObject();
        return (T) object;
    }
}
