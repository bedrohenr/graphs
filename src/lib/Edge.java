package lib;

public class Edge {
   private Vertex source, destination;
   private float weight;

    public Edge(Vertex vSource, Vertex vDestination, float weight) {
        this.source = vSource;
        this.destination = vDestination;

        this.weight = weight;
    }

    public Vertex getSource(){
        return this.source;
    }

    public Vertex getDestination(){
        return this.destination;
    }

    public float getWeight(){
        return this.weight;
    }
}
