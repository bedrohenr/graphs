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
}