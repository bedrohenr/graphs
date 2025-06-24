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
        // return "Roteador" + "\n" +
        //        "\tHostname: '" + getHostname() + '\'' + "\n" +
        //        "\tIP: '" + getIpAddress() + '\'' + "\n" +
        //        "\tModelo: '" + modelo + '\'' + "\n";

        return "[Router: " + modelo + ", " + getHostname() + ", " + getIpAddress() + "]";
    }
}