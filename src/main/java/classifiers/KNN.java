package classifiers;

import com.yahoo.labs.samoa.instances.Instance;
import util.InstanceDouble;
import util.Similarity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by loezerl-fworks on 24/08/17.
 */
public class KNN extends Classifier{

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

    public int getK(){return K;}
    public int getWindowSize(){return WindowSize;}
    public String getFunction(){return DistanceFunction;}

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

        int[] major_vote = new int[example_.classAttribute().numValues()];

        for(InstanceDouble x: K_neighbours){
            int aux = (int)x.key.classValue();
            major_vote[aux]++;
        }

        int bestclass_dist = -600;
        int bestclass_label = -600;


        for(int i=0; i< major_vote.length; i++){
            if(major_vote[i] > bestclass_dist){
                bestclass_label = i;
                bestclass_dist = major_vote[i];
            }
        }

        int targetclass = (int)example_.classValue();

        if(targetclass == bestclass_label)
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
