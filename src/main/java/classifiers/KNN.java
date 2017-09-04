package classifiers;

import com.yahoo.labs.samoa.instances.Instance;
import org.apache.storm.shade.com.fasterxml.jackson.core.util.InternCache;
import util.InstanceDouble;
import util.Similarity;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by loezerl-fworks on 24/08/17.
 */
public class KNN extends Classifier implements Serializable{

    private static final long serialVersionUID = 980377128340943649L;

    private int K;
    private int WindowSize;
    private String DistanceFunction;
    private List<Instance> Window;

    public KNN(int kneighbours, int wsize, String function) {
        K = kneighbours;
        WindowSize = wsize;
        if(function.equals("euclidean")){
            DistanceFunction = "euclidean";
        }
        else{
            System.out.println("Distancias disponiveis: euclidean");
            System.exit(1);
        }
        Window = new ArrayList<>(wsize);

    }

    public int getK() {
        return K;
    }

    public void setK(int k) {
        K = k;
    }

    public int getWindowSize() {
        return WindowSize;
    }

    public void setWindowSize(int windowSize) {
        WindowSize = windowSize;
    }

    public String getDistanceFunction() {
        return DistanceFunction;
    }

    public void setDistanceFunction(String distanceFunction) {
        DistanceFunction = distanceFunction;
    }

    public List<Instance> getWindow() {
        return Window;
    }

    public void setWindow(List<Instance> window) {
        Window = window;
    }

    @Override
    public boolean test(Instance example_) throws Exception{

        if(Window.size() == 0){return false;}

        //Calcula a distancia euclidiana entre a instancia do parametro e as instancias da janela
        Stream<InstanceDouble> distances = Window.stream().map(s -> new InstanceDouble(s, Similarity.EuclideanDistance(s, example_)));

        //Ordena as distancias
        distances = distances.sorted(
                new Comparator<InstanceDouble>() {
                    @Override
                    public int compare(InstanceDouble o1, InstanceDouble o2) {
                        if(o1.value > o2.value){return 1;}
                        else if(o1.value < o2.value){return -1;}
                        return 0;
                    }
                }
        );

        List<InstanceDouble> K_neighbours;
        //Pega os K vizinhos

        if (Window.size() < K){
            K_neighbours = new ArrayList<InstanceDouble>(distances.collect(Collectors.toList()).subList(0, Window.size()));
        }else{
            K_neighbours = new ArrayList<InstanceDouble>(distances.collect(Collectors.toList()).subList(0, K));
        }


        Map majorvote = new HashMap<Double, Integer>();

        for(InstanceDouble x: K_neighbours){
            if(majorvote.containsKey(x.key.classValue())){
                Integer aux = (Integer)majorvote.get(x.key.classValue());
                majorvote.put(x.key.classValue(), aux + 1);
            }else{
                majorvote.put(x.key.classValue(), 1);
            }
        }

        Integer bestclass_vote = -600;
        Double bestclass_label = -600.0;

        Iterator<Map.Entry<Double, Integer>> it = majorvote.entrySet().iterator();

        while(it.hasNext()){
            Map.Entry<Double, Integer> pair = it.next();
            if(pair.getValue() > bestclass_vote){
                bestclass_label = pair.getKey();
                bestclass_vote = pair.getValue();
            }
        }

        Double targetclass = example_.classValue();

        if(targetclass.equals(bestclass_label))
            return true;

        return false;
    }

    @Override
    public void train(Instance data){
        /**
         * Atente-se aqui em relação a exclusão mútua.
         * É provavel que as estruturas de array dos frameworks possuam mutex interno, mas é necessário verificar isso em cada framework.
         * */
        if (Window.size() < WindowSize) {
            Window.add(data);
        }
        else{
            Window.remove(0);
            Window.add(data);
        }
    }
}
