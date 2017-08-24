package classifiers;

import com.yahoo.labs.samoa.instances.Instance;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by loezerl-fworks on 24/08/17.
 */
public class KNN extends Classifier{

    private int K;
    private int WindowSize;
    private String DistanceFunction;
    private List<Instance> Window;

    public KNN(int kdistance, int wsize, String function) {
        K = kdistance;
        WindowSize = wsize;
        if(function == "euclidean"){
            DistanceFunction = "euclidean";
        }
        else{
            System.out.println("Distancias disponiveis: euclidean");
            System.exit(1);
        }
        Window = new ArrayList<>(wsize);

    }
    @Override
    public boolean test(Instance example_) throws Exception{return false;}

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
