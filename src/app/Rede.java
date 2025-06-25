package app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lib.Graph;
import lib.Vertex;

public class Rede implements Cloneable {

    // Agora o grafo é do tipo DispositivoRede
    private Graph<Dispositivo> redeGrafo;
    private ArrayList<Dispositivo> dispositivos;
    private boolean bidirecional;

    public Rede(boolean bidirecional) {
        this.redeGrafo = new Graph<Dispositivo>();
        this.dispositivos = new ArrayList<>();
        this.bidirecional = bidirecional;
    }

    public boolean isBidirecional(){
        return this.bidirecional;
    }

    // Classe clonável para testar se adicionar uma conexão ocasionará em um ciclo
    @Override
    protected Rede clone() throws CloneNotSupportedException {
        // Rede cloned = (Rede) super.clone();
        // return cloned;
        Rede clonedRede = (Rede) super.clone(); // Faz uma cópia rasa do objeto Rede

        // **PASSO ESSENCIAL**: Crie uma NOVA instância de Graph e copie seu conteúdo
        clonedRede.redeGrafo = new Graph<>(); // Ou chame redeGrafo.clone() se Graph.clone() for profundo
        
        // Copie os vértices para o novo grafo (pode ser a mesma referência se Dispositivo for imutável o suficiente,
        // caso contrário, Dispositivo também precisaria ser clonado)
        for (Dispositivo d : this.dispositivos) { // Assumindo 'dispositivos' é a lista de todos os dispositivos na Rede
            clonedRede.redeGrafo.addVertex(d); // Adiciona o MESMO objeto Dispositivo (se for ok)
        }

        // Copie as arestas para o novo grafo
        // IMPORTANTE: se Edge ou Vertex forem mutáveis, eles também precisariam ser clonados aqui
        for (Edge<Dispositivo> edge : this.redeGrafo.edges) {
            clonedRede.redeGrafo.addEdge(edge.getSource().getValor(), edge.getDestination().getValor(), edge.getWeight());
        }

        // Copie também a lista de dispositivos se for uma lista separada
        clonedRede.dispositivos = new ArrayList<>(this.dispositivos); // Faz uma cópia rasa da lista, mas os Dispositivos são os mesmos

        return clonedRede;
    }

    /**
     * Adiciona uma conexão entre dois dispositivos na rede.
     * Os dispositivos são agora passados como objetos DispositivoRede.
     * O método addEdge da sua lib.Graph já adiciona os vértices se eles não existirem.
     *
     * @param dispositivo1 Objeto DispositivoRede do primeiro dispositivo.
     * @param dispositivo2 Objeto DispositivoRede do segundo dispositivo.
     * @param latenciaMs Latência (peso) da conexão em milissegundos.
     * @throws CloneNotSupportedException 
     */
    public void adicionarConexao(Dispositivo dispositivo1, Dispositivo dispositivo2, float latenciaMs) throws CloneNotSupportedException {
        // Assegure-se de que os objetos DispositivoRede sejam os mesmos que já estão no grafo
        // ou que seus métodos equals/hashCode funcionem corretamente para que o getVertex/addVertex
        // da sua Graph identifique o vértice corretamente.
        // Sua lógica atual do addEdge na classe Graph já trata isso: ele verifica se o vértice
        // com o 'value' (que agora é um objeto DispositivoRede) já existe e o adiciona se não.

        // Teste de ciclo
        Rede tempGraph = this.clone();
        tempGraph.redeGrafo.addEdge(dispositivo1, dispositivo2, latenciaMs);

        // Verificar se o grafo temporário agora contém um ciclo
        if (tempGraph.redeGrafo.hasCycle()) { // Chama o método de verificação de ciclo
            System.out.println("Erro: A conexão de '" + dispositivo1.getIpAddress() + "' para '" + dispositivo2.getIpAddress() + "' criaria um ciclo na rede. Conexão não adicionada.");
        } else {
            // Se não houver ciclo, adicione a aresta ao grafo real
            // networkGraph.addEdge(source, destination, weight);
            redeGrafo.addEdge(dispositivo1, dispositivo2, latenciaMs);
            System.out.println("Conexão de '" + dispositivo1.getIpAddress() + "' para '" + dispositivo2.getIpAddress() + "' com peso adicionada com sucesso.");
        }

    }

    public void adicionarDispositivo(Dispositivo disp){
        dispositivos.add(disp);
    }

    /**
     * Realiza uma busca em largura na rede.
     */
    public void simularDescobertaDeRedeBFS() {
        redeGrafo.bfs(); // Seu BFS já imprime o valor do vértice (que agora é DispositivoRede)
    }


    /**
     * Djikstra.
     */
    public void simularCaminhoMinimo(Dispositivo startNodeValue, Dispositivo targetNodeValue){
        // Executando Dijkstra a partir do nó
        Map.Entry<HashMap<Vertex<Dispositivo>, Float>, HashMap<Vertex<Dispositivo>, Vertex<Dispositivo>>> result = 
            redeGrafo.dijkstra(startNodeValue);

        if (result != null) {
            HashMap<Vertex<Dispositivo>, Float> distances = result.getKey();
            HashMap<Vertex<Dispositivo>, Vertex<Dispositivo>> predecessors = result.getValue();

            System.out.println("Menores distâncias a partir de '" + startNodeValue + "':");
            for (Map.Entry<Vertex<Dispositivo>, Float> entry : distances.entrySet()) {
                System.out.println("  " + entry.getKey().getValor() + ": " + entry.getValue());
            }

            // Reconstruindo o caminho para um nó específico, por exemplo, para 'G'
            List<Vertex<Dispositivo>> pathToG = redeGrafo.reconstructPath(predecessors, startNodeValue, targetNodeValue);

            System.out.println("\nCaminho mínimo de '" + startNodeValue + "' para '" + targetNodeValue + "':");
            if (!pathToG.isEmpty()) {
                for (int i = 0; i < pathToG.size(); i++) {
                    System.out.print(pathToG.get(i).getValor());
                    if (i < pathToG.size() - 1) {
                        System.out.print(" -> ");
                    }
                }
                System.out.println();
            } else {
                System.out.println("Nenhum caminho encontrado.");
            }
        }
    }


    /**
     * Verificação de Ciclo.
     */
    public boolean temCiclo(){
        return redeGrafo.hasCycle();
    }

    public ArrayList<Dispositivo> listarDispositivos() {
        return dispositivos;
    }

    public Dispositivo getDispositivoByIP(String ip){
        for (Dispositivo d : this.dispositivos) {
            if (d.getIpAddress().equals(ip)) {
                return d;
            }
        }
        return null; // Not found
    } 

}
