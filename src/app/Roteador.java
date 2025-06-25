package app;

public class Roteador extends Dispositivo {
    private String modelo;

    public Roteador(String hostname, String ipAddress, String modelo) {
        super(hostname, ipAddress, "Roteador");
        this.modelo = modelo;
    }

    public String getModelo() {
        return modelo;
    }

    @Override
    public String toString() {
        return getIpAddress() + "\trt-" + getHostname();
    }
}