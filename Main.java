import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import app.Dispositivo;
import app.Rede;
import app.Roteador;
import app.Servidor;
import app.Workstation;

public class Main {
    private static Rede rede = new Rede();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
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
            scanner.nextLine(); // Consome a nova linha pendente (do nextInt/nextFloat)
            scanner.nextLine(); // Espera pelo Enter
        } while (opcao != 0);

        scanner.close();
    }

    private static void exibirMenu() {
        System.out.println("\n--- Menu de Simulação de Rede ---");
        System.out.println("1. Criar Dispositivo");
        System.out.println("2. Criar Conexão entre Dispositivos (com verificação de ciclo)");
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
        System.out.print("Digite o tipo do dispositivo:\n1 - Roteador\n2 - Servidor\n3 - PC: ");
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
            ip = scanner.nextLine();
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
                System.out.print("Digite o Sistema Operacional do servidor (ex: Ubuntu): ");
                String os = scanner.nextLine();
                System.out.print("Digite a RAM em GB do servidor: ");
                int ramGB = -1;
                try {
                    ramGB = scanner.nextInt();
                } catch (InputMismatchException e) {
                    System.out.println("Erro: RAM deve ser um número inteiro.");
                    scanner.nextLine(); // Limpa o buffer
                    return;
                }
                scanner.nextLine(); // Consome o \n após nextInt()
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
        if (sourceDisp == null) {
            System.out.println("Erro: Dispositivo de origem com IP '" + sourceDispIP + "' não encontrado.");
            return;
        }

        System.out.print("Digite o endereço IP do dispositivo de destino: ");
        String destinationDispIP = scanner.next();
        Dispositivo destinationDisp = rede.getDispositivoByIP(destinationDispIP);
        if (destinationDisp == null) {
            System.out.println("Erro: Dispositivo de destino com IP '" + destinationDispIP + "' não encontrado.");
            return;
        }

        if (sourceDisp.equals(destinationDisp)) {
            System.out.println("Erro: Não é possível conectar um dispositivo a si mesmo.");
            return;
        }

        try {
            System.out.print("Digite a latência (peso) dessa conexão: ");
            float weight = scanner.nextFloat();
            // scanner.nextLine(); // Consome o \n após nextFloat() se for usar nextLine depois

            rede.adicionarConexao(sourceDisp, destinationDisp, weight);
        } catch (InputMismatchException e) {
            System.out.println("Erro: Latência inválida. Por favor, digite um número.");
            scanner.nextLine(); // Limpa a entrada inválida
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
        if (sourceDisp == null) {
            System.out.println("Erro: Dispositivo de origem com IP '" + sourceDispIP + "' não encontrado.");
            return;
        }

        System.out.print("Digite o endereço IP do dispositivo de destino: ");
        String destinationDispIP = scanner.next();
        Dispositivo destinationDisp = rede.getDispositivoByIP(destinationDispIP);
        if (destinationDisp == null) {
            System.out.println("Erro: Dispositivo de destino com IP '" + destinationDispIP + "' não encontrado.");
            return;
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
