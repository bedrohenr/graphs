package app;

public class Workstation extends Dispositivo {
    private String usuarioPrincipal;

    public Workstation(String hostname, String ipAddress, String usuarioPrincipal) {
        super(hostname, ipAddress, "Estação de Trabalho");
        this.usuarioPrincipal = usuarioPrincipal;
    }

    public String getUsuarioPrincipal() {
        return usuarioPrincipal;
    }

    @Override
    public String toString() {
        return "Estacao de Trabalho" + "\n" +
               "\tHostname: '" + getHostname() + '\'' + "\n" +
               "\tIP: '" + getIpAddress() + '\'' + "\n" +
               "\tUsuario: '" + usuarioPrincipal + '\'' + "\n";
    }
}
