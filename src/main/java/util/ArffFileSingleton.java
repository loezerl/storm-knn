package util;

import moa.streams.ArffFileStream;

/**
 * Created by loezerl-fworks on 10/10/17.
 */
public class ArffFileSingleton {
    private static ArffFileStream instance;

    protected ArffFileSingleton(){}

    public static synchronized ArffFileStream getInstance(){
        if(instance == null){
            instance = new ArffFileStream();
        }
        return instance;
    }
    public static synchronized void setInstance(ArffFileStream obj){
        instance = obj;
    }
}
