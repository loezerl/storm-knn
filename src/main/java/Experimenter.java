import com.yahoo.labs.samoa.instances.Instance;
import moa.streams.ArffFileStream;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.testing.TestWordSpout;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import org.apache.storm.utils.Utils;
import util.GetInstances;
import util.Similarity;

import java.util.Map;

import static util.GetInstances.LOG;

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

        @Override
        public void prepare(Map conf, TopologyContext context, OutputCollector collector) {
            _collector = collector;
        }

        @Override
        public void execute(Tuple tuple) {

            try {
                    Instance value = (Instance)tuple.getValue(0);
                    System.out.println(value);
                    Double dist = Similarity.EuclideanDistance(value, _Example);
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


        builder.setSpout("Instance", new GetInstances(file), 10);
        builder.setBolt("euclidean_distance", new EuclideanDistanceBolt(), 2);

//        builder.setSpout("word", new TestWordSpout(), 10);
//        builder.setBolt("exclaim1", new ExclamationBolt(), 3).shuffleGrouping("word");
//        builder.setBolt("exclaim2", new ExclamationBolt(), 2).shuffleGrouping("exclaim1");

        Config conf = new Config();
        conf.setDebug(false);


        if (args != null && args.length > 0) {
            conf.setNumWorkers(3);

            StormSubmitter.submitTopologyWithProgressBar(args[0], conf, builder.createTopology());
        }
        else {

            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("test", conf, builder.createTopology());
            Utils.sleep(10000);
            cluster.killTopology("test");
            cluster.shutdown();
        }
    }
}
