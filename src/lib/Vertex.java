package lib;

public class Vertex<T> {
    private T value; 

    Vertex(T value){
        this.value = value;
    }

    public T getValor() {
        return this.value;
    }

}
