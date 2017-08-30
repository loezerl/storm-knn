import classifiers.Classifier;
import classifiers.KNN;
import com.yahoo.labs.samoa.instances.Instance;
import evaluators.Evaluator;
import evaluators.Prequential;
import moa.streams.ArffFileStream;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import org.apache.storm.utils.Utils;
import util.GetInstances;
import util.InstanceDouble;
import util.Similarity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by loezerl-fworks on 23/08/17.
 */




public class Experimenter {

    public static Instance _Example;

    public static class ExclamationBolt extends BaseRichBolt {
        OutputCollector _collector;

        @Override
        public void prepare(Map conf, TopologyContext context, OutputCollector collector) {
            _collector = collector;
        }

        @Override
        public void execute(Tuple tuple) {
            _collector.emit(tuple, new Values(tuple.getString(0) + "!!!"));
            _collector.ack(tuple);
        }

        @Override
        public void declareOutputFields(OutputFieldsDeclarer declarer) {
            declarer.declare(new Fields("word"));
        }

    }

    public static class EuclideanDistanceBolt extends BaseRichBolt{
        OutputCollector _collector;
        Classifier classsss;
        @Override
        public void prepare(Map conf, TopologyContext context, OutputCollector collector) {
            _collector = collector;
            classsss = new KNN((int)conf.get("knn-n"), (int)conf.get("knn-wsize"), conf.get("knn-f").toString());
        }

        @Override
        public void execute(Tuple tuple) {

            try {
                    Instance value = (Instance)tuple.getValue(0);
                    Double dist = Similarity.EuclideanDistance(value, _Example);
//                    System.out.println("[" + value + "] - [" + dist + "]");
                    _collector.emit(tuple, new Values(value, dist));
                    _collector.ack(tuple);
            } catch(Exception e){
                _collector.reportError(e);
            }
        }

        @Override
        public void declareOutputFields(OutputFieldsDeclarer declarer) {
            declarer.declare(new Fields("euclidean_distance"));
        }
    }



    public static void main(String[] args) throws Exception {
        TopologyBuilder builder = new TopologyBuilder();


        ArffFileStream file = new ArffFileStream("/home/loezerl-fworks/IdeaProjects/Experimenter/diabetes.arff", -1);

        _Example = file.nextInstance().getData();

        Config conf = new Config();


        //"/home/loezerl-fworks/Downloads/kyoto.arff"
        // "/home/loezerl-fworks/IdeaProjects/Experimenter/diabetes.arff"
        conf.put("arff_file", "/home/loezerl-fworks/IdeaProjects/Experimenter/diabetes.arff");

        Classifier myClassifier = new KNN(7, 25, "euclidean");

//        int confirm =0;
//        int miss = 0;
//
//        conf.put("knn-n", 7);
//        conf.put("knn-wsize", 25);
//        conf.put("knn-f", "euclidean");
////        conf.put("my_classifier", myClassifier);
//        conf.put("my_confirm", confirm);
//        conf.put("my_miss", miss);

        Evaluator myEvaluator = new Prequential(myClassifier, file, builder, conf);

//        builder.setSpout("Instances", new GetInstances(), 10);
//        builder.setBolt("ED", new EuclideanDistanceBolt(), 2).shuffleGrouping("Instances");
//        builder.setBolt("Prequential", new Prequential.Classifier_Prequential(), 2).shuffleGrouping("Instances");
//        builder.setBolt("Prequential_Results", new Prequential.Prequential_Results(), 2).shuffleGrouping("Prequential");

        conf.setDebug(true);

        conf.setMaxTaskParallelism(3);
        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("storm-knn", conf, builder.createTopology());
        Utils.sleep(10000);
        cluster.killTopology("storm-knn");
        cluster.shutdown();
        myEvaluator.run();

    }
}
