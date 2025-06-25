import app.Rede;
import app.Roteador;
import app.Servidor;
import app.Workstation;

public class MainTestes {
    public static void main(String[] args) throws CloneNotSupportedException {
        Rede minhaRede = new Rede();

        // Dispositivos  
        Servidor servidorWeb = new Servidor("Servidor Web", "192.168.1.10", "Ubuntu Apache", 32);
        Servidor servidorDatabase = new Servidor("Postgres DB", "192.168.1.11", "Windows Server", 64);
        Servidor servidorNuvem = new Servidor("servidor-nuvem", "203.0.113.45", "CentOS", 128);

        Workstation pcJoao = new Workstation("PC Joao", "192.168.1.20", "João Silva");
        Workstation pcPedro = new Workstation("PC Pedro", "192.168.1.21", "Pedro Ferreira");
        Workstation pcMaria = new Workstation("PC Eliz", "192.168.1.22", "Elisangela Paz");
        Roteador roteadorPrincipal = new Roteador("Roteador Principal", "192.168.1.1", "Cisco 2900");

        Workstation pcAndre = new Workstation("PC Andre", "192.168.10.3", "Andre Claudio");
        Workstation pcMarcelo = new Workstation("PC Marcelo", "192.168.10.2", "João Silva");
        Roteador roteadorSecundario = new Roteador("Roteador Secundario", "192.168.10.1", "Cisco 2900");

        Workstation pcYuri = new Workstation("PC Yuri", "192.168.20.3", "Yuri Claudio");
        Workstation pcDavi = new Workstation("PC Davi", "192.168.20.2", "Davi Santos");
        Roteador roteadorTerciario = new Roteador("Roteador Terciario", "192.168.20.1", "Cisco 2900");

        Roteador routerVPN = new Roteador("Roteador VPN", "10.0.0.1", "OpenVPN");

        System.out.println("Construindo a rede com objetos de dispositivos...");

        // Conexões do roteador principal
        minhaRede.adicionarConexao(roteadorPrincipal, servidorWeb, 5.0f);
        minhaRede.adicionarConexao(roteadorPrincipal, servidorDatabase, 3.0f);
        minhaRede.adicionarConexao(roteadorPrincipal, pcJoao, 8.0f);
        minhaRede.adicionarConexao(roteadorPrincipal, pcPedro, 8.0f);
        minhaRede.adicionarConexao(roteadorPrincipal, pcMaria, 10.0f);
        minhaRede.adicionarConexao(roteadorPrincipal, routerVPN, 20.0f);

        // Conexoes do roteador secundario
        minhaRede.adicionarConexao(roteadorSecundario, pcMarcelo, 11.0f);
        minhaRede.adicionarConexao(roteadorSecundario, pcAndre, 15.0f);

        // Conexões do roteador terciario
        minhaRede.adicionarConexao(roteadorTerciario, pcDavi, 8.0f);
        minhaRede.adicionarConexao(roteadorTerciario, pcYuri, 10.0f);

        // Conectando roteadores
        minhaRede.adicionarConexao(roteadorPrincipal, roteadorSecundario, 32.0f);
        minhaRede.adicionarConexao(roteadorSecundario, roteadorTerciario, 42.0f);
        minhaRede.adicionarConexao(roteadorTerciario,roteadorPrincipal, 24.0f);

        // Conexão do roteador VPN
        minhaRede.adicionarConexao(routerVPN, servidorNuvem, 50.0f);

        System.out.println("\nRede construída. Executando simulação de descoberta (BFS)...");
        minhaRede.simularDescobertaDeRedeBFS();

        System.out.println();

        System.out.println("-- Dikjstra: ");
        minhaRede.simularCaminhoMinimo(roteadorTerciario, routerVPN);

        System.out.println();

        System.out.println("-- Verificação de ciclo: ");
        System.out.print("A rede contém ciclo?: " + minhaRede.temCiclo());
    }
}
