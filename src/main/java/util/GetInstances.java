package util;

import moa.streams.ArffFileStream;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created by loezerl-fworks on 29/08/17.
 */




public class GetInstances extends BaseRichSpout {
    public static Logger LOG = LoggerFactory.getLogger(GetInstances.class);
    SpoutOutputCollector _collector;
    ArffFileStream _file;

    public GetInstances(){}

    @Override
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector){

        this._collector = collector;
        try {
            this._file = new ArffFileStream(conf.get("arff_file").toString(), -1);
        } catch (Exception e) {
            this._collector.reportError(e);
        }
    }


    @Override
    public void nextTuple(){
        if(_file.hasMoreInstances()){
            try {
                this._collector.emit(new Values(new Object[]{_file.nextInstance().getData()}));
            }catch (Exception e){
                this._collector.reportError(e);
            }
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer){
        declarer.declare(new Fields("Instance"));
    }
}
