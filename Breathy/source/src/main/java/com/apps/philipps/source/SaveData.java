package com.apps.philipps.source;

/**
 * Created by Jevgenij Huebert on 29.01.2017. Project Breathy
 */

import java.io.IOException;
import java.io.OutputStream;

/**
 * This Class must be instatiate from Backend Classes and save the specific data on the hard drive
 */
public class SaveData<T> {
    private String fileName;
    //TODO: Diese Klasse soll von den Objekten Backend erstellt werden und sich darum kümmern bestimmte Daten auf die Festplatte zu speichern

    public SaveData(String fileName){
        this.fileName = fileName;
    }

    public void write(T data){

    }

    public T read(){
        return read(0);
    }

    public T read(int index){
        return null;
    }
}
