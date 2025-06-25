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

    public Rede() {
        this.redeGrafo = new Graph<Dispositivo>();
        this.dispositivos = new ArrayList<>();
    }

    // Classe clonável para testar se adicionar uma conexão ocasionará em um ciclo
    @Override
    protected Rede clone() throws CloneNotSupportedException {
        Rede cloned = (Rede) super.clone();
        return cloned;
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
