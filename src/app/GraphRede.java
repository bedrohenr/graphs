package app;

import lib.Graph;
import lib.Vertex;

public class GraphRede<T> extends Graph<T> {
    
    public Vertex<Dispositivo> getVertexByIpAddress(String ipAddress) {
        for (Vertex<T> v : this.vertices) {
            Dispositivo value = (Dispositivo) v.getValor();
            if (value instanceof Dispositivo) {
                Dispositivo dispositivo = value;
                if (dispositivo.getIpAddress().equals(ipAddress)) {
                    return (Vertex<Dispositivo>) v;
                }
            }
        }
        return null; // Not found
    } 
}