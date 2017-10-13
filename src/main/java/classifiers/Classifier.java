package classifiers;

import com.yahoo.labs.samoa.instances.Instance;

/**
 * Created by loezerl-fworks on 24/08/17.
 */
public class Classifier {

    private static Classifier instance;

    protected Classifier(){}

    public static synchronized Classifier getInstance(){
        if(instance == null){
            instance = new Classifier();
        }
        return instance;
    }

    public static synchronized void setInstance(Classifier obj){
        instance = obj;
    }

    public boolean test(Instance example) throws Exception{return false;}
    public synchronized void  train(Instance example){}
}
