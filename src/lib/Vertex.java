package lib;

public class Vertex<T> {
    protected T value; 

    protected Vertex(T value){
        this.value = value;
    }

    public T getValor() {
        return this.value;
    }

}
