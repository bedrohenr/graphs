package lib;

import java.util.ArrayList;

public class Graph<T>{
    private ArrayList<Edge> edges;
    private ArrayList<Vertex<T>> vertices;

    private Vertex<T> addVertex(T value){
        Vertex<T> novo = new Vertex<T>(value);
        this.vertices.add(novo);
        return novo;
    }    

    private Vertex<T> getVertex(T value){
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

    // Breadth-first search
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
}