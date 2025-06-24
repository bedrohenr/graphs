package lib;
// Classe auxiliar para o prio queue
class NodeDistance implements Comparable<NodeDistance> {
    Vertex vertex;
    float distance;

    public NodeDistance(Vertex vertex, float distance) {
        this.vertex = vertex;
        this.distance = distance;
    }

    @Override
    public int compareTo(NodeDistance other) {
        return Float.compare(this.distance, other.distance);
    }
}
