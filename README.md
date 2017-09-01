# storm-knn

### Notas:
- Spouts: componente responsável pela leitura de tuplas de uma fonte externa de dados e pela emissão delas para a topoligia.
- Bolts: componente responsável pelo processamento dos dados.
- Fiz uma gambiarra para conseguir fazer o bolt funcionar, devo arrumar isso para que fique mais sensato e genérico.
- No bolt do Prequential_Results, a função `cleanup` é responsável por imprimir o resultado da classificação. (**MELHORAR ISSO**)

**TODO:**
- Arrumar o erro: `java.lang.IllegalArgumentException: Topology conf is not json-serializable`.
- Consegui arrumar o erro acima adicionando os parametros do KNN no mapa Config, entretanto é uma solução grotesca.
- Procurar uma solução melhor para esse problema de colocar o objeto no Map.
- Pegar de algum jeito os valores true e false.
- Realizar testes com a base.
- Ver se é necessário exclusão mútua nas variáveis do objeto Prequential.
- Anotar quais parametros eu posso mudar para aumentar o número de threads e jobs.


Links:
https://stackoverflow.com/questions/32053795/how-to-use-apache-storm-tuple
http://storm.apache.org/releases/1.1.0/Tutorial.html