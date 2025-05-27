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
}