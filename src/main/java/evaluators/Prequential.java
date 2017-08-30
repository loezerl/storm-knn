package evaluators;

import classifiers.Classifier;
import com.yahoo.labs.samoa.instances.Instance;
import moa.streams.ArffFileStream;
import org.apache.storm.Config;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import util.GetInstances;

import java.util.Map;

/**
 * Created by loezerl-fworks on 24/08/17.
 */
public class Prequential extends Evaluator{
    private int confirm=0;
    private int miss=0;
    private int[][] confusion_matrix;
    private TopologyBuilder _builder;

    public Prequential(Classifier _classifier, ArffFileStream data, int confirm_, int miss_){
        super(_classifier, data);
//        this._builder = builder;
//        conf.registerSerialization(Classifier.class);
//        conf.put("my_classifier", mClassifier);
//        conf.put("my_confirm", confirm);
//        conf.put("my_miss", miss);

        this.confirm = confirm_;
        this.miss = miss_;

//        this._builder.setSpout("Instances", new GetInstances(), 10);
//        this._builder.setBolt("Prequential", new Classifier_Prequential(), 2).shuffleGrouping("Instances");
//        this._builder.setBolt("Prequential_Results", new Prequential_Results(), 2).shuffleGrouping("Prequential");
    }

    @Override
    public void run() throws Exception{
        /**
         * Essa função é responsável por gerenciar a etapa de treino e teste do classificador.
         * Será responsável por:
         * - Passar as instancias para o test e em seguida o treino.
         * - Contabilizar os hits confirms e hit miss.
         * - Popular a Confusion Matrix
         * - Imprimir em real time os resultados obtidos, aka acuracia.
         * - Imprimir um relatório log final com os resultados, aka acuracia, calculos de erro (kappa), etc..
         *
         * */

        System.out.println("\n\n\nTotal acertos: " + confirm + " Total erros: " + miss);

    }

    public static class Classifier_Prequential extends BaseRichBolt{
        OutputCollector _collector;
        Classifier myClassifier;

        @Override
        public void prepare(Map conf, TopologyContext context, OutputCollector collector) {
            _collector = collector;
            try{
                myClassifier = (Classifier) conf.get("my_classifier");
            }catch (Exception e){
                _collector.reportError(e);
            }
        }

        @Override
        public void execute(Tuple tuple) {
            try {
                Instance value = (Instance)tuple.getValue(0);
                Boolean Result = myClassifier.test(value);
                myClassifier.train(value);
                _collector.emit(tuple, new Values(Result));
                _collector.ack(tuple);
            } catch(Exception e){
                _collector.reportError(e);
            }
        }

        @Override
        public void declareOutputFields(OutputFieldsDeclarer declarer) {
            declarer.declare(new Fields("classifier_result"));
        }
    }

    public static class Prequential_Results extends BaseRichBolt{
        OutputCollector _collector;
        int confirm;
        int miss;

        @Override
        public void prepare(Map conf, TopologyContext context, OutputCollector collector) {
            _collector = collector;
            try{
                confirm = (Integer) conf.get("my_confirm");
                miss = (Integer) conf.get("my_miss");
                _collector = collector;
            }catch (Exception e){
                _collector.reportError(e);
            }
        }

        @Override
        public void execute(Tuple tuple) {
            try {
                Boolean value = (Boolean) tuple.getValue(0);
                if(value){confirm++;}
                else{miss++;}
                _collector.ack(tuple);
            } catch(Exception e){
                _collector.reportError(e);
            }
        }

        @Override
        public void declareOutputFields(OutputFieldsDeclarer declarer) {
        }
    }

}
