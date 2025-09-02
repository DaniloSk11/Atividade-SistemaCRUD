package Controllers;

import Entities.Livro;
import Services.LivroService;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class LivroController {
    private final LivroService service;
    private final Scanner scanner;

    public LivroController(LivroService service) {
        this.service = service;
        this.scanner = new Scanner(System.in);
    }

    public void executar() {
        System.out.println("=== SISTEMA DE GESTÃO DE LIVROS ===");

        while (true) {
            exibirMenu();
            int opcao = lerOpcao();

            try {
                switch (opcao) {
                    case 1 -> cadastrarLivro();
                    case 2 -> listarLivros();
                    case 3 -> buscarLivroPorId();
                    case 4 -> atualizarLivro();
                    case 5 -> marcarComoIndisponivel();
                    case 6 -> marcarComoDisponivel();
                    case 7 -> removerLivro();
                    case 0 -> {
                        System.out.println("Saindo do sistema...");
                        return;
                    }
                    default -> System.out.println("Opção inválida! Tente novamente.");
                }
            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
            }

            System.out.println("\nPressione Enter para continuar...");
            scanner.nextLine();
        }
    }

    private void exibirMenu() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("       === SISTEMA DE GESTÃO DE LIVROS ===");
        System.out.println("=".repeat(50));
        System.out.println("1. Cadastrar Livro");
        System.out.println("2. Listar Todos os Livros");
        System.out.println("3. Buscar Livro por ID");
        System.out.println("4. Atualizar Livro");
        System.out.println("5. Marcar como Indisponível");
        System.out.println("6. Marcar como Disponível");
        System.out.println("7. Remover Livro");
        System.out.println("0. Sair");
        System.out.println("=".repeat(50));
        System.out.print("Digite sua opção: ");
    }

    private int lerOpcao() {
        try {
            int opcao = Integer.parseInt(scanner.nextLine().trim());
            return opcao;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private void cadastrarLivro() {
        System.out.println("\n=== CADASTRAR LIVRO ===");

        String titulo = lerString("Digite o título do livro: ");
        String autor = lerString("Digite o autor do livro: ");
        String isbn = lerString("Digite o ISBN do livro: ");
        int anoPublicacao = lerInteiro("Digite o ano de publicação: ");

        try {
            Livro livro = service.cadastrarLivro(titulo, autor, isbn, anoPublicacao);
            System.out.println("Livro cadastrado com sucesso!");
            System.out.println(livro);
        } catch (RuntimeException e) {
            System.out.println("Erro ao cadastrar: " + e.getMessage());
        }
    }

    private void listarLivros() {
        System.out.println("\n=== LISTA DE LIVROS ===");

        List<Livro> livros = service.listarLivros();
        if (livros.isEmpty()) {
            System.out.println("Nenhum livro cadastrado.");
            return;
        }

        System.out.println("Total de livros: " + livros.size());
        System.out.println("-".repeat(80));

        for (Livro livro : livros) {
            System.out.printf("ID: %d | Título: %s | Autor: %s%n",
                    livro.getId(), livro.getTitulo(), livro.getAutor());
            System.out.printf("ISBN: %s | Ano: %d | Status: %s%n",
                    livro.getIsbnFormatado(), livro.getAnoPublicacao(),
                    livro.isDisponivel() ? "Disponível" : "Indisponível");
            System.out.println("-".repeat(80));
        }
    }

    private void buscarLivroPorId() {
        System.out.println("\n=== BUSCAR LIVRO POR ID ===");

        int id = lerInteiro("Digite o ID do livro: ");

        try {
            Optional<Livro> livroOpt = service.buscarLivroPorId(id);
            if (livroOpt.isPresent()) {
                System.out.println("Livro encontrado:");
                exibirDetalhesLivro(livroOpt.get());
            } else {
                System.out.println("Livro com ID " + id + " não encontrado.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void buscarLivroPorIsbn() {
        System.out.println("\n=== BUSCAR LIVRO POR ISBN ===");

        String isbn = lerString("Digite o ISBN do livro: ");

        try {
            Optional<Livro> livroOpt = service.buscarLivroPorIsbn(isbn);
            if (livroOpt.isPresent()) {
                System.out.println("Livro encontrado:");
                exibirDetalhesLivro(livroOpt.get());
            } else {
                System.out.println("Livro com ISBN " + isbn + " não encontrado.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void atualizarLivro() {
        System.out.println("\n=== ATUALIZAR LIVRO ===");

        int id = lerInteiro("Digite o ID do livro a ser atualizado: ");

        Optional<Livro> livroOpt = service.buscarLivroPorId(id);
        if (livroOpt.isEmpty()) {
            System.out.println("Livro com ID " + id + " não encontrado.");
            return;
        }

        Livro livroAtual = livroOpt.get();
        System.out.println("Dados atuais do livro:");
        exibirDetalhesLivro(livroAtual);

        System.out.println("\nDigite os novos dados (pressione Enter para manter o valor atual):");

        String titulo = lerStringOpcional("Novo título (" + livroAtual.getTitulo() + "): ");
        if (titulo.isEmpty()) titulo = livroAtual.getTitulo();

        String autor = lerStringOpcional("Novo autor (" + livroAtual.getAutor() + "): ");
        if (autor.isEmpty()) autor = livroAtual.getAutor();

        String isbn = lerStringOpcional("Novo ISBN (" + livroAtual.getIsbn() + "): ");
        if (isbn.isEmpty()) isbn = livroAtual.getIsbn();

        String anoStr = lerStringOpcional("Novo ano (" + livroAtual.getAnoPublicacao() + "): ");
        int anoPublicacao = anoStr.isEmpty() ? livroAtual.getAnoPublicacao() : Integer.parseInt(anoStr);

        try {
            boolean sucesso = service.atualizarLivro(id, titulo, autor, isbn, anoPublicacao);
            if (sucesso) {
                System.out.println("Livro atualizado com sucesso!");
                Optional<Livro> livroAtualizado = service.buscarLivroPorId(id);
                livroAtualizado.ifPresent(this::exibirDetalhesLivro);
            } else {
                System.out.println("Erro ao atualizar o livro.");
            }
        } catch (RuntimeException e) {
            System.out.println("Erro ao atualizar: " + e.getMessage());
        }
    }

    private void removerLivro() {
        System.out.println("\n=== REMOVER LIVRO ===");

        int id = lerInteiro("Digite o ID do livro a ser removido: ");

        Optional<Livro> livroOpt = service.buscarLivroPorId(id);
        if (livroOpt.isEmpty()) {
            System.out.println("Livro com ID " + id + " não encontrado.");
            return;
        }

        System.out.println("Livro a ser removido:");
        exibirDetalhesLivro(livroOpt.get());

        String confirmacao = lerString("Confirma a remoção? (s/N): ");
        if (!confirmacao.toLowerCase().equals("s")) {
            System.out.println("Remoção cancelada.");
            return;
        }

        try {
            boolean sucesso = service.removerLivro(id);
            if (sucesso) {
                System.out.println("Livro removido com sucesso!");
            } else {
                System.out.println("Erro ao remover o livro.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void marcarComoIndisponivel() {
        System.out.println("\n=== MARCAR COMO INDISPONÍVEL ===");

        int id = lerInteiro("Digite o ID do livro: ");

        try {
            boolean sucesso = service.marcarComoIndisponivel(id);
            if (sucesso) {
                System.out.println("Livro marcado como indisponível com sucesso!");
            } else {
                System.out.println("Livro com ID " + id + " não encontrado.");
            }
        } catch (RuntimeException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void marcarComoDisponivel() {
        System.out.println("\n=== MARCAR COMO DISPONÍVEL ===");

        int id = lerInteiro("Digite o ID do livro: ");

        try {
            boolean sucesso = service.marcarComoDisponivel(id);
            if (sucesso) {
                System.out.println("Livro marcado como disponível com sucesso!");
            } else {
                System.out.println("Livro com ID " + id + " não encontrado.");
            }
        } catch (RuntimeException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void exibirEstatisticas() {
        System.out.println("\n=== ESTATÍSTICAS ===");

        int total = service.contarLivros();
        List<Livro> disponiveis = service.listarLivrosDisponiveis();
        List<Livro> indisponiveis = service.listarLivrosIndisponiveis();

        System.out.println("Total de livros cadastrados: " + total);
        System.out.println("Livros disponíveis: " + disponiveis.size());
        System.out.println("Livros indisponíveis: " + indisponiveis.size());

        if (total > 0) {
            double percentualDisponivel = (disponiveis.size() * 100.0) / total;
            System.out.printf("Percentual de disponibilidade: %.1f%%%n", percentualDisponivel);
        }
    }

    private void exibirDetalhesLivro(Livro livro) {
        System.out.println("-".repeat(50));
        System.out.println("ID: " + livro.getId());
        System.out.println("Título: " + livro.getTitulo());
        System.out.println("Autor: " + livro.getAutor());
        System.out.println("ISBN: " + livro.getIsbnFormatado());
        System.out.println("Ano de Publicação: " + livro.getAnoPublicacao());
        System.out.println("Status: " + (livro.isDisponivel() ? "Disponível" : "Indisponível"));
        System.out.println("-".repeat(50));
    }

    private String lerString(String mensagem) {
        System.out.print(mensagem);
        return scanner.nextLine().trim();
    }

    private String lerStringOpcional(String mensagem) {
        System.out.print(mensagem);
        return scanner.nextLine().trim();
    }

    private int lerInteiro(String mensagem) {
        while (true) {
            try {
                System.out.print(mensagem);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Por favor, digite um número válido.");
            }
        }
    }
}