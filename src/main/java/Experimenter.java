import classifiers.Classifier;
import classifiers.KNN;
import com.yahoo.labs.samoa.instances.Instance;
import evaluators.Evaluator;
import evaluators.Prequential;
import moa.streams.ArffFileStream;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.utils.Utils;

import java.io.*;

/**
 * Created by loezerl-fworks on 23/08/17.
 */



public class Experimenter {

    public static Instance _Example;

    public static void SerializeClassifier(Classifier classifier, String path){
        try {
            FileOutputStream fileOut =
                    new FileOutputStream(path);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(classifier);
            out.close();
            fileOut.close();
            System.out.printf("Serialized data is saved in" + path);
        }catch(IOException i) {
            i.printStackTrace();
            System.exit(1);
        }

    }

    public static void main(String[] args) {


        TopologyBuilder builder = new TopologyBuilder();
        Config conf = new Config();


        String DIABETES_DATABASE = "/home/loezerl-fworks/IdeaProjects/Experimenter/diabetes.arff";
        String KYOTO_DATABASE = "/home/loezerl-fworks/Downloads/kyoto.arff";

        ArffFileStream file = new ArffFileStream(DIABETES_DATABASE, -1);

        conf.put("arff_file", DIABETES_DATABASE);

        KNN myClassifier = new KNN(7, 25, "euclidean");
        Evaluator myEvaluator = new Prequential(myClassifier, file, builder, conf);
        String path = "knn.ser";
        SerializeClassifier(myClassifier, path);
        conf.put("classifier_path", path);

        conf.setDebug(false);
        conf.setMaxTaskParallelism(3);
        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("storm-knn", conf, builder.createTopology());
        Utils.sleep(10000);
        cluster.killTopology("storm-knn");
        cluster.shutdown();
    }

}
