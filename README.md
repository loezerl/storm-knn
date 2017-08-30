# storm-knn

### Notas:
- Spouts: componente responsável pela leitura de tuplas de uma fonte externa de dados e pela emissão delas para a topoligia.
- Bolts: componente responsável pelo processamento dos dados.
- Testar o storm com estruturas do MOA.

- A topologia foi definida dentro do Prequential, não sei se vai funcionar, mas é lá que eu monto a topologia.
- O classificador se tornou um Bolt, entretando os métodos dele não vão utilizar o esquema de bolt/spout do storm dentro de sua estrutura.


**TODO:**
- Arrumar o erro: `java.lang.IllegalArgumentException: Topology conf is not json-serializable`.
- Realizar testes com a base.
- Ver se é necessário exclusão mútua nas variáveis do objeto Prequential.
- Anotar quais parametros eu posso mudar para aumentar o número de threads e jobs.


Links:
https://stackoverflow.com/questions/32053795/how-to-use-apache-storm-tuple
http://storm.apache.org/releases/1.1.0/Tutorial.html