package app;

// Exemplos de subclasses para tipos específicos de dispositivos
public class Servidor extends Dispositivo {
    private String sistemaOperacional;
    private int ramGB;

    public Servidor(String hostname, String ipAddress, String sistemaOperacional, int ramGB) {
        super(hostname, ipAddress, "Servidor");
        this.sistemaOperacional = sistemaOperacional;
        this.ramGB = ramGB;
    }

    public String getSistemaOperacional() {
        return sistemaOperacional;
    }

    public int getRamGB() {
        return ramGB;
    }

    @Override
    public String toString() {
        // return "Servidor" + "\n" +
        //        "\tHostname: '" + getHostname() + '\'' + "\n" +
        //        "\tIP: '" + getIpAddress() + '\'' + "\n" +
        //        "\tSO: '" + sistemaOperacional + '\'' + "\n" + 
        //        "\tRAM: " + ramGB + " GB" + "\n"; 

        return getIpAddress() + "\tsv-" + getHostname();
    }
}