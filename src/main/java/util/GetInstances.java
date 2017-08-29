package util;

import com.yahoo.labs.samoa.instances.Instance;
import moa.core.InputStreamProgressMonitor;
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
    public static Logger LOG = LoggerFactory.getLogger(InputStreamProgressMonitor.class);
    SpoutOutputCollector _collector;
    ArffFileStream _file;

    public GetInstances(ArffFileStream file){this._file = file;}

    @Override
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector){this._collector = collector;}

    @Override
    public void nextTuple(){
        if(_file.hasMoreInstances()){
            this._collector.emit(new Values(new Object[]{_file.nextInstance()}));
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer){
        declarer.declare(new Fields("Instance"));
    }
}
