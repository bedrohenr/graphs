import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import app.Dispositivo;
import app.Rede;
import app.Roteador;
import app.Servidor;
import app.Workstation;

public class Main {
    private static Rede rede = new Rede(false);
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws CloneNotSupportedException {

        // Inserindo valores iniciais
        Roteador roteadorMain = new Roteador("main", "192.168.0.1", "Cisco 2900");
        Workstation pcJoao = new Workstation("joao", "192.168.0.2", "joaozin");
        Workstation pcPedro = new Workstation("pedro", "192.168.0.3", "pp");
        Workstation pcMaria = new Workstation("mari", "192.168.0.4", "amari");

        Roteador roteadorSec = new Roteador("secondary", "192.168.20.1", "Cisco 2900");
        Workstation pcAndre = new Workstation("andre", "192.168.20.2", "andre");
        Workstation pcLara = new Workstation("lara", "192.168.20.3", "lara");

        Roteador roteadorTer = new Roteador("tertiary", "192.168.40.1", "Cisco 2900");
        Workstation pcMarco = new Workstation("dona", "192.168.40.2", "marco");
        Workstation pcDona = new Workstation("dona", "192.168.40.3", "dona");


        System.out.println("-- Conexões do Sistema Pré Criado --");
        rede.adicionarDispositivo(roteadorMain);
        rede.adicionarDispositivo(pcJoao);
        rede.adicionarDispositivo(pcPedro);
        rede.adicionarDispositivo(pcMaria);

        rede.adicionarConexao(pcJoao, roteadorMain, 4);
        rede.adicionarConexao(pcPedro, roteadorMain, 2);
        rede.adicionarConexao(pcMaria, roteadorMain, 6);

        rede.adicionarDispositivo(roteadorSec);
        rede.adicionarDispositivo(pcAndre);
        rede.adicionarDispositivo(pcLara);

        rede.adicionarConexao(roteadorSec, pcLara, 4);
        rede.adicionarConexao(roteadorSec, pcAndre, 9);


        rede.adicionarDispositivo(roteadorTer);
        rede.adicionarDispositivo(pcDona);
        rede.adicionarDispositivo(pcMarco);
        rede.adicionarConexao(roteadorTer, pcDona, 15);
        rede.adicionarConexao(roteadorTer, pcMarco, 23);

        rede.adicionarConexao(roteadorMain, roteadorSec, 35);
        rede.adicionarConexao( roteadorSec, roteadorTer,20);
        rede.adicionarConexao(roteadorTer, roteadorMain, 28);

        int opcao;
        do {
            try {
                exibirMenu();
                opcao = lerOpcao();

                switch (opcao) {
                    case 1:
                        criarDispositivo();
                        break;
                    case 2:
                        criarConexao();
                        break;
                    case 3:
                        encontrarCaminhoMinimo();
                        break;
                    case 4:
                        listarTodosDispositivos();
                        break;
                    case 5:
                        listarConexoesBFS();
                        break;
                    case 0:
                        System.out.println("Saindo do programa. Até mais!");
                        break;
                    default:
                        System.out.println("Opção inválida. Por favor, tente novamente.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, digite um número ou um formato correto.");
                scanner.nextLine(); // Consome a entrada inválida para evitar loop infinito
                opcao = -1; // Garante que o loop continue
            } catch (Exception e) { // Captura outras exceções inesperadas
                System.out.println("Ocorreu um erro inesperado: " + e.getMessage());
                // e.printStackTrace(); // Para depuração, descomente esta linha
                opcao = -1; // Garante que o loop continue
            }
            System.out.println("\nPressione Enter para continuar...");
            scanner.nextLine(); // Espera pelo Enter
        } while (opcao != 0);

        scanner.close();
    }

    private static void exibirMenu() {
        if(rede.isBidirecional()){
            System.out.println("\n--- Menu de Simulação de Rede (Bidirecional) ---");
        } else {
            System.out.println("\n--- Menu de Simulação de Rede (Direcional) ---");
        }

        System.out.println("1. Criar Dispositivo");
        if(rede.isBidirecional()){
            System.out.println("2. Criar Conexão Bidirecional entre Dispositivos (sem verificação de ciclo)");
        } else {
            System.out.println("2. Criar Conexão Direcional entre Dispositivos (com verificação de ciclo)");
        }
        System.out.println("3. Encontrar Menor Caminho entre Dispositivos");
        System.out.println("4. Listar Dispositivos");
        System.out.println("5. Simular Descoberta de Rede (BFS)"); // Nome mais descritivo
        System.out.println("0. Sair");
        System.out.print("Escolha uma opção: ");
    }

    private static int lerOpcao() {
        return scanner.nextInt(); // InputMismatchException será capturada no main
    }

    private static void criarDispositivo() {
        System.out.print("Digite o tipo do dispositivo:\n1 - Roteador\n2 - Servidor\n3 - PC\nSua escolha: ");
        int tipo = -1;
        try {
            tipo = scanner.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Erro: Tipo de dispositivo deve ser um número (1, 2 ou 3).");
            scanner.nextLine(); // Limpa o buffer
            return;
        }
        scanner.nextLine(); // Consome o \n após nextInt()

        System.out.print("Digite o nome do dispositivo: ");
        String nome = scanner.nextLine(); // Use nextLine para nomes com espaço

        String ip;
        // Loop para garantir IP único
        do {
            System.out.print("Digite o endereço IP (ex: 192.168.1.1): ");
            ip = scanner.nextLine().trim();
            if (rede.getDispositivoByIP(ip) != null) {
                System.out.println("Erro: Já existe um dispositivo com este IP. Por favor, digite outro IP.");
            }
        } while (rede.getDispositivoByIP(ip) != null);

        Dispositivo novoDisp = null;

        switch (tipo) {
            case 1:
                // Pode adicionar input para modelo aqui se quiser
                novoDisp = new Roteador(nome, ip, "Modelo Padrão");
                break;
            case 2:
                String os = "Ubuntu";
                int ramGB = 64;
                novoDisp = new Servidor(nome, ip, os, ramGB);
                break;
            case 3:
                System.out.print("Digite o nome do usuário deste computador: ");
                String usuarioNome = scanner.nextLine();
                novoDisp = new Workstation(nome, ip, usuarioNome);
                break;
            default:
                System.out.println("Tipo de dispositivo inválido.");
                return; // Sai do método se o tipo for inválido
        }

        if (novoDisp != null) {
            rede.adicionarDispositivo(novoDisp);
        }
    }

    private static void criarConexao() {
        if (rede.listarDispositivos().isEmpty()) {
            System.out.println("Não há dispositivos na rede para criar conexões. Crie dispositivos primeiro.");
            return;
        }

        listarTodosDispositivos();

        System.out.print("Digite o endereço IP do dispositivo de origem: ");
        String sourceDispIP = scanner.next();
        Dispositivo sourceDisp = rede.getDispositivoByIP(sourceDispIP);
        while(sourceDisp == null){
            System.out.print("\nErro: Dispositivo de origem com IP '" + sourceDispIP + "' não encontrado.\nTente novamente: ");
            sourceDispIP = scanner.next();
            sourceDisp = rede.getDispositivoByIP(sourceDispIP);
        }

        System.out.print("Digite o endereço IP do dispositivo de destino: ");
        String destinationDispIP = scanner.next();
        Dispositivo destinationDisp = rede.getDispositivoByIP(destinationDispIP);

        while(destinationDisp == null){
            System.out.print("\nErro: Dispositivo de destino com IP '" + destinationDispIP + "' não encontrado.\nTente novamente: ");
            destinationDispIP = scanner.next();
            destinationDisp = rede.getDispositivoByIP(destinationDispIP);
        }

        if (sourceDisp.equals(destinationDisp)) {
            System.out.println("Erro: Não é possível conectar um dispositivo a si mesmo.");
            return;
        }

        System.out.print("Digite a latência (peso) dessa conexão: ");
        float weight = scanner.nextFloat();

        while(weight < 0){
            System.out.print("Peso da conexão não pode ser negativo, tente novamente: ");
            weight = scanner.nextFloat();
        }

        try {
            rede.adicionarConexao(sourceDisp, destinationDisp, weight);
        } catch (CloneNotSupportedException e) {
            System.out.println("Erro interno ao verificar ciclo: A operação de clonagem não é suportada. " + e.getMessage());
            // e.printStackTrace(); // Para depuração
        }
    }

    private static void encontrarCaminhoMinimo() {
        if (rede.listarDispositivos().isEmpty()) {
            System.out.println("Não há dispositivos na rede para encontrar caminhos. Crie dispositivos primeiro.");
            return;
        }

        listarTodosDispositivos();

        System.out.print("Digite o endereço IP do dispositivo de origem: ");
        String sourceDispIP = scanner.next();
        Dispositivo sourceDisp = rede.getDispositivoByIP(sourceDispIP);

        while(sourceDisp == null){
            System.out.print("\nErro: Dispositivo de origem com IP '" + sourceDispIP + "' não encontrado.\nTente novamente: ");
            sourceDispIP = scanner.next().trim();
            sourceDisp = rede.getDispositivoByIP(sourceDispIP);
        }

        System.out.print("Digite o endereço IP do dispositivo de destino: ");
        String destinationDispIP = scanner.next();
        Dispositivo destinationDisp = rede.getDispositivoByIP(destinationDispIP);

        while(destinationDisp == null){
            System.out.print("\nErro: Dispositivo de destino com IP '" + destinationDispIP + "' não encontrado.\nTente novamente: ");
            destinationDispIP = scanner.next().trim();
            destinationDisp = rede.getDispositivoByIP(destinationDispIP);
        }

        rede.simularCaminhoMinimo(sourceDisp, destinationDisp);
    }

    private static void listarTodosDispositivos() {
        ArrayList<Dispositivo> dispositivos = rede.listarDispositivos();
        if (dispositivos.isEmpty()) {
            System.out.println("Nenhum dispositivo cadastrado na rede.");
        } else {
            System.out.println("\n--- Dispositivos na Rede ---");
            for (Dispositivo d : dispositivos) {
                System.out.println(d);
            }
        }
    }

    private static void listarConexoesBFS() {
        rede.simularDescobertaDeRedeBFS();
    }
}
