import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import app.Dispositivo;
import app.Rede;
import app.Roteador;
import app.Servidor;
import app.Workstation;

public class MainBackup {
    private static Rede rede = new Rede();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws CloneNotSupportedException {
        int opcao;
        do {
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
            System.out.println("\nPressione Enter para continuar...");
            scanner.nextLine(); // Consome a nova linha pendente
            scanner.nextLine(); // Espera pelo Enter
        } while (opcao != 0);

        scanner.close();
    }

    private static void exibirMenu() {
        System.out.println("--- Menu de Simulação de Rede ---");
        System.out.println("1. Criar Dispositivo");
        System.out.println("2. Criar Conexão entre Dispositivos (com verificação de ciclo)");
        System.out.println("3. Encontrar Menor Caminho entre Dispositivos");
        System.out.println("4. Listar Dispositivos");
        System.out.println("5. Listar Conexões");
        System.out.println("0. Sair");
        System.out.print("Escolha uma opção: ");
    }

    private static int lerOpcao() {
        try {
            return scanner.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Entrada inválida. Por favor, digite um número.");
            scanner.next(); // Limpa a entrada inválida
            return -1; // Retorna uma opção inválida para repetir o loop
        }
    }

    private static void criarDispositivo() {
        System.out.print("Digite o tipo do dispositivo:\n1 - Roteador\n2 - Servidor\n3 - PC: ");
        int tipo = scanner.nextInt();
        System.out.print("Digite o nome do dispositivo: ");
        String nome = scanner.next();

        // Prevenindo adição de dispositivos com mesmo IP
        String ip;
        do{
            System.out.print("Digite o endereço IP: ");
            ip = scanner.next();
        } while (rede.getDispositivoByIP(ip) != null);

        Dispositivo novoDisp;

        switch (tipo) {
            case 1:
                novoDisp = new Roteador(nome, ip, "Cisco 290");
                break;
            case 2:
                novoDisp = new Servidor(nome, ip, "Ubuntu", 64);
                break;
            case 3:
                System.out.print("Nome o usuário deste computador: ");
                String usuarioNome = scanner.next();
                novoDisp = new Workstation(nome, ip, usuarioNome);
                break;
            default:
                System.out.println("Tipo de dispositivo inválido.");
                return;
        }
        rede.adicionarDispositivo(novoDisp);
    }

    private static void criarConexao() throws CloneNotSupportedException {
        if (rede.listarDispositivos().isEmpty()) {
            System.out.println("Não há dispositivos na rede para criar conexões. Crie dispositivos primeiro.");
            return;
        }

        listarTodosDispositivos();

        System.out.print("Digite o endereço IP do dispositivo de origem: ");
        String sourceDispIP = scanner.next();

        // tratar erros
        Dispositivo sourceDisp = rede.getDispositivoByIP(sourceDispIP);

        System.out.print("Digite o endereço IP do dispositivo de destino: ");
        String destinationDispIP = scanner.next();

        // tratar erros
        Dispositivo destinationDisp = rede.getDispositivoByIP(destinationDispIP);

        try {
            System.out.print("Digite a latência (peso) dessa conexão: ");
            float weight = scanner.nextFloat();
            rede.adicionarConexao(sourceDisp, destinationDisp, weight);
        } catch (InputMismatchException e) {
            System.out.println("Peso inválido. Por favor, digite um número.");
            scanner.next(); // Limpa a entrada inválida
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

        // tratar erros
        Dispositivo sourceDisp = rede.getDispositivoByIP(sourceDispIP);

        System.out.print("Digite o endereço IP do dispositivo de destino: ");
        String destinationDispIP = scanner.next();

        // tratar erros
        Dispositivo destinationDisp = rede.getDispositivoByIP(destinationDispIP);

        rede.simularCaminhoMinimo(sourceDisp, destinationDisp);
    }

    private static void listarTodosDispositivos() {
        ArrayList<Dispositivo> dispositivos = rede.listarDispositivos();
        if (dispositivos.isEmpty()) {
            System.out.println("Nenhum dispositivo cadastrado na rede.");
        } else {
            System.out.println("--- Dispositivos na Rede ---");
            for (Dispositivo d : dispositivos) {
                System.out.println(d);
            }
        }
    }

    private static void listarConexoesBFS() {
        System.out.println("Listando todas as conexões (BFS)");
        rede.simularDescobertaDeRedeBFS();
    }
}
