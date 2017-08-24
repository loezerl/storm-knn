package evaluators;

import classifiers.Classifier;
import moa.streams.ArffFileStream;

/**
 * Created by loezerl-fworks on 24/08/17.
 */
public class Evaluator {
    public Classifier mClassifier;
    public ArffFileStream data_source;

    public Evaluator(Classifier _classifier, ArffFileStream _data){
        mClassifier = _classifier;
        data_source = _data;
    }

    public void run() throws Exception{}
}
