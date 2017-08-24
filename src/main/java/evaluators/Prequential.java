package evaluators;

import classifiers.Classifier;
import moa.streams.ArffFileStream;

/**
 * Created by loezerl-fworks on 24/08/17.
 */
public class Prequential extends Evaluator{
    private int confirm=0;
    private int miss=0;
    private int[][] confusion_matrix;

    public Prequential(Classifier _classifier, ArffFileStream data){ super(_classifier, data); }

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

        while(data_source.hasMoreInstances()){
            if(mClassifier.test(data_source.nextInstance().getData()))
                confirm++;
            else
                miss++;
            mClassifier.train(data_source.nextInstance().getData());
        }

        System.out.println("Total acertos: " + confirm + " Total erros: " + miss);

    }

}
