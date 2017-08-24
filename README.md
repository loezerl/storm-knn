# storm-knn

### Notas:
- Spouts: componente responsável pela leitura de tuplas de uma fonte externa de dados e pela emissão delas para a topoligia.
- Bolts: componente responsável pelo processamento dos dados.
- Testar o storm com estruturas do MOA.



- O calculo da distancia euclideana vai emitir uma tupla (Instance, Double), vou usar a mesma estratégia da variável que usei no flink.
- O prequential será um spout ou um bolt, ainda não tenho certeza.
- Os procedimentos do classificador serão bolts, provavelmente terei que usar a variavel para setar eles.
http://storm.apache.org/releases/1.1.0/Tutorial.html

