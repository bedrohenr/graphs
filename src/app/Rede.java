package app;

import lib.Graph;

// Importar as classes de dispositivos que você criou
// package ...
// public abstract class DispositivoRede { ... }
// public class Servidor extends DispositivoRede { ... }
// public class EstacaoTrabalho extends DispositivoRede { ... }
// public class Roteador extends DispositivoRede { ... }

public class Rede {

    // Agora o grafo é do tipo DispositivoRede
    private Graph<Dispositivo> redeGrafo;

    public Rede() {
        this.redeGrafo = new Graph<Dispositivo>();
    }

    /**
     * Adiciona uma conexão entre dois dispositivos na rede.
     * Os dispositivos são agora passados como objetos DispositivoRede.
     * O método addEdge da sua lib.Graph já adiciona os vértices se eles não existirem.
     *
     * @param dispositivo1 Objeto DispositivoRede do primeiro dispositivo.
     * @param dispositivo2 Objeto DispositivoRede do segundo dispositivo.
     * @param latenciaMs Latência (peso) da conexão em milissegundos.
     */
    public void adicionarConexao(Dispositivo dispositivo1, Dispositivo dispositivo2, float latenciaMs) {
        // Assegure-se de que os objetos DispositivoRede sejam os mesmos que já estão no grafo
        // ou que seus métodos equals/hashCode funcionem corretamente para que o getVertex/addVertex
        // da sua Graph identifique o vértice corretamente.
        // Sua lógica atual do addEdge na classe Graph já trata isso: ele verifica se o vértice
        // com o 'value' (que agora é um objeto DispositivoRede) já existe e o adiciona se não.

        redeGrafo.addEdge(dispositivo1, dispositivo2, latenciaMs);
        // Para conexões de rede bidirecionais (físicas), adicione a aresta no sentido inverso
        redeGrafo.addEdge(dispositivo2, dispositivo1, latenciaMs);
    }

    /**
     * Realiza uma busca em largura na rede.
     */
    public void simularDescobertaDeRedeBFS() {
        System.out.println("--- Simulação de Descoberta de Rede (BFS) ---");
        redeGrafo.bfs(); // Seu BFS já imprime o valor do vértice (que agora é DispositivoRede)
    }

    // Você pode adicionar um método para buscar um dispositivo específico
    // se o seu Graph.getVertex() fosse público, ou se você adicionasse um
    // método similar na RedeDeComputadores.
    /*
    public DispositivoRede buscarDispositivoPorIP(String ip) {
        // Isso exigiria uma iteração sobre os vértices do grafo
        // ou uma forma de obter um Vertex<DispositivoRede> e acessar seu valor.
        // Sua `lib.Graph` não expõe diretamente os vértices, então precisaria de uma modificação lá
        // para buscar por um atributo específico do DispositivoRede (como o IP).
        return null;
    }
    */
}
