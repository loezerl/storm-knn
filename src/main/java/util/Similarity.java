package util;

/**
 * Created by loezerl-fworks on 24/08/17.
 */
import com.yahoo.labs.samoa.instances.Instance;


public class Similarity {
    public static double EuclideanDistance(Instance a, Instance b){
        double dist = 0.0;

        if(a.numAttributes() != b.numAttributes()){
            System.out.println("Instances with different sizes.");
            System.exit(1);
        }

        for (int i=0; i<b.numAttributes(); i++){
            if(a.classIndex() != i) {
                double x = a.value(i);
                double y = b.value(i);

                if (Double.isNaN(x) || Double.isNaN(y)) {
                    continue;
                }

                dist += (x - y) * (x - y);
            }
        }

        return Math.sqrt(dist);
    }
}