package lib;

public class Edge<T> {
   private Vertex<T> source, destination;
   private float weight;

    public Edge(Vertex<T> vSource, Vertex<T> vDestination, float weight) {
        this.source = vSource;
        this.destination = vDestination;
        this.weight = weight;
    }

    public Vertex<T> getSource(){
        return this.source;
    }

    public Vertex<T> getDestination(){
        return this.destination;
    }

    public float getWeight(){
        return this.weight;
    }
}
