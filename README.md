# storm-knn

### Notas:
- Spouts: componente responsável pela leitura de tuplas de uma fonte externa de dados e pela emissão delas para a topoligia.
- Bolts: componente responsável pelo processamento dos dados.
- No bolt do Prequential_Results, a função `cleanup` é responsável por imprimir o resultado da classificação. (**MELHORAR ISSO**).
- Serializei o classificador manualmente.

**TODO:**
- Realizar testes com a base.
- **Estou tendo problemas com paralelismo, alguns resultados além do esperado.**
- **Ver se é necessário exclusão mútua nas variáveis do objeto Prequential.**
- Parametros que eu posso mudar para aumentar o número de threads e job: `conf.setMaxTaskParallelism(x)`, `parallelism_hint`.


Links:
https://stackoverflow.com/questions/32053795/how-to-use-apache-storm-tuple
http://storm.apache.org/releases/1.1.0/Tutorial.html