package app;

// Classe base para qualquer dispositivo de rede
public abstract class Dispositivo {
    private String hostname;
    private String ipAddress;
    private String tipo; // SErvidor, Roteador, Estacao de Trabalho
    public Dispositivo(String hostname, String ipAddress, String tipo) {
        this.hostname = hostname;
        this.ipAddress = ipAddress;
        this.tipo = tipo;
    }

    // Getters
    public String getHostname() {
        return hostname;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getTipo() {
        return tipo;
    }

    // Métodos equals e hashCode baseados no IP para unicidade
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dispositivo that = (Dispositivo) o;
        return ipAddress.equals(that.ipAddress);
    }

    @Override
    public int hashCode() {
        return ipAddress.hashCode();
    }

    @Override
    public String toString() {
        return "DispositivoRede:" +
               "\tHostname='" + hostname + '\'' +
               "\tIP='" + ipAddress + '\'' +
               "\tTipo='" + tipo + '\'';
    }
}