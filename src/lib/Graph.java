package lib;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class Graph<T>{
    public ArrayList<Edge> edges;
    protected ArrayList<Vertex<T>> vertices;

    public Graph(){
        this.edges = new ArrayList<>();
        this.vertices = new ArrayList<>();
    }

    public Vertex<T> addVertex(T value){
        Vertex<T> novo = new Vertex<T>(value);
        this.vertices.add(novo);
        return novo;
    }    

    public Vertex<T> getVertex(T value){
        Vertex<T> v;
        for(int i = 0; i < this.vertices.size(); i++){
            v = this.vertices.get(i);

            if(v.getValor().equals(value))
                return v;
        }
        return null;
    }

    public void addEdge(T source, T destination, float weight){
        Vertex<T> vSource, vDestination;
        Edge newEdge;

        vSource = getVertex(source);

        if(vSource == null)
            vSource = addVertex(source);

        vDestination = getVertex(destination);

        if(vDestination == null)
            vDestination = addVertex(destination);

        newEdge = new Edge(vSource, vDestination, weight);

        this.edges.add(newEdge);
    }

    private ArrayList<Edge> getDestinations(Vertex v){
        ArrayList<Edge> destinations = new ArrayList<>();
        Edge current;

        for(int i = 0; i < this.edges.size(); i++){
            current = this.edges.get(i);
            if(current.getSource().equals(v))
                destinations.add(current);
        }

        return destinations;
    }

    // Breadth-first search ou Busca em largura
    public void bfs(){
        ArrayList<Vertex> marked = new ArrayList<>();
        ArrayList<Vertex> queue = new ArrayList<>();
        Vertex current = this.vertices.get(0);

        queue.add(current);
        while(queue.size() > 0){
            current = queue.get(0);
            queue.remove(0);
            marked.add(current);
            System.out.println(current.getValor());

            ArrayList<Edge> destinations = this.getDestinations(current);
            Vertex next;

            for(int i = 0; i < destinations.size(); i++){
                next = destinations.get(i).getDestination();
                if(!marked.contains(next) && !queue.contains(next))
                    queue.add(next);
            }
        }
    }

    /**
     * Implementa o algoritmo de Dijkstra para encontrar o caminho mínimo
     * de um nó de partida para todos os outros nós em um grafo.
     *
     * @param startValue O valor do nó de partida.
     * @return Map.Entry contendo um mapa de distâncias (Vertex -> Float)
     * e um mapa de predecessores (Vertex -> Vertex). Retorna null se o nó de partida não for encontrado.
     */
    public Map.Entry<HashMap<Vertex<T>, Float>, HashMap<Vertex<T>, Vertex<T>>> dijkstra(T startValue) {
        Vertex<T> startNode = getVertex(startValue);
        if (startNode == null) {
            System.out.println("Nó de partida não encontrado: " + startValue);
            return null;
        }

        HashMap<Vertex<T>, Float> distances = new HashMap<>();
        HashMap<Vertex<T>, Vertex<T>> predecessors = new HashMap<>();
        PriorityQueue<NodeDistance> priorityQueue = new PriorityQueue<>();

        // Inicializa distâncias: infinito para todos, 0 para o nó de partida
        for (Vertex<T> vertex : vertices) {
            distances.put(vertex, Float.POSITIVE_INFINITY);
            predecessors.put(vertex, null); // Ninguém é predecessor inicialmente
        }
        distances.put(startNode, 0.0f);
        priorityQueue.add(new NodeDistance(startNode, 0.0f));

        while (!priorityQueue.isEmpty()) {
            NodeDistance current = priorityQueue.poll();
            Vertex<T> currentNode = current.vertex;
            float currentDistance = current.distance;

            // Se já encontramos um caminho mais curto para este nó, pule
            if (currentDistance > distances.get(currentNode)) {
                continue;
            }

            // Para cada vizinho do nó atual
            for (Edge edge : getDestinations(currentNode)) {
                Vertex<T> neighbor = edge.getDestination();
                float weight = edge.getWeight();
                float distanceThroughCurrentNode = currentDistance + weight;

                // Se encontrarmos um caminho mais curto para o vizinho
                if (distanceThroughCurrentNode < distances.get(neighbor)) {
                    distances.put(neighbor, distanceThroughCurrentNode);
                    predecessors.put(neighbor, currentNode);
                    priorityQueue.add(new NodeDistance(neighbor, distanceThroughCurrentNode));
                }
            }
        }
        
        // Retorna um Map.Entry contendo os dois HashMaps
        return new AbstractMap.SimpleEntry<>(distances, predecessors);
    }

    /**
     * Reconstrói o caminho mínimo de um nó de partida para um nó alvo
     * usando o mapa de predecessores.
     *
     * @param predecessors Mapa de predecessores retornado por Dijkstra.
     * @param startValue O valor do nó de partida.
     * @param targetValue O valor do nó alvo.
     * @return Uma lista de vértices representando o caminho mínimo.
     * Retorna uma lista vazia se não houver caminho.
     */
    public List<Vertex<T>> reconstructPath(HashMap<Vertex<T>, Vertex<T>> predecessors, T startValue, T targetValue) {
        List<Vertex<T>> path = new ArrayList<>();
        Vertex<T> startNode = getVertex(startValue);
        Vertex<T> targetNode = getVertex(targetValue);

        if (startNode == null || targetNode == null) {
            return path; // Retorna vazio se os nós não existirem
        }

        Vertex<T> current = targetNode;
        while (current != null) {
            path.add(current);
            if (current.equals(startNode)) {
                break;
            }
            current = predecessors.get(current);
        }
        Collections.reverse(path); // O caminho é construído de trás para frente, então inverta

        // Verifica se o caminho realmente começa do nó de partida
        if (path.isEmpty() || !path.get(0).equals(startNode)) {
            return new ArrayList<>(); // Não há caminho ou caminho incompleto
        }

        return path;
    }

/**
     * Verifica se o grafo contém ciclos usando uma abordagem de Busca em Profundidade (DFS).
     * Esta implementação é para grafos direcionados.
     * Para grafos não direcionados, a lógica de "parent" precisaria ser adicionada.
     *
     * @return true se um ciclo for detectado, false caso contrário.
     */
    public boolean hasCycle() {
        // visited: mantém o controle de todos os vértices que foram visitados durante a DFS
        HashSet<Vertex<T>> visited = new HashSet<>();
        // recursionStack: mantém o controle dos vértices que estão no caminho de recursão atual da DFS.
        // Se encontrarmos um vértice já no recursionStack, significa que há um ciclo.
        HashSet<Vertex<T>> recursionStack = new HashSet<>();

        // Itera por todos os vértices para garantir que todos os componentes conectados sejam verificados
        for (Vertex<T> vertex : vertices) {
            if (!visited.contains(vertex)) {
                if (dfsCheckCycle(vertex, visited, recursionStack)) {
                    return true; // Ciclo encontrado
                }
            }
        }
        return false; // Nenhum ciclo encontrado
    }

    /**
     * Método auxiliar recursivo para a Busca em Profundidade (DFS) para detecção de ciclo.
     *
     * @param currentVertex O vértice atual sendo visitado.
     * @param visited HashSet de vértices já visitados globalmente.
     * @param recursionStack HashSet de vértices no caminho de recursão atual.
     * @return true se um ciclo for detectado a partir deste vértice, false caso contrário.
     */
    private boolean dfsCheckCycle(Vertex<T> currentVertex, HashSet<Vertex<T>> visited, HashSet<Vertex<T>> recursionStack) {
        visited.add(currentVertex); // Marca o vértice como visitado
        recursionStack.add(currentVertex); // Adiciona ao caminho de recursão atual

        // Explora todos os vizinhos do vértice atual
        for (Edge edge : getDestinations(currentVertex)) {
            Vertex<T> neighbor = edge.getDestination();

            // Se o vizinho ainda não foi visitado, faz uma chamada recursiva
            if (!visited.contains(neighbor)) {
                if (dfsCheckCycle(neighbor, visited, recursionStack)) {
                    return true; // Ciclo encontrado em uma chamada recursiva
                }
            }
            // Se o vizinho já está no caminho de recursão (recursionStack), é um back-edge, então há um ciclo.
            else if (recursionStack.contains(neighbor)) {
                return true; // Ciclo encontrado
            }
        }

        // Remove o vértice do caminho de recursão antes de retornar (backtracking)
        recursionStack.remove(currentVertex);
        return false; // Nenhum ciclo encontrado a partir deste vértice e seus descendentes
    }
}