package evaluators;

import classifiers.Classifier;
import classifiers.KNN;
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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Map;

/**
 * Created by loezerl-fworks on 24/08/17.
 */
public class Prequential extends Evaluator{
    private int confirm=0;
    private int miss=0;
    private int[][] confusion_matrix;
    private TopologyBuilder _builder;

    public Prequential(Classifier _classifier, ArffFileStream data, TopologyBuilder builder, Config conf){
        super(_classifier, data);
        this._builder = builder;

        this._builder.setSpout("Instances", new GetInstances(), 1);
        this._builder.setBolt("Prequential", new Classifier_Prequential(), 8)
                .setNumTasks(1)
                .shuffleGrouping("Instances");
        this._builder.setBolt("Prequential_Results", new Prequential_Results(), 1)
                .shuffleGrouping("Prequential");
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
        Long instances_ = new Long(0);
        Long hits_ = new Long(0);

        @Override
        public void prepare(Map conf, TopologyContext context, OutputCollector collector) {
            _collector = collector;

            try {
                myClassifier = Classifier.getInstance();

            }catch(Exception e){
                System.err.println(e.getMessage());
                System.exit(1);
            }
        }

        @Override
        public void execute(Tuple tuple) {
            try {
                Instance value = (Instance)tuple.getValue(0);
                Boolean Result = myClassifier.test(value);
                myClassifier.train(value);
                if(Result){hits_++;}
                instances_++;
                _collector.emit(tuple, new Values(Result));
                _collector.ack(tuple);
            } catch(Exception e){
                _collector.reportError(e);
                System.err.println(e.getMessage());
            }
        }

        @Override
        public void declareOutputFields(OutputFieldsDeclarer declarer) {
            declarer.declare(new Fields("classifier_result"));
        }

        @Override
        public void cleanup(){

           // System.err.println("\n\n\nInstancias OSOSOSO: " + instances_ +  "\nConfirms: " + hits_);
        }
    }

    public static class Prequential_Results extends BaseRichBolt{
        OutputCollector _collector;
        Long confirm= new Long(0);
        Long miss= new Long(0);

        @Override
        public void prepare(Map conf, TopologyContext context, OutputCollector collector) {
            _collector = collector;
            try{
                _collector = collector;
            }catch (Exception e){
                _collector.reportError(e);
                System.err.println(e.getMessage());
            }
        }

        @Override
        public void execute(Tuple tuple) {
            try {
                Boolean value = (Boolean) tuple.getValue(0);
                if(value){
                    if(confirm > Long.MAX_VALUE - 1){
                        System.err.println("OVERFLOW OVERFLOW OVERFLOW");
                        System.exit(1);
                    }
                    confirm++;
                }
                else{miss++;}
                _collector.ack(tuple);
            } catch(Exception e){
                _collector.reportError(e);
                System.err.println(e.getMessage());
            }
        }

        @Override
        public void declareOutputFields(OutputFieldsDeclarer declarer) {
        }

        @Override
        public void cleanup(){

            float accuracy = confirm + miss;
            accuracy = (confirm*100)/accuracy;

            System.err.println("\n\n\nInstancias: " + (confirm+miss) + "\nTotal acertos: " + confirm + " Total erros: " + miss + "\nAcuracia: " + accuracy);
        }
    }

}
